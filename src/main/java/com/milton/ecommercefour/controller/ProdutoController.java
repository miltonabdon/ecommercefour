package com.milton.ecommercefour.controller;

import com.milton.ecommercefour.domain.Produto;
import com.milton.ecommercefour.exception.RecursoNaoEncontradoException;
import com.milton.ecommercefour.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public Iterable<Produto> findAllProdutos() {
        return produtoService.findAllProdutos();
    }

    @GetMapping("/{id}")
    public Produto findById(@PathVariable UUID id) {
        return produtoService.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto) {
        Produto created = produtoService.create(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable UUID id, @RequestBody Produto produto) {
        Produto existente = produtoService.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        // Keep original creation date if present in existing
        Produto toUpdate = new Produto(
                id,
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getQuantidadeEstoque(),
                existente.getDataCriacao(),
                null
        );
        Produto updated = produtoService.update(id, toUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        // Throws 404 if not found
        produtoService.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        produtoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
