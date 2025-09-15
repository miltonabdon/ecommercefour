package com.milton.ecommercefour.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public record Produto(@Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id, String nome, String descricao, Double preco, String categoria, Double quantidadeEstoque, Date dataCriacao, Date dataAtualizacao) {
}
