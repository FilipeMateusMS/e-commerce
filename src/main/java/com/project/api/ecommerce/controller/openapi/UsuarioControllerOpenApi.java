package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.UsuarioResponseDTO;
import com.project.api.ecommerce.dto.UsuarioUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Usuários", description = "Gerenciamento de usuários e perfis")
public interface UsuarioControllerOpenApi {

    @Operation(summary = "Busca um usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado", content = @Content)
    })
    ResponseEntity<UsuarioResponseDTO> getUsuarioById(@Parameter(description = "ID do usuário") Long id);

    @Operation(summary = "Cadastra um novo usuário no sistema")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    ResponseEntity<UsuarioResponseDTO> insertUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO);

    @Operation(summary = "Atualiza dados de um usuário existente")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado")
    ResponseEntity<UsuarioResponseDTO> alterarUsuario(
            @Parameter(description = "ID do usuário") Long id,
            @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO);

    @Operation(summary = "Remove um usuário do sistema")
    @ApiResponse(responseCode = "204", description = "Usuário deletado")
    ResponseEntity<Void> deletarUsuario(@Parameter(description = "ID do usuário") Long id);
}
