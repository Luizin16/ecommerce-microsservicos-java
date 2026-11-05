package br.com.luizalves.pedido.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PedidoTest {

    @Test
    @DisplayName("Deve calcular o valor total e iniciar com status CRIADO")
    void deveCalcularValorTotal() {
        Pedido pedido = Pedido.novo("Luiz", "luiz@email.com", "Teclado", 3, new BigDecimal("150.00"));

        assertThat(pedido.getValorTotal()).isEqualByComparingTo("450.00");
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CRIADO);
        assertThat(pedido.getCriadoEm()).isNotNull();
    }

    @Test
    @DisplayName("Deve cancelar um pedido criado")
    void deveCancelarPedido() {
        Pedido pedido = Pedido.novo("Luiz", "luiz@email.com", "Mouse", 1, new BigDecimal("80.00"));

        pedido.cancelar();

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CANCELADO);
    }

    @Test
    @DisplayName("Não deve cancelar um pedido que já está cancelado")
    void naoDeveCancelarDuasVezes() {
        Pedido pedido = Pedido.novo("Luiz", "luiz@email.com", "Mouse", 1, new BigDecimal("80.00"));
        pedido.cancelar();

        assertThatThrownBy(pedido::cancelar)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("já está cancelado");
    }
}
