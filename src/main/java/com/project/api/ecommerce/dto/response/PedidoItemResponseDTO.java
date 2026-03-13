package com.project.api.ecommerce.dto.response;

import java.math.BigDecimal;

public record PedidoItemResponseDTO(
    Long id,
    Integer quantidade,
    BigDecimal precoVendaUnitario,
    BigDecimal precoVendaTotal,
    ProdutoResponseDTO produto
){}
