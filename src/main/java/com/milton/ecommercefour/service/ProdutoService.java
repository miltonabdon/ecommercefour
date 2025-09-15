package com.milton.ecommercefour.service;

import com.milton.ecommercefour.domain.Produto;

import java.util.Optional;
import java.util.UUID;

public interface ProdutoService {

    Iterable<Produto> findAllProdutos();

    Optional<Produto> findById(UUID id);

    void deleteById(UUID id);

    Optional<Produto> findByNome(String nome);

    Produto update(UUID id, Produto produto);

    Produto create(Produto produto);

}
