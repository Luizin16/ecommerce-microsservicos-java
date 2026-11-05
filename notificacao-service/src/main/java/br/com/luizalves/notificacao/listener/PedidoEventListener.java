package br.com.luizalves.notificacao.listener;

import br.com.luizalves.notificacao.config.RabbitConfig;
import br.com.luizalves.notificacao.event.PedidoEvent;
import br.com.luizalves.notificacao.service.NotificacaoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consome os eventos de pedido publicados no RabbitMQ.
 * Se o processamento falhar, a mensagem vai para a fila de erro (DLQ) em vez de se perder.
 */
@Component
public class PedidoEventListener {

    private final NotificacaoService service;

    public PedidoEventListener(NotificacaoService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.FILA_NOTIFICACOES)
    public void aoReceber(PedidoEvent evento) {
        service.registrar(evento);
    }
}
