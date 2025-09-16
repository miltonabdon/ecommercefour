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

    @Query(value = "SELECT created_by AS criadoPor, SUM(valor_total) AS totalGasto, COUNT(*) AS quantidadePedidos " +
            "FROM pedido WHERE status <> 'CANCELADO' AND created_by IS NOT NULL " +
            "GROUP BY created_by ORDER BY totalGasto DESC LIMIT :limit", nativeQuery = true)
    List<UsuarioTopProjecao> findTopUsersByTotalSpent(@Param("limit") int limit);

    @Query(value = "SELECT created_by AS criadoPor, AVG(valor_total) AS ticketMedio " +
            "FROM pedido WHERE status <> 'CANCELADO' AND created_by IS NOT NULL " +
            "GROUP BY created_by", nativeQuery = true)
    List<TicketMedioProjecao> findAvgTicketPerUser();

    @Query(value = "SELECT YEAR(data_criacao) AS ano, MONTH(data_criacao) AS mes, SUM(valor_total) AS total " +
            "FROM pedido WHERE status <> 'CANCELADO' " +
            "GROUP BY YEAR(data_criacao), MONTH(data_criacao) " +
            "ORDER BY ano, mes", nativeQuery = true)
    List<ReceitaMensalProjecao> findMonthlyRevenue();
}
