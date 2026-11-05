package br.com.luizalves.pedido.controller;

import br.com.luizalves.pedido.domain.PedidoNaoEncontradoException;
import br.com.luizalves.pedido.domain.StatusPedido;
import br.com.luizalves.pedido.dto.PedidoResponse;
import br.com.luizalves.pedido.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService service;

    @Test
    @DisplayName("POST /pedidos deve retornar 201 com Location")
    void deveCriarPedido() throws Exception {
        PedidoResponse resposta = new PedidoResponse(1L, "Luiz", "Monitor", 2,
                new BigDecimal("900.00"), new BigDecimal("1800.00"), StatusPedido.CRIADO, LocalDateTime.now());
        when(service.criar(any())).thenReturn(resposta);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cliente":"Luiz","emailCliente":"luiz@email.com",
                                 "produto":"Monitor","quantidade":2,"valorUnitario":900.00}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/pedidos/1"))
                .andExpect(jsonPath("$.valorTotal").value(1800.00));
    }

    @Test
    @DisplayName("POST /pedidos deve retornar 400 quando os dados forem inválidos")
    void deveValidarCampos() throws Exception {
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cliente":"","emailCliente":"email-invalido",
                                 "produto":"Monitor","quantidade":0,"valorUnitario":900.00}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros", hasItem("emailCliente inválido")))
                .andExpect(jsonPath("$.erros", hasItem("quantidade deve ser no mínimo 1")));
    }

    @Test
    @DisplayName("GET /pedidos/{id} deve retornar 404 quando o pedido não existir")
    void deveRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new PedidoNaoEncontradoException(99L));

        mockMvc.perform(get("/pedidos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pedido 99 não encontrado"));
    }
}
