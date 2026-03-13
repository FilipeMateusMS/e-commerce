package com.project.api.ecommerce.security.user;

import com.project.api.ecommerce.model.Usuario;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopUsuarioDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public static ShopUsuarioDetails buildUserDetails(Usuario usuario ){
        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map( role -> new SimpleGrantedAuthority( role.getNome() ) )
                .collect( Collectors.toList() );

        return new ShopUsuarioDetails(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                authorities );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
