package com.project.api.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Getter
@Setter
public class ImagemUploadRequestDTO {

        @NotBlank(message = "Deve ser fornecido a descrição da imagem")
        private String descricao;

        @NotNull(message = "Deve ser fornecido a imagem")
        private MultipartFile file;
}
