package com.milton.ecommercefour.service;

import com.milton.ecommercefour.domain.Pedido;
import org.springframework.http.ResponseEntity;

public interface PedidoService {
    ResponseEntity<Object> processarPagamento(Pedido pedido);
}
