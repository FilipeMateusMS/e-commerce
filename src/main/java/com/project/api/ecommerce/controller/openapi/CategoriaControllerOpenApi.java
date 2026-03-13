package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.request.CategoriaCreateRequestDTO;
import com.project.api.ecommerce.dto.response.CategoriaResponseDTO;
import com.project.api.ecommerce.dto.request.CategoriaUpdateRequestDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Categorias", description = "Gerenciamento de categorias de produtos")
public interface CategoriaControllerOpenApi {

    @Operation(summary = "Busca categoria por ID")
    ResponseEntity<CategoriaResponseDTO> findById(@Parameter(description = "ID da categoria") Long id);

    @Operation(summary = "Lista categorias com filtro por nome e paginação")
    ResponseEntity<PageResponse<CategoriaResponseDTO>> findAllCategorias(
            @Parameter(description = "Parte do nome da categoria") String nome,
            @ParameterObject Pageable pageable);

    @Operation(summary = "Cria uma nova categoria")
    ResponseEntity<CategoriaResponseDTO> createCategoria(@RequestBody CategoriaCreateRequestDTO categoria);

    @Operation(summary = "Atualiza uma categoria existente")
    ResponseEntity<CategoriaResponseDTO> updateCategoria(
            @Parameter(description = "ID da categoria") Long id,
            @RequestBody CategoriaUpdateRequestDTO categoria);

    @Operation(summary = "Exclui uma categoria")
    @ApiResponse(responseCode = "204", description = "Categoria excluída")
    ResponseEntity<Void> deleteById(Long id);
}
