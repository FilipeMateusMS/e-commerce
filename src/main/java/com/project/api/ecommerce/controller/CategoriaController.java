package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.CategoriaControllerOpenApi;
import com.project.api.ecommerce.dto.CategoriaCreateRequestDTO;
import com.project.api.ecommerce.dto.CategoriaResponseDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.CategoriaService;
import com.project.api.ecommerce.dto.CategoriaUpdateRequestDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/categorias")
public class CategoriaController implements CategoriaControllerOpenApi {

    private final CategoriaService categoriaService;

    private static final Logger logger = LoggerFactory.getLogger( CategoriaController.class );

    public CategoriaController( CategoriaService categoriaService ) {
        this.categoriaService = categoriaService;
    }

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
    public ResponseEntity<CategoriaResponseDTO> createCategoria(
            @Valid @RequestBody CategoriaCreateRequestDTO categoria ) {
        return ResponseEntity.status( HttpStatus.CREATED ).body( categoriaService.addCategoria( categoria ) );
    }

    @PutMapping( "/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaUpdateRequestDTO categoria ) {
        return ResponseEntity.ok( categoriaService.updateCategoria( id, categoria ) );
    }

    @PostMapping( "/{categoriaId}/produtos/{produtoId}" )
    public ResponseEntity<Void> addProdutoNaCategoria(
            @PathVariable Long categoriaId,
            @PathVariable Long produtoId ) {
        categoriaService.adicionarProdutoNaCategoria( categoriaId, produtoId );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping( "/{id}")
    public ResponseEntity<Void> deleteById( @PathVariable Long id ){
        categoriaService.deleteCategoriaById( id );
        return ResponseEntity.noContent().build();
    }
}