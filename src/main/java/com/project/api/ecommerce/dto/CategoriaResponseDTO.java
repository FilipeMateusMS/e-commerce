package com.project.api.ecommerce.dto;

import java.util.List;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        List<ProdutoCategoriaDTO> produtos ) {}
