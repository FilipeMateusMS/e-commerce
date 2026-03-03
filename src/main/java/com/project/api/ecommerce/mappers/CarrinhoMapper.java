package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.CarrinhoResponseDTO;
import com.project.api.ecommerce.model.Carrinho;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CarrinhoItemMapper.class})
public interface CarrinhoMapper {

    @Mapping(target = "valorTotal", expression = "java(carrinho.calcularValorTotal())")
    @Mapping(target = "itens", source = "itensCarrinho")
    CarrinhoResponseDTO toResponseDTO( Carrinho carrinho );
}
