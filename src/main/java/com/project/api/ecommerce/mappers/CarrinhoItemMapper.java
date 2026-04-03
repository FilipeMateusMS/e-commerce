package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.response.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.model.CarrinhoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProdutoMapper.class)
public interface CarrinhoItemMapper {

    @Mapping(target = "precoTotalItem", expression = "java(carrinhoItem.calcularPrecoItem())")
    CarrinhoItemResponseDTO toDTO(CarrinhoItem carrinhoItem);
}
