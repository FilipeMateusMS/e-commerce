package com.project.api.ecommerce.service.storage;

import com.project.api.ecommerce.model.Imagem;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String salvar( MultipartFile file );

    byte[] carregar( String path );

    void deletar( String path );
}
