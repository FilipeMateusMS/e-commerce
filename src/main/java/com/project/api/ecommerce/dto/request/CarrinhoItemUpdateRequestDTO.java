package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;

public record CarrinhoItemUpdateRequestDTO(
        @NotNull(message = "Deve ser fornecido a quantidade do produto")
        Integer quantidade) {}
