package com.milton.ecommercefour.service;

import com.milton.ecommercefour.domain.Pedido;
import com.milton.ecommercefour.domain.Produto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PedidoService {
    ResponseEntity<Object> processarPagamento(Pedido pedido);

    ResponseEntity<Object> geraPedido(Iterable<Produto> produtos);
}
