package com.project.api.ecommerce.dto;

import java.math.BigDecimal;

public record CategoriaProdutoDTO(
        Long id,
        String nome,
        String marca,
        String descricao,
        BigDecimal precoUnitario,
        Integer quantidade ) {}
