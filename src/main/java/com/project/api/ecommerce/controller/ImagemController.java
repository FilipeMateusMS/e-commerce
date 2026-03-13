package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.ImagemControllerOpenApi;
import com.project.api.ecommerce.dto.request.ImagemUploadRequestDTO;
import com.project.api.ecommerce.dto.response.ImagemResponseDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.ImagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/imagens" )
@RequiredArgsConstructor
public class ImagemController implements ImagemControllerOpenApi {

    private final ImagemService imagemService;

    @GetMapping( "/produtos/{idProduto}" )
    public ResponseEntity<PageResponse<ImagemResponseDTO>> obterImagenDoProduto(@PathVariable Long idProduto, Pageable pageable ) {
        return ResponseEntity.ok( imagemService.obterImagensDoProduto( idProduto, pageable ) );
    }

    @GetMapping
    public ResponseEntity<PageResponse<ImagemResponseDTO>> obterImagens( Pageable pageable ) {
        return ResponseEntity.ok( imagemService.obterImagens( pageable ) );
    }

    @GetMapping("{idImagem}")
    public ResponseEntity<Resource> downloadImagem( @PathVariable Long idImagem ) {
        return imagemService.downloadImagem( idImagem );
    }

    @PostMapping("/produtos/{idProduto}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImagemResponseDTO> uploadImagem(
            @PathVariable Long idProduto,
            @Valid @ModelAttribute( "request" ) ImagemUploadRequestDTO imagemUploadRequestDTO )
    {
        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( imagemService.uploadImagem( idProduto, imagemUploadRequestDTO ) );
    }

    @PutMapping("{idImagem}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImagemResponseDTO> alterarImagem(
            @PathVariable Long idImagem,
            @Valid @ModelAttribute( "request" ) ImagemUploadRequestDTO imagemUploadRequestDTO ) {
         return ResponseEntity.ok( imagemService.updateImagem( idImagem, imagemUploadRequestDTO ) );
    }

    @DeleteMapping( "{idImagem}" )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarImagem( @PathVariable Long idImagem ) {
        imagemService.deleteImagemById( idImagem );
        return ResponseEntity.noContent().build();
    }
}