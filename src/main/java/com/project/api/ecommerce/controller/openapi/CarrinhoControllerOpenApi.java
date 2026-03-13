package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.response.CarrinhoResponseDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Carrinhos", description = "Gerenciamento de carrinhos de compras temporários")
public interface CarrinhoControllerOpenApi {

    @Operation(summary = "Busca um carrinho pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrinho encontrado"),
            @ApiResponse(responseCode = "404", description = "Carrinho não localizado", content = @Content)
    })
    ResponseEntity<CarrinhoResponseDTO> getCarrinho(@Parameter(description = "ID do carrinho") Long id);

    @Operation(summary = "Lista todos os carrinhos com paginação")
    @ApiResponse(responseCode = "200", description = "Lista recuperada")
    ResponseEntity<PageResponse<CarrinhoResponseDTO>> getCarrinhos(@ParameterObject Pageable pageable);

    @Operation(summary = "Exclui um carrinho", description = "Remove permanentemente o carrinho e seus itens.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrinho excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado para exclusão", content = @Content)
    })
    ResponseEntity<Void> deletarCarrinho(@Parameter(description = "ID do carrinho") Long id);
}