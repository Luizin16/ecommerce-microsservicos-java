package br.com.luizalves.pedido.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarPedidoRequest(
        @NotBlank(message = "cliente é obrigatório") String cliente,
        @NotBlank(message = "emailCliente é obrigatório") @Email(message = "emailCliente inválido") String emailCliente,
        @NotBlank(message = "produto é obrigatório") String produto,
        @NotNull(message = "quantidade é obrigatória") @Min(value = 1, message = "quantidade deve ser no mínimo 1") Integer quantidade,
        @NotNull(message = "valorUnitario é obrigatório") @DecimalMin(value = "0.01", message = "valorUnitario deve ser maior que zero") BigDecimal valorUnitario
) {
}
