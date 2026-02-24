package com.project.api.ecommerce.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório" )
        @Size(max = 120, message = "O nome do produto deve ter no máximo 120 caracteres")
        String nome,

        @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres")
        String marca,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @PositiveOrZero(message = "A quantidade não pode ser negativa")
        @Max(value = 10000, message = "A quantidade máxima a ser incluída permitida é 10.000 unidades")
        Integer quantidade,

        @NotBlank( message = "O nome da categoria é obrigatório" )
        @Size(max = 120, message = "O nome da categoria deve ter no máximo 100 caracteres")
        String nomeCategoria
) {}

