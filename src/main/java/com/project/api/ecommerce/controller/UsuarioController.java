package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.UsuarioControllerOpenApi;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.UsuarioService;
import com.project.api.ecommerce.dto.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.UsuarioResponseDTO;
import com.project.api.ecommerce.dto.UsuarioUpdateRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/usuarios" )
public class UsuarioController implements UsuarioControllerOpenApi {

    private final UsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger( UsuarioController.class );

    public UsuarioController( UsuarioService usuarioService, PasswordEncoder passwordEncoder ) {
        this.usuarioService = usuarioService;
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id ) {
        return ResponseEntity.ok( usuarioService.findUsuarioById( id ) );
    }

    @GetMapping
    public ResponseEntity<PageResponse<UsuarioResponseDTO>> getAllUsuarios(Pageable pageable ) {
        return ResponseEntity.ok( usuarioService.findAllUsuario( pageable) );
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> insertUsuario( @RequestBody UsuarioRequestDTO usuarioRequestDTO ) {
        return ResponseEntity.status( HttpStatus.CREATED ).body( usuarioService.criarUsuario( usuarioRequestDTO ) );
    }

    @PutMapping( "/{id}")
    public ResponseEntity<UsuarioResponseDTO> alterarUsuario( @PathVariable Long id, @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO ) {
        return ResponseEntity.ok( usuarioService.alterarUsuario( usuarioUpdateRequestDTO, id ) );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Void> deletarUsuario( @PathVariable Long id ){
        usuarioService.deletarUsuario( id );
        return ResponseEntity.noContent().build();
    }
}
