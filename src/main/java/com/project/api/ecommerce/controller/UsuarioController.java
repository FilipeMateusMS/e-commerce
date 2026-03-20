package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.UsuarioControllerOpenApi;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.service.UsuarioService;
import com.project.api.ecommerce.dto.request.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.response.UsuarioResponseDTO;
import com.project.api.ecommerce.dto.request.UsuarioUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/usuarios" )
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerOpenApi {

    private final UsuarioService usuarioService;

    @GetMapping( "/{id}" )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id ) {
        return ResponseEntity.ok( usuarioService.findUsuarioById( id ) );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UsuarioResponseDTO>> getAllUsuarios(Pageable pageable ) {
        return ResponseEntity.ok( usuarioService.findAllUsuario( pageable) );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> insertUsuario( @RequestBody UsuarioRequestDTO usuarioRequestDTO ) {
        return ResponseEntity.status( HttpStatus.CREATED ).body( usuarioService.criarUsuario( usuarioRequestDTO ) );
    }

    @PutMapping( "/{id}")
    @PreAuthorize("hasRole('USER', 'ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> alterarUsuario( @PathVariable Long id, @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO ) {
        return ResponseEntity.ok( usuarioService.alterarUsuario( usuarioUpdateRequestDTO, id ) );
    }

    @DeleteMapping( "/{id}" )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarUsuario( @PathVariable Long id ){
        usuarioService.deletarUsuario( id );
        return ResponseEntity.noContent().build();
    }
}
