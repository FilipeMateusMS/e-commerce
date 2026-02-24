package com.project.api.ecommerce.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
            Long id,
            String nome,
            String marca,
            String descricao,
            BigDecimal preco,
            Integer quantidade,
            CategoriaProdutoResponseDTO categoria ) {}
