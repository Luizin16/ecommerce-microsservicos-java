package br.com.luizalves.notificacao.service;

import br.com.luizalves.notificacao.event.PedidoEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * Monta o texto da notificação de acordo com o tipo do evento (padrão Strategy).
 * Para um novo tipo de evento, basta registrar uma nova estratégia no mapa.
 */
@Component
public class MensagemFactory {

    private static final NumberFormat REAIS = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    private final Map<String, Function<PedidoEvent, String>> estrategias = Map.of(
            "PEDIDO_CRIADO", e -> "Olá, " + e.cliente() + "! Seu pedido #" + e.pedidoId()
                    + " de " + e.produto() + " foi recebido. Total: " + formatar(e.valorTotal()) + ".",
            "PEDIDO_CANCELADO", e -> "Olá, " + e.cliente() + ". Seu pedido #" + e.pedidoId()
                    + " foi cancelado."
    );

    public String criar(PedidoEvent evento) {
        Function<PedidoEvent, String> estrategia = estrategias.get(evento.tipo());
        if (estrategia == null) {
            throw new IllegalArgumentException("Tipo de evento não suportado: " + evento.tipo());
        }
        return estrategia.apply(evento);
    }

    private static String formatar(BigDecimal valor) {
        return REAIS.format(valor).replace(' ', ' ');
    }
}
