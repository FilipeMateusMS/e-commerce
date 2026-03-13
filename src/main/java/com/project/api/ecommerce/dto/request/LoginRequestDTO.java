package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank( message = "Deve ser fornecido o email" )
    String email,
    @NotBlank( message = "Deve ser fornecido a senha" )
    String password
) {}
