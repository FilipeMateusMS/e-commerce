package com.project.api.ecommerce.service.storage;

import com.project.api.ecommerce.controller.ImagemController;
import com.project.api.ecommerce.model.Imagem;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Profile( "local" )
@Slf4j
public class LocalStorageService implements StorageService {

    @Value("${app.upload.dir}")
    private String pathString;

    private Path root;

    @PostConstruct
    public void init() {
        this.root = Paths.get(pathString);

        try {
            this.root = Paths.get(pathString);

            // Verifica se o diretório não existe e tenta criar
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                // Opcional: Log informando que o diretório foi criado
                log.warn( "Diretório criado: {}", root.getFileName() );
            }

            // Verifica se é um diretório e se tem permissão de escrita
            if (!Files.isDirectory(root)) {
                throw new RuntimeException("O caminho definido em 'app.upload.dir' não é um diretório: " + pathString);
            }

            if (!Files.isWritable(root)) {
                throw new RuntimeException("A aplicação não tem permissão de escrita no diretório: " + pathString);
            }

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretório de upload: " + pathString, e);
        }
    }

    @Override
    public String salvar( MultipartFile file ) {

        String nome = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            Files.copy( file.getInputStream(), root.resolve( nome ) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nome;
    }

    @Override
    public byte[] carregar( String storageKey ) {
        try {
            return Files.readAllBytes( root.resolve( storageKey ) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar( String storageKey ) {
        try {
            Files.deleteIfExists( root.resolve( storageKey ) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateUrlDownload(Imagem imagem) {
        return linkTo(methodOn(ImagemController.class)
                .downloadImagem(imagem.getId()))
                .toUri()
                .toString();
    }
}
