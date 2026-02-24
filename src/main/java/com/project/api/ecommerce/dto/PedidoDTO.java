package com.project.api.ecommerce.dto;

import com.project.api.ecommerce.enums.PedidoStatus;
import com.project.api.ecommerce.model.PedidoItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record PedidoDTO(
        Long id,
        UsuarioResponseDTO usuario,
        Set<PedidoItem> itensPedidos,
        PedidoStatus pedidoStatus,
        BigDecimal precoTotal,
        LocalDate dataPedido ) {}
