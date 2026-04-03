package com.project.api.ecommerce.dto.response;

import java.math.BigDecimal;

public record CarrinhoItemResponseDTO(
        Long id,
        ProdutoResponseDTO produto,
        int quantidade,
        BigDecimal precoTotalItem ) {}
