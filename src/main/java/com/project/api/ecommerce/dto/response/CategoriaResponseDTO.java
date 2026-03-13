package com.project.api.ecommerce.dto.response;

import java.util.List;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        List<ProdutoCategoriaResponseDTO> produtos ) {}
