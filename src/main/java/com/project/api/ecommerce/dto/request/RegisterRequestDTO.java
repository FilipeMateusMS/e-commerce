package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank(message = "O telefone é obrigatório" )
        String telefone,

        @NotBlank(message = "O nome usuário é obrigatório" )
        @Size( min = 3, max = 120, message = "O nome do usuário deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "Email do usuário é obrigatório" )
        String email,

        @NotBlank(message = "A senha do usuário é obrigatório" )
        @Size( min = 8, max = 32, message = "A senha do usuário deve ter entre 8 e 32 caracteres")
        String password )
{}
