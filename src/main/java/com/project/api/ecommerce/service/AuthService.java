package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.LoginRequestDTO;
import com.project.api.ecommerce.dto.request.RegisterRequestDTO;
import com.project.api.ecommerce.dto.response.AuthResponseDTO;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.model.Role;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.RoleRepository;
import com.project.api.ecommerce.repository.UsuarioRepository;
import com.project.api.ecommerce.security.jwt.JwtUtils;
import com.project.api.ecommerce.security.user.ShopUsuarioDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO login( LoginRequestDTO requestDTO ) {
        return login( requestDTO.email(), requestDTO.password() );
    }

    public AuthResponseDTO register( RegisterRequestDTO registerRequestDTO ) {
        Role roleUser = roleRepository.findByNome( "USER" )
                .orElseThrow( () ->  new IllegalStateException( "Role padrão não encontrada" ) );

        if( usuarioRepository.existsByEmail( registerRequestDTO.email() ) )
            throw new ResourceAlreadyExistsException( "Email já cadastrado" );

        Usuario usuario = new Usuario();
        usuario.setNome( registerRequestDTO.nome() );
        usuario.setEmail( registerRequestDTO.email() );
        usuario.setSenha( passwordEncoder.encode( registerRequestDTO.password() ) );
        usuario.setRoles( List.of( roleUser ) );
        usuario.setTelefone( registerRequestDTO.telefone() );

        usuarioRepository.save( usuario );

        return login( registerRequestDTO.email(), registerRequestDTO.password() );
    }

    public AuthResponseDTO login( String email, String password ) {
        Authentication authentication = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken( email, password ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );
        String jwtToken = jwtUtils.generateTokenForUser( authentication );
        ShopUsuarioDetails userDetails = (ShopUsuarioDetails) authentication.getPrincipal();

        return new AuthResponseDTO( userDetails.getId(), jwtToken );
    }
}
