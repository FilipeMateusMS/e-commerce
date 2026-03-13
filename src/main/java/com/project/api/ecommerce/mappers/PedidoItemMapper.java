package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.response.PedidoItemResponseDTO;
import com.project.api.ecommerce.model.PedidoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring" )
public interface PedidoItemMapper {

    @Mapping(target = "precoVendaTotal", expression = "java(pedido.calcularValorTotalItem())")
    PedidoItemResponseDTO toDTO( PedidoItem pedido );
}
