package com.project.api.ecommerce.security.config;

import com.project.api.ecommerce.security.jwt.AuthTokenFilter;
import com.project.api.ecommerce.security.jwt.JwtAuthEntryPoint;
import com.project.api.ecommerce.security.jwt.JwtUtils;
import com.project.api.ecommerce.security.user.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity( prePostEnabled = true )
public class ShopConfigSecurity {

    private final UsuarioDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtUtils jwtUtils;

    private static final String[] SECURED_URLS = {
            "api/v1/usuarios/**"
    };

    public ShopConfigSecurity( UsuarioDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint, JwtUtils jwtUtils ) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter( jwtUtils, userDetailsService );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration ) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService( userDetailsService );
        daoAuthProvider.setPasswordEncoder( passwordEncoder() );
        return daoAuthProvider;
    }

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {

        http.csrf( AbstractHttpConfigurer:: disable )
                .exceptionHandling( exception ->
                        exception.authenticationEntryPoint( authEntryPoint ) )
                .sessionManagement( session ->
                        session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( SECURED_URLS ).authenticated()
                        .anyRequest().permitAll()
                );

        http.authenticationProvider( daoAuthenticationProvider() );
        http.addFilterBefore( authTokenFilter(), UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }
}
