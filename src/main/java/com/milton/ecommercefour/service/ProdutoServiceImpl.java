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

        Date now = new Date();
        Produto updated = new Produto(
                id,
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getQuantidadeEstoque(),
                produto.getDataCriacao(),
                now
        );
        Produto saved = produtoRepository.save(updated);
        recalcPedidosTotalForProduct(id);
        return saved;
    }

    @Override
    public Produto create(Produto produto) {
        Date now = new Date();
        Produto toSave = new Produto(
                java.util.UUID.randomUUID(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getQuantidadeEstoque(),
                now,
                now
        );
        Produto saved = produtoRepository.save(toSave);

        return saved;
    }

    private void recalcPedidosTotalForProduct(UUID produtoId) {
        List<Pedido> pedidos = pedidoRepository.findByProdutos_Id(produtoId);
        if (pedidos == null || pedidos.isEmpty()) return;
        List<Pedido> atualizados = pedidos.stream()
                .map(p -> new Pedido(
                        p.getId(),
                        p.getProdutos(),
                        p.getStatus(),
                        p.getPago(),
                        calcularTotal(p.getProdutos()),
                        p.getCreatedBy(),
                        p.getDataCriacao()
                ))
                .toList();
        pedidoRepository.saveAll(atualizados);
    }

    private double calcularTotal(List<Produto> produtos) {
        if (produtos == null) return 0.0;
        return produtos.stream()
                .map(Produto::getPreco)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
