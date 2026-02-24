package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.AuthRequestDTO;
import com.project.api.ecommerce.dto.AuthResponseDTO;
import com.project.api.ecommerce.security.jwt.JwtUtils;
import com.project.api.ecommerce.security.user.ShopUsuarioDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService( AuthenticationManager authenticationManager, JwtUtils jwtUtils ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponseDTO login( AuthRequestDTO requestDTO ) {
        Authentication authentication = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken(
                        requestDTO.email(), requestDTO.password() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );
        String jwtToken = jwtUtils.generateTokenForUser( authentication );
        ShopUsuarioDetails userDetails = (ShopUsuarioDetails) authentication.getPrincipal();

        return new AuthResponseDTO( userDetails.getId(), jwtToken );
    }
}
