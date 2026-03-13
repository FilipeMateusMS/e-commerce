package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.request.LoginRequestDTO;
import com.project.api.ecommerce.dto.request.RegisterRequestDTO;
import com.project.api.ecommerce.dto.response.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Endpoints para registro e autenticação do cliente")
public interface AuthControllerOpenApi {

    @Operation(summary = "Realiza o login", description = "Retorna um token JWT após validar as credenciais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO );

    @Operation(summary = "Registra um novo usuário e já autentica",
            description = "Cria um usuário no banco e já retorna o token de acesso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de registro inválidos")
    })
    ResponseEntity<AuthResponseDTO> register( @RequestBody RegisterRequestDTO registerRequestDTO );

}
