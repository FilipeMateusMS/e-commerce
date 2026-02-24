package com.project.api.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
    @NotBlank( message = "Deve ser fornecido o email" )
    String email,
    @NotBlank( message = "Deve ser fornecido a senha" )
    String password
) {}
