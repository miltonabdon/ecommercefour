package com.milton.ecommercefour.service;

import com.milton.ecommercefour.domain.Pedido;
import com.milton.ecommercefour.domain.Produto;
import com.milton.ecommercefour.domain.Status;
import com.milton.ecommercefour.exception.EstoqueInsuficienteException;
import com.milton.ecommercefour.repository.PedidoRepository;
import com.milton.ecommercefour.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    private double calcularTotal(List<Produto> produtos) {
        if (produtos == null) return 0.0;
        return produtos.stream()
                .map(Produto::getPreco)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    @Override
    @Transactional(noRollbackFor = EstoqueInsuficienteException.class)
    public ResponseEntity<Object> processarPagamento(Pedido pedido) {
        if (pedido == null || pedido.getProdutos() == null || pedido.getProdutos().isEmpty()) {
            cancelarPedidoSePossivel(pedido);
            throw new EstoqueInsuficienteException("Pedido inválido: lista de produtos ausente. Pedido cancelado.");
        }

        Map<UUID, Long> solicitados = pedido.getProdutos().stream()
                .map(Produto::getId)
                .peek(id -> {
                    if (id == null) {
                        cancelarPedidoSePossivel(pedido);
                        throw new EstoqueInsuficienteException("Produto no pedido sem ID. Pedido cancelado.");
                    }
                })
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        Date now = new Date();
        List<Produto> atualizados = new ArrayList<>();

        for (Map.Entry<UUID, Long> entry : solicitados.entrySet()) {
            UUID produtoId = entry.getKey();
            long qtdSolicitada = entry.getValue();

            Optional<Produto> opt = produtoRepository.findById(produtoId);
            if (opt.isEmpty()) {
                cancelarPedidoSePossivel(pedido);
                throw new EstoqueInsuficienteException("Produto não encontrado: " + produtoId + ". Pedido cancelado.");
            }
            Produto existente = opt.get();

            Double disponivel = existente.getQuantidadeEstoque();
            if (disponivel == null || disponivel < qtdSolicitada) {
                String nome = existente.getNome() != null ? existente.getNome() : produtoId.toString();
                double disp = disponivel == null ? 0.0 : disponivel;
                cancelarPedidoSePossivel(pedido);
                throw new EstoqueInsuficienteException("Estoque insuficiente para produto " + nome + ". Solicitado: " + qtdSolicitada + ", disponível: " + disp + ". Pedido cancelado.");
            }

            Produto atualizado = new Produto(
                    existente.getId(),
                    existente.getNome(),
                    existente.getDescricao(),
                    existente.getPreco(),
                    existente.getCategoria(),
                    existente.getQuantidadeEstoque() - qtdSolicitada,
                    existente.getDataCriacao(),
                    now
            );
            atualizados.add(atualizado);
        }

        produtoRepository.saveAll(atualizados);

        return ResponseEntity.ok("Pagamento processado com sucesso.");
    }

    private void cancelarPedidoSePossivel(Pedido pedido) {
        try {
            if (pedido != null && pedido.getId() != null) {
                UUID id = pedido.getId();
                String username = currentUsername();
                Pedido existente = pedidoRepository.findById(id)
                        .orElse(new Pedido(
                                id,
                                pedido.getProdutos(),
                                Status.CANCELADO,
                                false,
                                calcularTotal(pedido.getProdutos()),
                                pedido.getCreatedBy() != null ? pedido.getCreatedBy() : username,
                                new Date()
                        ));
                Pedido cancelado = new Pedido(
                        existente.getId(),
                        existente.getProdutos(),
                        Status.CANCELADO,
                        false,
                        calcularTotal(existente.getProdutos()),
                        existente.getCreatedBy() != null ? existente.getCreatedBy() : username,
                        existente.getDataCriacao() != null ? existente.getDataCriacao() : new Date()
                );
                pedidoRepository.save(cancelado);
            }
        } catch (Exception ignored) {
            // Evitar que falhas de cancelamento impeçam a resposta ao usuário
        }
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }
}
