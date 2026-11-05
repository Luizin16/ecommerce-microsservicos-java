package br.com.luizalves.notificacao.controller;

import br.com.luizalves.notificacao.domain.Notificacao;
import br.com.luizalves.notificacao.service.NotificacaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Notificacao> listar(@RequestParam(required = false) Long pedidoId) {
        return service.listar(pedidoId);
    }
}
