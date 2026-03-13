package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.request.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.response.UsuarioResponseDTO;
import com.project.api.ecommerce.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);
}
