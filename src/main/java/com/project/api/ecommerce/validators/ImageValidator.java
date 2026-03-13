package com.project.api.ecommerce.validators;

import com.project.api.ecommerce.exceptions.BusinessAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ImageValidator {

    @Value("${app.images.allowed-types}")
    private List<String> allowedTypes;

    public void validar(MultipartFile file) {
        if (file == null ) {
            throw new BusinessAlertException("Arquivo é obrigatório.");
        }
        if (file.isEmpty() ) {
            throw new BusinessAlertException("Arquivo não pode ser vazio");
        }
        if ( !allowedTypes.contains( file.getContentType() ) ) {
            throw new BusinessAlertException("Tipo de arquivo não permitido.");
        }
    }
}
