package com.project.api.ecommerce.service.storage;

import com.project.api.ecommerce.model.Imagem;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    // Recebe o arquivo e retorna a nova chave
    String salvar( MultipartFile file );

    // Recebe a imagem da base de dados e retorna o arquivo
    byte[] carregar( String storageKey );

    // Deleta a imagem
    void deletar( String storageKey );

    // Gera o link para dowload
    String generateUrlDownload( Imagem imagem );
}
