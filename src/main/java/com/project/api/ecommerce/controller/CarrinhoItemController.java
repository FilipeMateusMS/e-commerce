package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.dto.CarrinhoItemRequestDTO;
import com.project.api.ecommerce.dto.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.dto.CarrinhoItemUpdateDTO;
import com.project.api.ecommerce.service.CarrinhoItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/carrinhos/itens")
public class CarrinhoItemController {

    private final CarrinhoItemService carrinhoItemService;

    private static final Logger logger = LoggerFactory.getLogger( CarrinhoItemController.class );

    public CarrinhoItemController(CarrinhoItemService carrinhoItemService) {
        this.carrinhoItemService = carrinhoItemService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CarrinhoItemResponseDTO> upsertCarrinho(
            @Valid @RequestBody CarrinhoItemRequestDTO dto ){
        return ResponseEntity.ok( carrinhoItemService.upsertItemNoCarrinho( dto ) );
    }

    @PutMapping( "/{id}" )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CarrinhoItemResponseDTO> alterarQuantidadeDeItens(
            @PathVariable Long id,
            @Valid @RequestBody CarrinhoItemUpdateDTO carrinhoItemUpdateDTO ){
        return ResponseEntity.ok( carrinhoItemService.alterarQuantidade( id, carrinhoItemUpdateDTO ) );
    }

    @DeleteMapping( "/{id}" )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> removerItemDoCarrinho( @PathVariable Long id ){
        carrinhoItemService.removerItemDoCarrinho( id );
        return ResponseEntity.noContent().build();
    }
}
