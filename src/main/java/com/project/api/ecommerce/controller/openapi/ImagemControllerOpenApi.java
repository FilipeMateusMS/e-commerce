package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.ImagemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Imagens", description = "Endpoints para upload e gerenciamento de imagens de produtos")
public interface ImagemControllerOpenApi {

    @Operation(summary = "Realiza o upload de múltiplas imagens para um produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Upload realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou lista vazia", content = @Content)
    })
    ResponseEntity<List<ImagemResponseDTO>> uploadImagens(
            @Parameter(description = "ID do produto", example = "1") Long idProduto,
            @Parameter(description = "Arquivos de imagem (jpg, png)") MultipartFile[] files
    );

    @Operation(summary = "Realiza o download/visualização de uma imagem")
    @ApiResponse(responseCode = "200", description = "Imagem encontrada")
    ResponseEntity<Resource> downloadImagem(@Parameter(description = "ID da imagem") Long idImagem);

    @Operation(summary = "Substitui o arquivo de uma imagem existente")
    ResponseEntity<ImagemResponseDTO> alterarImagem(
            @Parameter(description = "ID da imagem") Long idImagem,
            @Parameter(description = "Novo arquivo de imagem") MultipartFile file
    );

    @Operation(summary = "Exclui uma imagem do sistema")
    @ApiResponse(responseCode = "204", description = "Imagem removida")
    ResponseEntity<Void> deletarImagem(@Parameter(description = "ID da imagem") Long idImagem);
}
