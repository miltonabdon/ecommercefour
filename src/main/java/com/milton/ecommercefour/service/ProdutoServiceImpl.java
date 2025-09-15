package com.milton.ecommercefour.service;

import com.milton.ecommercefour.domain.Pedido;
import com.milton.ecommercefour.domain.Produto;
import com.milton.ecommercefour.repository.PedidoRepository;
import com.milton.ecommercefour.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Iterable<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }

    @Override
    public Optional<Produto> findById(UUID id) {
        return produtoRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        produtoRepository.deleteById(id);
    }

    @Override
    public Optional<Produto> findByNome(String nome) {
        return produtoRepository.findByNome(nome);
    }

    @Override
    public Produto update(UUID id, Produto produto) {
        // Preserve ID and timestamps semantics: update dataAtualizacao to now
        Date now = new Date();
        Produto updated = new Produto(
                id,
                produto.nome(),
                produto.descricao(),
                produto.preco(),
                produto.categoria(),
                produto.quantidadeEstoque(),
                produto.dataCriacao(), // keep original if provided; if null, keep null
                now
        );
        Produto saved = produtoRepository.save(updated);
        // Recalculate totals for all Pedidos containing this product
        recalcPedidosTotalForProduct(id);
        return saved;
    }

    @Override
    public Produto create(Produto produto) {
        Date now = new Date();
        Produto toSave = new Produto(
                null,
                produto.nome(),
                produto.descricao(),
                produto.preco(),
                produto.categoria(),
                produto.quantidadeEstoque(),
                now,
                now
        );
        Produto saved = produtoRepository.save(toSave);
        // New product may not be referenced yet; no recalculation needed
        return saved;
    }

    private void recalcPedidosTotalForProduct(UUID produtoId) {
        List<Pedido> pedidos = pedidoRepository.findByProdutos_Id(produtoId);
        if (pedidos == null || pedidos.isEmpty()) return;
        List<Pedido> atualizados = pedidos.stream()
                .map(p -> new Pedido(p.id(), p.produtos(), p.status(), p.pago(), calcularTotal(p.produtos()), p.createdBy(), p.dataCriacao()))
                .toList();
        pedidoRepository.saveAll(atualizados);
    }

    private double calcularTotal(List<Produto> produtos) {
        if (produtos == null) return 0.0;
        return produtos.stream()
                .map(Produto::preco)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
