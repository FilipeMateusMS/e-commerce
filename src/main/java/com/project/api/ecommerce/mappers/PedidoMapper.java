package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.PedidoDTO;
import com.project.api.ecommerce.dto.UsuarioResponseDTO;
import com.project.api.ecommerce.model.Pedido;
import com.project.api.ecommerce.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoDTO toDTO(Pedido pedido) {
        Usuario usuario = pedido.getUsuario();
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail());

        return new PedidoDTO(
                pedido.getId(),
                usuarioResponseDTO,
                pedido.getItensPedidos(),
                pedido.getPedidoStatus(),
                pedido.getPrecoVendaTotal(),
                pedido.getDataPedido() );
    }
}
