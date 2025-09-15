package com.milton.ecommercefour.service;

import com.milton.ecommercefour.repository.PedidoRepository;
import com.milton.ecommercefour.repository.PedidoRepository.ReceitaMensalProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.TicketMedioProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.UsuarioTopProjecao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final PedidoRepository pedidoRepository;

    public AnalyticsServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<UsuarioTopProjecao> topUsuarios(int limit) {
        int effectiveLimit = limit <= 0 ? 5 : limit;
        return pedidoRepository.findTopUsersByTotalSpent(effectiveLimit);
        
    }

    @Override
    public List<TicketMedioProjecao> ticketMedioPorUsuario() {
        return pedidoRepository.findAvgTicketPerUser();
    }

    @Override
    public List<ReceitaMensalProjecao> receitaPorMes() {
        return pedidoRepository.findMonthlyRevenue();
    }
}
