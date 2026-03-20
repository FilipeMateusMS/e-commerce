package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.CarrinhoControllerOpenApi;
import com.project.api.ecommerce.dto.response.CarrinhoResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/v1/carrinhos" )
@RequiredArgsConstructor
public class CarrinhoController implements CarrinhoControllerOpenApi {

    private final CarrinhoService carrinhoService;

    @GetMapping( "/{id}" )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CarrinhoResponseDTO> getCarrinho( @PathVariable Long id ) {
        return ResponseEntity.ok( carrinhoService.getCarrinhoById( id ) );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<CarrinhoResponseDTO>> getCarrinhos(Pageable pageable ) {
        return ResponseEntity.ok( carrinhoService.getCarrinhos( pageable ) );
    }

    @DeleteMapping( "/{id}" )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN' )")
    public ResponseEntity<Void> deletarCarrinho( @PathVariable Long id ){
        carrinhoService.deletarCarrinho( id );
        return ResponseEntity.noContent().build();
    }
}