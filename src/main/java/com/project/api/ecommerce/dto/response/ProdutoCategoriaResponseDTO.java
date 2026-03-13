package com.project.api.ecommerce.dto.response;

import java.math.BigDecimal;

public record ProdutoCategoriaResponseDTO(
        Long id,
        String nome,
        String marca,
        String descricao,
        BigDecimal precoUnitario,
        Integer quantidade ) {}
