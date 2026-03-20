package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.CategoriaControllerOpenApi;
import com.project.api.ecommerce.dto.request.CategoriaCreateRequestDTO;
import com.project.api.ecommerce.dto.response.CategoriaResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.service.CategoriaService;
import com.project.api.ecommerce.dto.request.CategoriaUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController implements CategoriaControllerOpenApi {

    private final CategoriaService categoriaService;

    @GetMapping( "/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById( @PathVariable Long id ) {
        return ResponseEntity.ok( categoriaService.findCategoriaById( id ) );
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoriaResponseDTO>> findAllCategorias(
            @RequestParam( required = false ) String nome,
            @PageableDefault( sort = "nome" ) Pageable pageable ){
        return ResponseEntity.ok( categoriaService.findAllByContendoNome( nome, pageable ) );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> createCategoria(
            @Valid @RequestBody CategoriaCreateRequestDTO categoria ) {
        return ResponseEntity.status( HttpStatus.CREATED ).body( categoriaService.addCategoria( categoria ) );
    }

    @PutMapping( "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaUpdateRequestDTO categoria ) {
        return ResponseEntity.ok( categoriaService.updateCategoria( id, categoria ) );
    }

    @PostMapping( "/{categoriaId}/produtos/{produtoId}" )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addProdutoNaCategoria(
            @PathVariable Long categoriaId,
            @PathVariable Long produtoId ) {
        categoriaService.adicionarProdutoNaCategoria( categoriaId, produtoId );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping( "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById( @PathVariable Long id ){
        categoriaService.deleteCategoriaById( id );
        return ResponseEntity.noContent().build();
    }
}