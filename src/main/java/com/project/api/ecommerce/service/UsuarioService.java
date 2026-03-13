package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.response.UsuarioResponseDTO;
import com.project.api.ecommerce.dto.request.UsuarioUpdateRequestDTO;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.UsuarioMapper;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;

    public PageResponse<UsuarioResponseDTO> findAllUsuario( Pageable pageable ) {
        Page<UsuarioResponseDTO> dtoPage = usuarioRepository.findAll( pageable )
                .map( usuarioMapper :: toDTO );
        return PageResponse.of( dtoPage );
    }

    public Usuario findUsuarioByIdEntity(Long idUsuario ){
        return usuarioRepository.findById( idUsuario )
                .orElseThrow( () -> new ResourceNotFoundException( "Usuário não encontrado" ) );
    }

    public UsuarioResponseDTO findUsuarioById(Long idUsuario ){
        return usuarioMapper.toDTO( usuarioRepository.findById( idUsuario )
                .orElseThrow( () -> new ResourceNotFoundException( "Usuário não encontrado" ) ) );
    }

    public UsuarioResponseDTO criarUsuario( UsuarioRequestDTO usuarioRequestDTO ) {
        if( usuarioRepository.existsByNome( usuarioRequestDTO.nome() ) )
            throw new ResourceAlreadyExistsException( "Já existe um usuário com o nome cadastrado" );

        Usuario usuario = usuarioMapper.toEntity( usuarioRequestDTO );
        usuario.setSenha( passwordEncoder.encode( usuarioRequestDTO.senha() ) ); // codifica a senha

        return usuarioMapper.toDTO( usuarioRepository.save( usuario ) );
    }

    public UsuarioResponseDTO alterarUsuario( UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, Long idUsuario ) {
        return usuarioMapper.toDTO( usuarioRepository.findById( idUsuario )
                .map( usuarioExistente -> {
                    usuarioExistente.setNome( usuarioUpdateRequestDTO.nome() );
                    return usuarioRepository.save( usuarioExistente );
                })
                .orElseThrow( () -> new ResourceNotFoundException( "Usuário não encontrado" ) )  );
    }

    public void deletarUsuario( Long idUsuario ) {
        usuarioRepository.findById( idUsuario )
                .ifPresentOrElse( usuarioRepository :: delete,
                        () ->{ throw new ResourceNotFoundException( "Usuário não encontrado" );
        });
    }
}
