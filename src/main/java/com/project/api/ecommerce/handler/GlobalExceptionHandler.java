package com.project.api.ecommerce.handler;

import com.project.api.ecommerce.exceptions.*;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. Trata Recursos Não Encontrados (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.info( "Recurso não encontrado");
        return buildResponseDefault(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request);
    }

    // 2. Trata Conflitos/Recursos Duplicados (409)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        log.info( "Recurso já existe");
        return buildResponseDefault(HttpStatus.CONFLICT, "Recurso já existe", ex.getMessage(), request);
    }

    // Quando um endpoint não é encontrado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn( "Rota não encontrada");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Método não suportado: " + ex.getMethod());
    }

    // 3. Trata Regra de Negócio: 400
    @ExceptionHandler(BusinessAlertException.class)
    public ResponseEntity<ErrorResponseDTO> handleEstoque(BusinessAlertException ex, HttpServletRequest request) {
        return buildResponseDefault(HttpStatus.UNPROCESSABLE_ENTITY, "Erro de requisitos de negócio", ex.getMessage(), request);
    }

    // 4. Trata Falhas de armazenamento de arquivos
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileExceptions(RuntimeException ex, HttpServletRequest request) {
        log.info( "Erro nos arquivos");
        return buildResponseDefault(HttpStatus.INTERNAL_SERVER_ERROR, "Erro de processamento de arquivo", ex.getMessage(), request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDTO> handlJwtInvalid(AuthenticationFailedException ex, HttpServletRequest request) {
        log.warn( "Erro de JWT={}", ex.getMessage() );
       return buildResponseDefault(
                HttpStatus.UNAUTHORIZED,
                "Erro de autenticação",
                "Faça o login",
                request );
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationFailed(AuthenticationFailedException ex, HttpServletRequest request) {
        log.warn( "Auntentiação inválida={}", ex.getMessage() );
        return buildResponseDefault(
                HttpStatus.UNAUTHORIZED,
                "Erro de autenticação",
                "Faça o login",
                request );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.warn( "Endpoint não encontrado");
        return buildResponseDefault(HttpStatus.NOT_FOUND, "Endpoint não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler( BadCredentialsException.class )
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials( BadCredentialsException ex, HttpServletRequest request ) {
        log.warn( "Credeciais inválidas" );
        return buildResponseDefault( HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid email or password.", request );
    }

    // Erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleSuperiorException(Exception ex, HttpServletRequest request) {
        log.error( "Erro={}", ex.getMessage() );
        log.error( "StackTrace={}", (Object) ex.getStackTrace());
        return buildResponseDefault(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado no servidor",
                request );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldMessage> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add( new FieldMessage( error.getField(), error.getDefaultMessage() ) )
        );
        return buildResponseField(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação",
                "Um ou mais campos estão inválidos",
                request,
                errors );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request) {
        List<FieldMessage> errors = new ArrayList<>();

        // Esta exceção agrupa os erros por resultado de validação de cada parâmetro
        ex.getAllValidationResults().forEach(result -> {
            String parameterName = result.getMethodParameter().getParameterName();
            result.getResolvableErrors().forEach(error -> {
                errors.add(new FieldMessage(parameterName, error.getDefaultMessage()));
            });
        });

        return buildResponseField(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação",
                "Parâmetros de requisição inválidos",
                request,
                errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(BindException ex, HttpServletRequest request) {
        List<FieldMessage> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(new FieldMessage(error.getField(), error.getDefaultMessage()))
        );

        return buildResponseField(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação de Formulário",
                "Um ou mais campos do formulário estão inválidos",
                request,
                errors);
    }

    @ExceptionHandler( AccessDeniedException.class )
    public ResponseEntity<ErrorResponseDTO> handleAcessDeniedException( AccessDeniedException ex, HttpServletRequest request ){
        return buildResponseDefault(
                HttpStatus.FORBIDDEN,
                "Erro de acesso",
                "Usuário pode não ter permissão para essa ação",
                request );
    }

    // Método para retorna a resposta sem campos nulos
    private ResponseEntity<ErrorResponseDTO> buildResponseDefault(HttpStatus status, String error, String message, HttpServletRequest request) {
        ErrorResponseDTO err = new ErrorResponseDTO(
                LocalDateTime.now( ZoneOffset.UTC ),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status( status ).body( err );
    }

    private ResponseEntity<ErrorResponseDTO> buildResponseField(HttpStatus status, String error, String message, HttpServletRequest request, List<FieldMessage> errors) {
        ErrorResponseDTO err = new ErrorResponseDTO(
                LocalDateTime.now( ZoneOffset.UTC ),
                status.value(),
                error,
                message,
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status( status ).body( err );
    }
}
