package br.com.luizalves.pedido.service;

import br.com.luizalves.pedido.domain.Pedido;
import br.com.luizalves.pedido.domain.PedidoNaoEncontradoException;
import br.com.luizalves.pedido.domain.StatusPedido;
import br.com.luizalves.pedido.dto.CriarPedidoRequest;
import br.com.luizalves.pedido.dto.PedidoResponse;
import br.com.luizalves.pedido.event.PedidoEvent;
import br.com.luizalves.pedido.event.PedidoEventPublisher;
import br.com.luizalves.pedido.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final PedidoEventPublisher publisher;

    public PedidoService(PedidoRepository repository, PedidoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public PedidoResponse criar(CriarPedidoRequest request) {
        Pedido pedido = Pedido.novo(
                request.cliente(),
                request.emailCliente(),
                request.produto(),
                request.quantidade(),
                request.valorUnitario()
        );
        Pedido salvo = repository.save(pedido);
        publisher.publicar(PedidoEvent.de(salvo, PedidoEvent.CRIADO));
        return PedidoResponse.de(salvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return PedidoResponse.de(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar(StatusPedido status) {
        List<Pedido> pedidos = status == null ? repository.findAll() : repository.findByStatus(status);
        return pedidos.stream().map(PedidoResponse::de).toList();
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = buscar(id);
        pedido.cancelar();
        Pedido salvo = repository.save(pedido);
        publisher.publicar(PedidoEvent.de(salvo, PedidoEvent.CANCELADO));
        return PedidoResponse.de(salvo);
    }

    private Pedido buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }
}
