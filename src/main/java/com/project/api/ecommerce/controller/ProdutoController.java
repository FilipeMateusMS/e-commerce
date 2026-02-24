package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.ProdutoControllerOpenApi;
import com.project.api.ecommerce.dto.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.ProdutoResponseDTO;
import com.project.api.ecommerce.dto.ProdutoSearchDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/produtos" )
public class ProdutoController implements ProdutoControllerOpenApi {

    private final ProdutoService produtoService;

    private static final Logger logger = LoggerFactory.getLogger( ProdutoController.class );

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping( "/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterPorId( @PathVariable Long id ){
        return ResponseEntity.ok( produtoService.getProdutoById( id ) );
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProdutoResponseDTO>> findAllProdutosFiltrados(
            @ModelAttribute ProdutoSearchDTO produtoSearch,
            Pageable pageable ){
        return ResponseEntity.ok( produtoService.getAllProdutosFiltered( produtoSearch, pageable ) );
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> insertProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequest ){
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( produtoService.insertProduto( produtoRequest ) );
    }

    @PutMapping( "/{id}")
    public ResponseEntity<ProdutoResponseDTO> updateProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO produtoRequest ){
        return ResponseEntity.ok( produtoService.updateProduto( id, produtoRequest ) );
    }

    @DeleteMapping( "/{id}")
    public ResponseEntity<Void> deleteProduto( @PathVariable Long id ){
        produtoService.deleteProdutoById( id );
        return ResponseEntity.noContent().build();
    }
}