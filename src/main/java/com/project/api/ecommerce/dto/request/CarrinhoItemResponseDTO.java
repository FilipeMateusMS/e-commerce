package com.project.api.ecommerce.dto.request;

import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;

import java.math.BigDecimal;

public record CarrinhoItemResponseDTO(
        Long id,
        ProdutoResponseDTO produto,
        int quantidade,
        BigDecimal precoTotalItem ) {}
