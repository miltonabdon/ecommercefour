package com.milton.ecommercefour.controller;

import com.milton.ecommercefour.exception.RequisicaoInvalidaException;
import com.milton.ecommercefour.repository.PedidoRepository.TicketMedioProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.ReceitaMensalProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.UsuarioTopProjecao;
import com.milton.ecommercefour.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/top-usuarios")
    public List<UsuarioTopProjecao> topUsers(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        if (limit < 1) {
            throw new RequisicaoInvalidaException("Parâmetro 'limit' deve ser >= 1");
        }
        return analyticsService.topUsuarios(limit);
    }

    @GetMapping("/avg-ticket")
    public List<TicketMedioProjecao> averageTicketPerUser() {
        return analyticsService.ticketMedioPorUsuario();
    }

    @GetMapping("/receita-por-mes")
    public List<ReceitaMensalProjecao> revenueByMonth() {
        return analyticsService.receitaPorMes();
    }
}
