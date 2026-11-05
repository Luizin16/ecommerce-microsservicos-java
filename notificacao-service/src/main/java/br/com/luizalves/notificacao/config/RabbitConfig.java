package br.com.luizalves.notificacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_PEDIDOS = "pedidos.exchange";
    public static final String ROUTING_KEY_PEDIDO = "pedido.evento";
    public static final String FILA_NOTIFICACOES = "notificacoes.pedidos";
    public static final String FILA_NOTIFICACOES_DLQ = "notificacoes.pedidos.dlq";

    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(EXCHANGE_PEDIDOS);
    }

    /** Fila principal: mensagens rejeitadas seguem para a DLQ. */
    @Bean
    public Queue filaNotificacoes() {
        return QueueBuilder.durable(FILA_NOTIFICACOES)
                .deadLetterExchange("")
                .deadLetterRoutingKey(FILA_NOTIFICACOES_DLQ)
                .build();
    }

    @Bean
    public Queue filaNotificacoesDlq() {
        return QueueBuilder.durable(FILA_NOTIFICACOES_DLQ).build();
    }

    @Bean
    public Binding bindingNotificacoes(Queue filaNotificacoes, TopicExchange pedidosExchange) {
        return BindingBuilder.bind(filaNotificacoes).to(pedidosExchange).with(ROUTING_KEY_PEDIDO);
    }

    /** Converte o JSON para o tipo do parâmetro do listener, sem depender da classe do produtor. */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
