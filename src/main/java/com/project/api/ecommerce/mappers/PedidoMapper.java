package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.response.PedidoResponseDTO;
import com.project.api.ecommerce.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring", uses = { UsuarioMapper.class, PedidoItemMapper.class } )
public interface PedidoMapper {

    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "itensPedidos", source = "itensPedidos")
    @Mapping(target = "precoTotal", expression = "java(pedido.calcularPrecoTotal())" )
    PedidoResponseDTO toDTO(Pedido pedido);
}
