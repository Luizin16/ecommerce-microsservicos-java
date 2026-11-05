package br.com.luizalves.notificacao.listener;

import br.com.luizalves.notificacao.event.PedidoEvent;
import br.com.luizalves.notificacao.service.NotificacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PedidoEventListenerTest {

    @Mock
    private NotificacaoService service;

    @InjectMocks
    private PedidoEventListener listener;

    @Test
    @DisplayName("Deve repassar o evento recebido da fila para o serviço")
    void deveProcessarEvento() {
        PedidoEvent evento = new PedidoEvent(1L, "PEDIDO_CRIADO", "Luiz", "luiz@email.com",
                "Teclado", new BigDecimal("150.00"));

        listener.aoReceber(evento);

        verify(service).registrar(evento);
    }
}
