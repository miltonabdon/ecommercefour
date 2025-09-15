package com.milton.ecommercefour.controller;

import com.milton.ecommercefour.domain.Produto;
import com.milton.ecommercefour.service.ProdutoService;
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
    public Optional<Produto> findById(@PathVariable UUID id) { return produtoService.findById(id); }

    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto) {
        Produto created = produtoService.create(produto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable UUID id, @RequestBody Produto produto) {
        Optional<Produto> existing = produtoService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Keep original creation date if present in existing
        Produto toUpdate = new Produto(
                id,
                produto.nome(),
                produto.descricao(),
                produto.preco(),
                produto.categoria(),
                produto.quantidadeEstoque(),
                existing.get().dataCriacao(),
                null
        );
        Produto updated = produtoService.update(id, toUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        Optional<Produto> existing = produtoService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
