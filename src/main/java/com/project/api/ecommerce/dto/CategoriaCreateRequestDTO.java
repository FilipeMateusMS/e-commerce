package com.project.api.ecommerce.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record CategoriaCreateRequestDTO(
        @NotBlank( message = "Deve informar o nome da categoria" )
        @Size( min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres" )
        String nome,
        @NotEmpty( message = "A categoria deve possuir ao menos um produto associado" )
        List<Long> produtoIds ) {}
