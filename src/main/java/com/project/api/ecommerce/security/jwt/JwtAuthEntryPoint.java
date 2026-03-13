package com.project.api.ecommerce.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.handler.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException ) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED );

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                        HttpStatus.UNAUTHORIZED.value(),
                   "Unauthorized",
                "Authentication is required to access this resource.",
                        request.getServletPath() );

        mapper.writeValue( response.getOutputStream(), responseDTO );
    }
}
