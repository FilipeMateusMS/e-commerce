package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequestDTO(
        @NotBlank(message = "O nome do usuário é obrigatório" )
        @Size(max = 120, message = "O nome do usuário deve ter no máximo 120 caracteres")
        String nome
) {}

