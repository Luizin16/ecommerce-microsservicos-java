package br.com.luizalves.notificacao.service;

import br.com.luizalves.notificacao.domain.Notificacao;
import br.com.luizalves.notificacao.event.PedidoEvent;
import br.com.luizalves.notificacao.repository.NotificacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);

    private final NotificacaoRepository repository;
    private final MensagemFactory mensagemFactory;

    public NotificacaoService(NotificacaoRepository repository, MensagemFactory mensagemFactory) {
        this.repository = repository;
        this.mensagemFactory = mensagemFactory;
    }

    public Notificacao registrar(PedidoEvent evento) {
        String mensagem = mensagemFactory.criar(evento);
        Notificacao notificacao = repository.save(new Notificacao(evento.pedidoId(), evento.emailCliente(), mensagem));
        log.info("Notificação do pedido {} enviada para {}", evento.pedidoId(), evento.emailCliente());
        return notificacao;
    }

    public List<Notificacao> listar(Long pedidoId) {
        return pedidoId == null ? repository.findAll() : repository.findByPedidoIdOrderByEnviadaEmAsc(pedidoId);
    }
}
