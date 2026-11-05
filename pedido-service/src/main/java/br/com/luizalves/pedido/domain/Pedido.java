package br.com.luizalves.pedido.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private String emailCliente;

    @Column(nullable = false)
    private String produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    protected Pedido() {
        // exigido pelo JPA
    }

    /** Cria um pedido novo já com valor total calculado e status CRIADO. */
    public static Pedido novo(String cliente, String emailCliente, String produto,
                              Integer quantidade, BigDecimal valorUnitario) {
        Pedido pedido = new Pedido();
        pedido.cliente = cliente;
        pedido.emailCliente = emailCliente;
        pedido.produto = produto;
        pedido.quantidade = quantidade;
        pedido.valorUnitario = valorUnitario;
        pedido.valorTotal = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        pedido.status = StatusPedido.CRIADO;
        pedido.criadoEm = LocalDateTime.now();
        return pedido;
    }

    /** Regra de negócio: só é possível cancelar um pedido que ainda não foi cancelado. */
    public void cancelar() {
        if (status == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("O pedido " + id + " já está cancelado");
        }
        this.status = StatusPedido.CANCELADO;
    }

    public Long getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public String getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
