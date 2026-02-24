package com.project.api.ecommerce.service.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("s3")
public class S3StorageService implements StorageService {

    @Override
    public String salvar(MultipartFile file) {
        throw new UnsupportedOperationException( "S3 ainda não implementado" );
    }

    @Override
    public byte[] carregar(String path) {
        throw new UnsupportedOperationException( "S3 ainda não implementado" );
    }

    @Override
    public void deletar(String path) {
        throw new UnsupportedOperationException( "S3 ainda não implementado" );
    }
}