package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.request.CarrinhoItemRequestDTO;
import com.project.api.ecommerce.dto.request.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.dto.request.CarrinhoItemUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Carrinho", description = "Gerenciamento de itens no carrinho de compras")
@SecurityRequirement(name = "bearerAuth") // Todos os métodos dessa rota precisam de Token
public interface CarrinhoItemControllerOpenApi {

    @Operation(summary = "Adicionar ou atualizar item",
            description = "Insere um item no carrinho ou atualiza se já existir (Upsert)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item processado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Token inválido ou Role insuficiente)")
    })
    @PostMapping
    ResponseEntity<CarrinhoItemResponseDTO> upsertCarrinho(@Valid @RequestBody CarrinhoItemRequestDTO dto);

    @Operation(summary = "Alterar quantidade",
            description = "Muda a quantidade de um item específico no carrinho pelo ID")
    @PutMapping("/{id}")
    ResponseEntity<CarrinhoItemResponseDTO> alterarQuantidadeDeItens(
            @PathVariable Long id,
            @Valid @RequestBody CarrinhoItemUpdateRequestDTO carrinhoItemUpdateDTO);

    @Operation(summary = "Remover item",
            description = "Exclui permanentemente um item do carrinho")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> removerItemDoCarrinho(@PathVariable Long id);
}
