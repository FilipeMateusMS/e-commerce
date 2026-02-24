package com.project.api.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.hateoas.RepresentationModel;

public record CarrinhoItemRequestDTO (
        @NotNull( message = "Deve ser fornecido o id do produto" )
        @Positive( message = "O id do produto deve ser maior que zero")
        Long idProduto,
        @NotNull( message = "Deve ser fornecido a quantidade do produto")
        @Positive( message = "A quantidade do produto deve ser maior que zero")
        Integer quantidade ) {}
