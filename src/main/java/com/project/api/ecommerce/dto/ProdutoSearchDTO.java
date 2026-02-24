package com.project.api.ecommerce.dto;

import java.math.BigDecimal;

public record ProdutoSearchDTO(
        String nome,
        String marca,
        String descricao,
        BigDecimal preco,
        Integer quantidade,
        String nomeCategoria ) {}
