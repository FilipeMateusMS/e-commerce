package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.dto.CategoriaProdutoResponseDTO;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.dto.CategoriaResponseDTO;
import com.project.api.ecommerce.dto.ProdutoCategoriaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "produtos", source = "produtos")
    CategoriaResponseDTO toDTO(Categoria categoria);

    CategoriaProdutoResponseDTO toCategoriaProdutoResponse(Categoria categoria);

    // Método auxiliar para converter Produto -> ProdutoCategoriaDTO
    ProdutoCategoriaDTO toProdutoCategoriaDTO(Produto produto);
}
