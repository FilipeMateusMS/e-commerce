package com.project.api.ecommerce.security.jwt;

import com.project.api.ecommerce.security.user.ShopUsuarioDetails;
import com.project.api.ecommerce.security.user.UsuarioDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UsuarioDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = parseJwt( request );

            if( StringUtils.hasText( jwt ) && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );

                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                SecurityContextHolder.getContext().setAuthentication( authentication );
            }

            filterChain.doFilter( request, response );
        }
        catch ( JwtException e ) {
            throw new BadCredentialsException( "Invalid or expired token, you may login again",e );
        } catch ( Exception e ) {
            throw new AuthenticationServiceException( "Unexpected authentication error", e  );
        }
    }

    private String parseJwt( HttpServletRequest request ) {
        String headerAuth = request.getHeader("Authorization");
        if( StringUtils.hasText( headerAuth ) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring( 7 );
        }
        return null;
    }
}
