package br.com.luizalves.notificacao.repository;

import br.com.luizalves.notificacao.domain.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {

    List<Notificacao> findByPedidoIdOrderByEnviadaEmAsc(Long pedidoId);
}
