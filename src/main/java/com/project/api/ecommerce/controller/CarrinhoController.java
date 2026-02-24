package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.CarrinhoControllerOpenApi;
import com.project.api.ecommerce.dto.CarrinhoResponseDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.CarrinhoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/v1/carrinhos" )
public class CarrinhoController implements CarrinhoControllerOpenApi {

    private final CarrinhoService carrinhoService;

    private static final Logger log = LoggerFactory.getLogger( CarrinhoController.class );

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<CarrinhoResponseDTO> getCarrinho( @PathVariable Long id ) {
        return ResponseEntity.ok( carrinhoService.getCarrinhoById( id ) );
    }

    @GetMapping
    public ResponseEntity<PageResponse<CarrinhoResponseDTO>> getCarrinhos(Pageable pageable ) {
        return ResponseEntity.ok( carrinhoService.getCarrinhos( pageable ) );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Void> deletarCarrinho( @PathVariable Long id ){
        carrinhoService.deletarCarrinho( id );
        return ResponseEntity.noContent().build();
    }
}