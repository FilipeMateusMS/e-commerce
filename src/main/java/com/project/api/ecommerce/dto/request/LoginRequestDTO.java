package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

    @Email( message = "Deve ser um email válido")
    @NotBlank( message = "Deve ser fornecido o email" )
    String email,
    @NotBlank( message = "Deve ser fornecido a senha" )
    String password
) {}
