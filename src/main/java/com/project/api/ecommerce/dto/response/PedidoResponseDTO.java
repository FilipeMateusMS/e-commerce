package com.project.api.ecommerce.dto.response;

import com.project.api.ecommerce.enums.PedidoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record PedidoResponseDTO(
        Long id,
        UsuarioResponseDTO usuario,
        Set<PedidoItemResponseDTO> itensPedidos,
        PedidoStatus pedidoStatus,
        BigDecimal precoTotal,
        LocalDate dataPedido ) {}
