package com.milton.ecommercefour.repository;

import com.milton.ecommercefour.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    List<Pedido> findByProdutos_Id(UUID produtoId);
    List<Pedido> findByCreatedBy(String createdBy);

    interface UsuarioTopProjecao {
        String getCriadoPor();
        Double getTotalGasto();
        Long getQuantidadePedidos();
    }

    interface TicketMedioProjecao {
        String getCriadoPor();
        Double getTicketMedio();
    }

    interface ReceitaMensalProjecao {
        Integer getAno();
        Integer getMes();
        Double getTotal();
    }

    @Query(value = "SELECT createdBy AS criadoPor, SUM(valorTotal) AS totalGasto, COUNT(*) AS quantidadePedidos " +
            "FROM pedido WHERE status <> 'CANCELADO' AND createdBy IS NOT NULL " +
            "GROUP BY createdBy ORDER BY totalGasto DESC LIMIT :limit", nativeQuery = true)
    List<UsuarioTopProjecao> findTopUsersByTotalSpent(@Param("limit") int limit);

    @Query(value = "SELECT createdBy AS criadoPor, AVG(valorTotal) AS ticketMedio " +
            "FROM pedido WHERE status <> 'CANCELADO' AND createdBy IS NOT NULL " +
            "GROUP BY createdBy", nativeQuery = true)
    List<TicketMedioProjecao> findAvgTicketPerUser();

    @Query(value = "SELECT YEAR(dataCriacao) AS ano, MONTH(dataCriacao) AS mes, SUM(valorTotal) AS total " +
            "FROM pedido WHERE status <> 'CANCELADO' " +
            "GROUP BY YEAR(dataCriacao), MONTH(dataCriacao) " +
            "ORDER BY ano, mes", nativeQuery = true)
    List<ReceitaMensalProjecao> findMonthlyRevenue();
}
