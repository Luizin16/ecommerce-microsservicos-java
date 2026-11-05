package br.com.luizalves.notificacao.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;
    private Long pedidoId;
    private String destinatario;
    private String mensagem;
    private LocalDateTime enviadaEm;

    protected Notificacao() {
        // exigido pelo Spring Data
    }

    public Notificacao(Long pedidoId, String destinatario, String mensagem) {
        this.pedidoId = pedidoId;
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.enviadaEm = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getEnviadaEm() {
        return enviadaEm;
    }
}
