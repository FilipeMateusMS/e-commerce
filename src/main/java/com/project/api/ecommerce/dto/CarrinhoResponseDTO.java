package com.project.api.ecommerce.dto;

import com.project.api.ecommerce.model.CarrinhoItem;

import java.math.BigDecimal;
import java.util.Set;

public record CarrinhoResponseDTO(
        Long id,
        Set<CarrinhoItemResponseDTO> itens,
        BigDecimal valorTotal ) {}
