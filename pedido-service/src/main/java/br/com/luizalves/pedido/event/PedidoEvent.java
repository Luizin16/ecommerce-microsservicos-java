package br.com.luizalves.pedido.event;

import br.com.luizalves.pedido.domain.Pedido;

import java.math.BigDecimal;

/**
 * Evento publicado no RabbitMQ quando um pedido é criado ou cancelado.
 * É o contrato entre o pedido-service e quem consome os eventos.
 */
public record PedidoEvent(
        Long pedidoId,
        String tipo,
        String cliente,
        String emailCliente,
        String produto,
        BigDecimal valorTotal
) {

    public static final String CRIADO = "PEDIDO_CRIADO";
    public static final String CANCELADO = "PEDIDO_CANCELADO";

    public static PedidoEvent de(Pedido pedido, String tipo) {
        return new PedidoEvent(
                pedido.getId(),
                tipo,
                pedido.getCliente(),
                pedido.getEmailCliente(),
                pedido.getProduto(),
                pedido.getValorTotal()
        );
    }
}
