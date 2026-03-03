package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.controller.ImagemController;
import com.project.api.ecommerce.dto.ImagemResponseDTO;
import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public abstract class ImagemMapper {

    @Mapping(target = "downloadUrl", expression = "java(generateDownloadUrl(imagem))")
    public abstract ImagemResponseDTO toDTO(Imagem imagem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileName", source = "file.originalFilename")
    @Mapping(target = "fileType", source = "file.contentType")
    @Mapping(target = "storageKey", source = "storageKey")
    @Mapping(target = "produto", source = "produto")
    public abstract Imagem toEntity(MultipartFile file, Produto produto, String storageKey);

    protected String generateDownloadUrl(Imagem imagem) {
        return linkTo(methodOn(ImagemController.class)
                .downloadImagem(imagem.getId()))
                .toUri()
                .toString();
    }
}
