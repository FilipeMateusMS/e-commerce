package com.project.api.ecommerce.security.user;

import com.project.api.ecommerce.exceptions.AuthenticationFailedException;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserAuthenticatedService {

    private final UsuarioRepository usuarioRepository;

    public Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationFailedException("Nenhum usuário autenticado encontrado!");

        if( !( authentication.getPrincipal() instanceof ShopUsuarioDetails ) )
            throw new RuntimeException( "Erro na autenticação" );

        ShopUsuarioDetails principal = (ShopUsuarioDetails) authentication.getPrincipal();

        String email = principal.getUsername(); // retorna o e-mail
        return usuarioRepository.findByEmail( email )
                .orElseThrow(() -> new AuthenticationFailedException("Usuário logado não foi encontrado!"));
    }
}
