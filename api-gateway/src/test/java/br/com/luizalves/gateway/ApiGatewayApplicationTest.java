package br.com.luizalves.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApiGatewayApplicationTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    @DisplayName("Deve registrar as rotas dos dois microsserviços")
    void deveRegistrarRotas() {
        List<String> rotas = routeLocator.getRoutes().map(Route::getId).collectList().block();

        assertThat(rotas).containsExactlyInAnyOrder("pedido-service", "notificacao-service");
    }
}
