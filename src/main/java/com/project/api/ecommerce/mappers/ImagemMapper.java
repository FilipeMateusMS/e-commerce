package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.controller.ImagemController;
import com.project.api.ecommerce.dto.ImagemResponseDTO;
import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImagemMapper {

    public ImagemResponseDTO toDTO( Imagem imagem ) {
        String dowloadImagemUrl = linkTo(methodOn( ImagemController.class )
                .downloadImagem( imagem.getId() ) )
                .toUri()
                .toString();
        return new ImagemResponseDTO( imagem.getId(), imagem.getFileName(), dowloadImagemUrl );
    }

    public Imagem toEntity( MultipartFile file, Produto produto, String storageKey ) {
        return new Imagem( file.getOriginalFilename(), file.getContentType(), storageKey, produto );
    }
}
