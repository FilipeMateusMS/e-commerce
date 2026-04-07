package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.controller.ImagemController;
import com.project.api.ecommerce.dto.request.ImagemUploadRequestDTO;
import com.project.api.ecommerce.dto.response.ImagemResponseDTO;
import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public abstract class ImagemMapper {

    @Autowired
    protected StorageService storageService;

    // O Mapping chama o serviço
    @Mapping(target = "downloadUrl", expression = "java(storageService.generateUrlDownload(imagem))")
    public abstract ImagemResponseDTO toDTO(Imagem imagem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nome", source = "upload.file.originalFilename")
    @Mapping(target = "fileType", source = "upload.file.contentType")
    @Mapping(target = "storageKey", source = "storageKey")
    @Mapping(target = "produto", source = "produto")
    @Mapping( target= "descricao", source = "upload.descricao" )
    public abstract Imagem toEntity(ImagemUploadRequestDTO upload, Produto produto, String storageKey);
}
