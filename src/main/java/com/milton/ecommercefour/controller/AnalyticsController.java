package com.milton.ecommercefour.controller;

import com.milton.ecommercefour.repository.PedidoRepository;
import com.milton.ecommercefour.repository.PedidoRepository.TicketMedioProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.ReceitaMensalProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.UsuarioTopProjecao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final PedidoRepository pedidoRepository;

    public AnalyticsController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // TOP N users that bought the most (by total spent)
    @GetMapping("/top-users")
    public List<UsuarioTopProjecao> topUsers(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        if (limit <= 0) limit = 5;
        return pedidoRepository.findTopUsersByTotalSpent(limit);
    }

    // Medium (Average) Ticket of the Pedidos for each User
    @GetMapping("/avg-ticket")
    public List<TicketMedioProjecao> averageTicketPerUser() {
        return pedidoRepository.findAvgTicketPerUser();
    }

    // Total Amount Earned money on Pedidos by month
    @GetMapping("/revenue-by-month")
    public List<ReceitaMensalProjecao> revenueByMonth() {
        return pedidoRepository.findMonthlyRevenue();
    }
}
