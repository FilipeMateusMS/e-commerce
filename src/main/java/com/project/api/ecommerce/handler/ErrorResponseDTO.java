package com.project.api.ecommerce.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path,
        // Só aparece no JSON se não for null
        @JsonInclude( JsonInclude.Include.NON_NULL )
        List<FieldMessage> errors ) {

    // Sobrecarga do construtor
    public ErrorResponseDTO(LocalDateTime timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}