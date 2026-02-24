package com.project.api.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

public record CarrinhoItemUpdateDTO(
        @NotNull(message = "Deve ser fornecido a quantidade do produto")
        int quantidade) {}
