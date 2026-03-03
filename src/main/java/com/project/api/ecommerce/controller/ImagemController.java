package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.ImagemControllerOpenApi;
import com.project.api.ecommerce.dto.ImagemResponseDTO;
import com.project.api.ecommerce.service.ImagemService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping( "api/v1/imagens" )
public class ImagemController implements ImagemControllerOpenApi {

    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping("/produtos/{idProduto}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImagemResponseDTO>> uploadImagens(
            @PathVariable @Positive( message = "Deve informar um produto" ) Long idProduto,
            @RequestParam("files") @NotEmpty( message = "Deve ser fornecido os arquivos" ) MultipartFile[] files )
    {
        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( imagemService.uploadImagens( files, idProduto ) );
    }

    @GetMapping("{idImagem}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadImagem( @PathVariable Long idImagem ) {
        return imagemService.downloadImagem( idImagem );
    }

    @PutMapping("{idImagem}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImagemResponseDTO> alterarImagem(
            @PathVariable Long idImagem,
            @RequestParam("file") MultipartFile file ) {
         return ResponseEntity.ok( imagemService.updateImagem( file, idImagem ) );
    }

    @DeleteMapping( "{idImagem}" )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarImagem( @PathVariable Long idImagem ) {
        imagemService.deleteImagemById( idImagem );
        return ResponseEntity.noContent().build();
    }
}