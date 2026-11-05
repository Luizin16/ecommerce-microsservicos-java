package br.com.luizalves.notificacao.service;

import br.com.luizalves.notificacao.domain.Notificacao;
import br.com.luizalves.notificacao.event.PedidoEvent;
import br.com.luizalves.notificacao.repository.NotificacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository repository;

    @Test
    @DisplayName("Deve salvar a notificação para o e-mail do cliente")
    void deveRegistrarNotificacao() {
        NotificacaoService service = new NotificacaoService(repository, new MensagemFactory());
        when(repository.save(any(Notificacao.class))).thenAnswer(inv -> inv.getArgument(0));
        PedidoEvent evento = new PedidoEvent(3L, "PEDIDO_CRIADO", "Ana", "ana@email.com",
                "Cadeira", new BigDecimal("700.00"));

        service.registrar(evento);

        ArgumentCaptor<Notificacao> salva = ArgumentCaptor.forClass(Notificacao.class);
        verify(repository).save(salva.capture());
        assertThat(salva.getValue().getPedidoId()).isEqualTo(3L);
        assertThat(salva.getValue().getDestinatario()).isEqualTo("ana@email.com");
        assertThat(salva.getValue().getMensagem()).contains("pedido #3");
    }
}
