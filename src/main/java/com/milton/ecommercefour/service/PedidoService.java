package com.milton.ecommercefour.service;

import com.milton.ecommercefour.domain.Pedido;

public interface PedidoService {
    String processarPagamento(Pedido pedido);
}
