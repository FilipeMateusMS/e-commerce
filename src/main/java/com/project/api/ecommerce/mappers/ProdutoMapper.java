package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.ProdutoCategoriaDTO;
import com.project.api.ecommerce.dto.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.ProdutoResponseDTO;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper( componentModel = "spring", uses = CategoriaMapper.class )
public interface ProdutoMapper {

    @Mapping(target = "categoria",
            source = "categoria")
    ProdutoResponseDTO toDTO(Produto produto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nome", source = "produtoDTO.nome")
    @Mapping(target = "marca", source = "produtoDTO.marca")
    @Mapping(target = "descricao", source = "produtoDTO.descricao")
    @Mapping(target = "precoUnitario", source = "produtoDTO.preco")
    @Mapping(target = "quantidade", source = "produtoDTO.quantidade")
    @Mapping(target = "categoria", source = "categoria")
    Produto toProduto(ProdutoRequestDTO produtoDTO, Categoria categoria);
}