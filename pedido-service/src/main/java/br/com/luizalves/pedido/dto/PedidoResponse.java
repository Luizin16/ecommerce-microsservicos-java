package br.com.luizalves.pedido.dto;

import br.com.luizalves.pedido.domain.Pedido;
import br.com.luizalves.pedido.domain.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponse(
        Long id,
        String cliente,
        String produto,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        StatusPedido status,
        LocalDateTime criadoEm
) {

    public static PedidoResponse de(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getProduto(),
                pedido.getQuantidade(),
                pedido.getValorUnitario(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                pedido.getCriadoEm()
        );
    }
}
