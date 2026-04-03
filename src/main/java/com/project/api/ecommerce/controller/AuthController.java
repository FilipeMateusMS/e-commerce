package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.AuthControllerOpenApi;
import com.project.api.ecommerce.dto.request.LoginRequestDTO;
import com.project.api.ecommerce.dto.request.RegisterRequestDTO;
import com.project.api.ecommerce.dto.response.AuthResponseDTO;
import com.project.api.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "api/v1/auth" )
@RequiredArgsConstructor
public class AuthController implements AuthControllerOpenApi {

    private final AuthService authService;

    @PostMapping( "/login")
    public ResponseEntity<AuthResponseDTO> login( @Valid @RequestBody LoginRequestDTO loginDTO ){
        return ResponseEntity.ok( authService.login( loginDTO ) );
    }

    @PostMapping( "/register" )
    public ResponseEntity<AuthResponseDTO> register( @Valid @RequestBody RegisterRequestDTO registerRequestDTO ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( authService.register( registerRequestDTO ) );
    }
}
