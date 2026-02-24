package com.project.api.ecommerce.dto;

import com.project.api.ecommerce.model.Produto;

import java.math.BigDecimal;

public record CarrinhoItemResponseDTO(
        Long idCarrinhoItem,
        ProdutoResponseDTO produto,
        int quantidade,
        BigDecimal precoTotalItem ) {}
