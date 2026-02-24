package com.project.api.ecommerce.service.storage;

import com.project.api.ecommerce.model.Imagem;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("local")
public class LocalStorageService implements StorageService {

    private final Path root = Paths.get("uploads");

    @Override
    public String salvar( MultipartFile file ) {

        String nome = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            Files.copy(file.getInputStream(), root.resolve(nome));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nome;
    }

    @Override
    public byte[] carregar(String path) {
        try {
            return Files.readAllBytes( root.resolve( path ) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar( String path ) {
        try {
            Files.deleteIfExists( root.resolve( path ) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
