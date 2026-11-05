package br.com.luizalves.pedido.event;

import br.com.luizalves.pedido.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica eventos de pedido no exchange do RabbitMQ (padrão Publisher/Subscriber).
 */
@Component
public class PedidoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PedidoEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PedidoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(PedidoEvent evento) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_PEDIDOS, RabbitConfig.ROUTING_KEY_PEDIDO, evento);
        log.info("Evento {} publicado para o pedido {}", evento.tipo(), evento.pedidoId());
    }
}
