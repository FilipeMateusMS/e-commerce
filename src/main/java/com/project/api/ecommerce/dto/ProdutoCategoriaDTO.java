package com.project.api.ecommerce.dto;

import java.math.BigDecimal;

public record ProdutoCategoriaDTO(
        Long id,
        String nome,
        String marca,
        String descricao,
        BigDecimal precoUnitario,
        Integer quantidade ) {}
