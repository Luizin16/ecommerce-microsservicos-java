package br.com.luizalves.pedido.service;

import br.com.luizalves.pedido.domain.Pedido;
import br.com.luizalves.pedido.domain.PedidoNaoEncontradoException;
import br.com.luizalves.pedido.domain.StatusPedido;
import br.com.luizalves.pedido.dto.CriarPedidoRequest;
import br.com.luizalves.pedido.dto.PedidoResponse;
import br.com.luizalves.pedido.event.PedidoEvent;
import br.com.luizalves.pedido.event.PedidoEventPublisher;
import br.com.luizalves.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private PedidoEventPublisher publisher;

    @InjectMocks
    private PedidoService service;

    private final CriarPedidoRequest request = new CriarPedidoRequest(
            "Luiz", "luiz@email.com", "Monitor", 2, new BigDecimal("900.00"));

    @Test
    @DisplayName("Deve salvar o pedido e publicar o evento PEDIDO_CRIADO")
    void deveCriarPedidoEPublicarEvento() {
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse resposta = service.criar(request);

        assertThat(resposta.valorTotal()).isEqualByComparingTo("1800.00");
        assertThat(resposta.status()).isEqualTo(StatusPedido.CRIADO);

        ArgumentCaptor<PedidoEvent> evento = ArgumentCaptor.forClass(PedidoEvent.class);
        verify(publisher).publicar(evento.capture());
        assertThat(evento.getValue().tipo()).isEqualTo(PedidoEvent.CRIADO);
        assertThat(evento.getValue().emailCliente()).isEqualTo("luiz@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedido inexistente")
    void deveLancarExcecaoQuandoNaoEncontrar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(PedidoNaoEncontradoException.class)
                .hasMessage("Pedido 99 não encontrado");
    }

    @Test
    @DisplayName("Deve filtrar pedidos por status quando informado")
    void deveListarPorStatus() {
        Pedido cancelado = Pedido.novo("Ana", "ana@email.com", "Cadeira", 1, new BigDecimal("700.00"));
        cancelado.cancelar();
        when(repository.findByStatus(StatusPedido.CANCELADO)).thenReturn(List.of(cancelado));

        List<PedidoResponse> pedidos = service.listar(StatusPedido.CANCELADO);

        assertThat(pedidos).hasSize(1);
        verify(repository, never()).findAll();
    }

    @Test
    @DisplayName("Deve cancelar o pedido e publicar o evento PEDIDO_CANCELADO")
    void deveCancelarPedido() {
        Pedido pedido = Pedido.novo("Luiz", "luiz@email.com", "Monitor", 1, new BigDecimal("900.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(repository.save(pedido)).thenReturn(pedido);

        PedidoResponse resposta = service.cancelar(1L);

        assertThat(resposta.status()).isEqualTo(StatusPedido.CANCELADO);
        ArgumentCaptor<PedidoEvent> evento = ArgumentCaptor.forClass(PedidoEvent.class);
        verify(publisher).publicar(evento.capture());
        assertThat(evento.getValue().tipo()).isEqualTo(PedidoEvent.CANCELADO);
    }
}
