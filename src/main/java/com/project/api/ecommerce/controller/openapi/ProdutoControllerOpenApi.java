package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.request.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Produtos", description = "Gerenciamento do catálogo de produtos")
public interface ProdutoControllerOpenApi {

    @Operation(summary = "Busca um produto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<ProdutoResponseDTO> obterPorId(@Parameter(description = "ID do produto") Long id);

    @Operation(summary = "Lista produtos com filtros avançados e paginação")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    ResponseEntity<PageResponse<ProdutoResponseDTO>> findAllProdutosFiltrados(
            @ParameterObject ProdutoFilterDTO produtoSearch,
            @ParameterObject Pageable pageable);

    @Operation(summary = "Cadastra um novo produto")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    ResponseEntity<ProdutoResponseDTO> insertProduto(@RequestBody ProdutoRequestDTO produtoRequest);

    @Operation(summary = "Atualiza todos os dados de um produto")
    ResponseEntity<ProdutoResponseDTO> updateProduto(
            @Parameter(description = "ID do produto") Long id,
            @RequestBody ProdutoRequestDTO produtoRequest);

    @Operation(summary = "Exclui um produto do catálogo")
    @ApiResponse(responseCode = "204", description = "Produto removido")
    ResponseEntity<Void> deleteProduto(@Parameter(description = "ID do produto") Long id);
}