package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.CarrinhoItemControllerOpenApi;
import com.project.api.ecommerce.dto.request.CarrinhoItemRequestDTO;
import com.project.api.ecommerce.dto.response.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.dto.request.CarrinhoItemUpdateRequestDTO;
import com.project.api.ecommerce.service.CarrinhoItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/carrinhos/itens")
@RequiredArgsConstructor
public class CarrinhoItemController implements CarrinhoItemControllerOpenApi {

    private final CarrinhoItemService carrinhoItemService;

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
            @Valid @RequestBody CarrinhoItemUpdateRequestDTO carrinhoItemUpdateDTO ){
        return ResponseEntity.ok( carrinhoItemService.alterarQuantidade( id, carrinhoItemUpdateDTO ) );
    }

    @DeleteMapping( "/{id}" )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> removerItemDoCarrinho( @PathVariable Long id ){
        carrinhoItemService.removerItemDoCarrinho( id );
        return ResponseEntity.noContent().build();
    }
}
