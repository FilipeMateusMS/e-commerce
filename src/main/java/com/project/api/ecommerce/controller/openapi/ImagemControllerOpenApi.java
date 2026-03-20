package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.request.ImagemUploadRequestDTO;
import com.project.api.ecommerce.dto.response.ImagemResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Imagens", description = "Gerenciamento de fotos dos produtos")
public interface ImagemControllerOpenApi {

    @Operation(summary = "Listar imagens de um produto", description = "Acesso público")
    @GetMapping("/produtos/{idProduto}")
    ResponseEntity<PageResponse<ImagemResponseDTO>> obterImagenDoProduto(@PathVariable Long idProduto, Pageable pageable);

    @Operation(summary = "Listar todas as imagens", description = "Acesso público")
    @GetMapping
    ResponseEntity<PageResponse<ImagemResponseDTO>> obterImagens(Pageable pageable);

    @Operation(summary = "Download/Visualizar imagem", description = "Retorna o arquivo binário da imagem")
    @GetMapping("/{idImagem}")
    ResponseEntity<Resource> downloadImagem(@PathVariable Long idImagem);

    @Operation(summary = "Upload de nova imagem", description = "Restrito: ADMIN. Envie o arquivo no campo 'file'")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/produtos/{idProduto}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ImagemResponseDTO> uploadImagem(
            @PathVariable Long idProduto,
            @Valid @ModelAttribute("request") ImagemUploadRequestDTO imagemUploadRequestDTO);

    @Operation(summary = "Substituir imagem existente", description = "Restrito: ADMIN")
    @SecurityRequirement(name = "bearerAuth" )
    @PutMapping(value = "/{idImagem}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ImagemResponseDTO> alterarImagem(
            @PathVariable Long idImagem,
            @Valid @ModelAttribute("request") ImagemUploadRequestDTO imagemUploadRequestDTO);

    @Operation(summary = "Deletar imagem", description = "Restrito: ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{idImagem}")
    ResponseEntity<Void> deletarImagem(@PathVariable Long idImagem);
}