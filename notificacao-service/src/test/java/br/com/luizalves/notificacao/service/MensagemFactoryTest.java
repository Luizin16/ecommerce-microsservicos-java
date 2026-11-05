package br.com.luizalves.notificacao.service;

import br.com.luizalves.notificacao.event.PedidoEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MensagemFactoryTest {

    private final MensagemFactory factory = new MensagemFactory();

    @Test
    @DisplayName("Deve montar a mensagem de pedido criado com o valor em reais")
    void mensagemPedidoCriado() {
        PedidoEvent evento = new PedidoEvent(7L, "PEDIDO_CRIADO", "Luiz", "luiz@email.com",
                "Monitor", new BigDecimal("1800.00"));

        assertThat(factory.criar(evento))
                .isEqualTo("Olá, Luiz! Seu pedido #7 de Monitor foi recebido. Total: R$ 1.800,00.");
    }

    @Test
    @DisplayName("Deve montar a mensagem de pedido cancelado")
    void mensagemPedidoCancelado() {
        PedidoEvent evento = new PedidoEvent(7L, "PEDIDO_CANCELADO", "Luiz", "luiz@email.com",
                "Monitor", new BigDecimal("1800.00"));

        assertThat(factory.criar(evento)).isEqualTo("Olá, Luiz. Seu pedido #7 foi cancelado.");
    }

    @Test
    @DisplayName("Deve rejeitar tipo de evento desconhecido")
    void tipoDesconhecido() {
        PedidoEvent evento = new PedidoEvent(7L, "PEDIDO_TELEPORTADO", "Luiz", "luiz@email.com",
                "Monitor", BigDecimal.TEN);

        assertThatThrownBy(() -> factory.criar(evento))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PEDIDO_TELEPORTADO");
    }
}
