package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.UsuarioResponseDTO;
import com.project.api.ecommerce.model.Usuario;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);
}
