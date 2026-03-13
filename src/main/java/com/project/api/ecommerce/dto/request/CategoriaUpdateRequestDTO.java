package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record CategoriaUpdateRequestDTO(
        @Size( min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres" )
        String nome,
        @Size(min = 1, message = "Se a lista de produtos for enviada, ela deve conter pelo menos um ID")
        List<Long> produtoIds ) {}
