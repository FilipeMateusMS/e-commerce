package com.project.api.ecommerce.security.user;

import com.project.api.ecommerce.exceptions.AuthenticationFailedException;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticatedService {

    private final UsuarioRepository usuarioRepository;

    public Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationFailedException("Nenhum usuário autenticado encontrado!");

        if( !( authentication.getPrincipal() instanceof ShopUsuarioDetails ) )
            throw new RuntimeException( "Erro authentication é" + authentication.getPrincipal() );

        ShopUsuarioDetails principal = (ShopUsuarioDetails) authentication.getPrincipal();

        String email = principal.getUsername(); // retorna o e-mail
        return usuarioRepository.findByEmail( email )
                .orElseThrow(() -> new AuthenticationFailedException("Usuário logado não foi encontrado!"));
    }
}
