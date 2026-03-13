package com.project.api.ecommerce.dto.filters;

import java.math.BigDecimal;

public record ProdutoFilterDTO(
        String nome,
        String marca,
        String descricao,
        BigDecimal preco,
        Integer quantidade,
        String nomeCategoria ) {}
