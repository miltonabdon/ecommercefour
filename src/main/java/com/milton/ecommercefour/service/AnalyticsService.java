package com.milton.ecommercefour.service;

import com.milton.ecommercefour.repository.PedidoRepository.ReceitaMensalProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.TicketMedioProjecao;
import com.milton.ecommercefour.repository.PedidoRepository.UsuarioTopProjecao;

import java.util.List;

public interface AnalyticsService {
    List<UsuarioTopProjecao> topUsuarios(int limit);
    List<TicketMedioProjecao> ticketMedioPorUsuario();
    List<ReceitaMensalProjecao> receitaPorMes();
}
