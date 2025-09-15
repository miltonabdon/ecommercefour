package com.milton.ecommercefour.repository;

import com.milton.ecommercefour.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    Optional<Produto> findByNome(String nome);
}
