package com.project.api.ecommerce.security.jwt;

import com.project.api.ecommerce.security.user.ShopUsuarioDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value( "${auth.token.jwtSecret}" )
    private String jwtSecret;

    @Value( "${auth.token.expirationInMilis}" )
    private int expirationTime;

    private final Logger logger = LoggerFactory.getLogger( JwtUtils.class );

    public String generateTokenForUser( Authentication authentication ){
        ShopUsuarioDetails userPrincipal = (ShopUsuarioDetails) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map( GrantedAuthority::getAuthority )
                .toList();

        return Jwts.builder()
                .setSubject( userPrincipal.getEmail() )
                .claim( "id", userPrincipal.getId() )
                .claim( "roles", roles )
                .setIssuedAt( new Date() )
                .setExpiration( new Date( new Date().getTime() + expirationTime ) )
                .signWith( key(), SignatureAlgorithm.HS256 )
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor( Decoders.BASE64.decode( jwtSecret ) );
    }

    public String getUserNameFromJwtToken( String token ){
        return Jwts.parserBuilder()
                .setSigningKey( key() )
                .build()
                .parseClaimsJws( token )
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken( String token ){

        try{
            Jwts.parserBuilder()
                    .setSigningKey( key() )
                    .build()
                    .parseClaimsJws( token );
        }
        catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            logger.error("Falha na validação do Token JWT: {}", e.getMessage());
            throw new BadCredentialsException("Token inválido ou malformado", e);
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
            throw new CredentialsExpiredException("O token expirou", e);
        }
        return true;
    }
}
