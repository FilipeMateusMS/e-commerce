package com.project.api.ecommerce.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.handler.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public JwtAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        "Usuário não tem permissão para esta ação",
                        ex.getMessage(),
                        request.getServletPath() );

        mapper.writeValue( response.getOutputStream(), responseDTO );
    }
}
