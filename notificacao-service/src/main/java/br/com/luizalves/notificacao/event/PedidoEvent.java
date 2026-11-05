package br.com.luizalves.notificacao.event;

import java.math.BigDecimal;

/**
 * Cópia do contrato publicado pelo pedido-service.
 * Cada microsserviço mantém o seu próprio modelo: não existe dependência de código entre eles.
 */
public record PedidoEvent(
        Long pedidoId,
        String tipo,
        String cliente,
        String emailCliente,
        String produto,
        BigDecimal valorTotal
) {
}
