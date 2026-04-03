package com.project.api.ecommerce.dto.response;

import java.math.BigDecimal;
import java.util.Set;

public record CarrinhoResponseDTO(
        Long id,
        Set<CarrinhoItemResponseDTO> itens,
        BigDecimal valorTotal ) {}
