package com.project.api.ecommerce.security.user;

import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail( email )
                .orElseThrow(() -> new UsernameNotFoundException( "User not found!" ) );

        return ShopUsuarioDetails.buildUserDetails( usuario );
    }
}
