package br.com.luizalves.pedido.repository;

import br.com.luizalves.pedido.domain.Pedido;
import br.com.luizalves.pedido.domain.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatus(StatusPedido status);
}
