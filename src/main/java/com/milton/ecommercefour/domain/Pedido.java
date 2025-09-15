package com.milton.ecommercefour.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public record Pedido(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id,
        @ManyToMany
        @JoinTable(name = "pedido_produto",
                joinColumns = @JoinColumn(name = "pedido_id"),
                inverseJoinColumns = @JoinColumn(name = "produto_id"))
        List<Produto> produtos,
        Status status,
        Boolean pago,
        Double valorTotal,
        String createdBy,
        Date dataCriacao
) {
}
