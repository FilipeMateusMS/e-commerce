package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome do usuário é obrigatório" )
        @Size(max = 120, message = "O nome do usuário deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório" )
        String email,

        @NotBlank( message= "O telefone do usuário é obrigatório" )
        String telefone,

        @NotBlank(message = "A senha do usuário é obrigatório" )
        @Size( min = 8, max = 32, message = "A senha do usuário deve ter entre 8 e 32 caracteres")
        String senha ) {}
