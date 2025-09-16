package com.milton.ecommercefour.controller;

import com.milton.ecommercefour.domain.Pedido;
import com.milton.ecommercefour.domain.Produto;
import com.milton.ecommercefour.repository.PedidoRepository;
import com.milton.ecommercefour.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoService pedidoService, PedidoRepository pedidoRepository) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public Iterable<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Object> processarPagamento(@RequestBody Pedido pedido) {
        return pedidoService.processarPagamento(pedido);
    }

    @PostMapping("/gera")
    public ResponseEntity<Object> geraPedido(@RequestBody Iterable<Produto> produtos) {
        return pedidoService.geraPedido(produtos);
    }

    @GetMapping("/meusPedidos")
    public List<Pedido> listarMeusPedidos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;
        if (username == null) return List.of();
        return pedidoRepository.findByCreatedBy(username);
    }
}
