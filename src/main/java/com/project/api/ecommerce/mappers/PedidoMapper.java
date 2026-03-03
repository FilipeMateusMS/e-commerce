package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.PedidoDTO;
import com.project.api.ecommerce.dto.UsuarioResponseDTO;
import com.project.api.ecommerce.model.Pedido;
import com.project.api.ecommerce.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper( componentModel = "spring", uses = { UsuarioMapper.class } )
public interface PedidoMapper {

    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "itensPedidos", source = "itensPedidos")
    PedidoDTO toDTO(Pedido pedido);

}
