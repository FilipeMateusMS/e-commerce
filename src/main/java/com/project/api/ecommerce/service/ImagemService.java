package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.ImagemResponseDTO;
import com.project.api.ecommerce.exceptions.FileStorageException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.ImagemMapper;
import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.repository.ImagemRepository;
import com.project.api.ecommerce.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImagemService {

    private final ImagemRepository imagemRepository;
    private final ProdutoService produtoService;
    private final ImagemMapper imagemMapper;

    private final StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger( ImagemService.class );

    public ImagemService(ImagemRepository imagemRepository, ProdutoService produtoService, ImagemMapper imagemMapper, StorageService storageService) {
        this.imagemRepository = imagemRepository;
        this.produtoService = produtoService;
        this.imagemMapper = imagemMapper;
        this.storageService = storageService;
    }

    public List<ImagemResponseDTO> findAllImagens()
    {
        return imagemRepository.findAll().stream().map( imagemMapper :: toDTO ).toList();
    }

    public ResponseEntity<Resource> downloadImagem( Long id ) {
        Imagem imagem = imagemRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Imagem não encontrada" ) );

        try
        {
            byte[] fileBytes = storageService.carregar( imagem.getStorageKey() );
            ByteArrayResource resource = new ByteArrayResource(fileBytes);

            MediaType mediaType;
            try {
                mediaType = MediaType.parseMediaType(imagem.getFileType());
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            String safeFilename = imagem.getFileName().replaceAll("[\\r\\n\"]", "_");

            return ResponseEntity.ok()
                    .contentType( mediaType )
                    .contentLength( fileBytes.length )
                    .header( HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename( safeFilename )
                                    .build()
                                    .toString())
                    .cacheControl( CacheControl.noCache() )
                    .body( resource );
        }
        catch ( Exception e )
        {
            throw new FileStorageException( "Erro ao ler o arquivo", e );
        }
    }

    public void deleteImagemById(Long id) {

        Imagem imagem = imagemRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Imagem não encontrada" ) );

        try {
            storageService.deletar( imagem.getStorageKey() );
            imagemRepository.delete( imagem );
        } catch ( Exception e )
        {
            throw new FileStorageException( "Erro ao ler o arquivo", e );
        }
    }

    @Transactional
    public List<ImagemResponseDTO> uploadImagens( MultipartFile[] files, Long idProduto ) {
        Produto produto = produtoService.getProdutoEntity( idProduto );
        List<ImagemResponseDTO> imagensSalvas = new ArrayList<>();
        List<String> chavesSalvas = new ArrayList<>();

        try{
            for( MultipartFile file : files ){
                String storageKey = storageService.salvar( file );
                chavesSalvas.add( storageKey );

                Imagem imagemRecebida = imagemMapper.toEntity( file, produto, storageKey );
                Imagem imagemSalva = imagemRepository.save( imagemRecebida );

                imagensSalvas.add( imagemMapper.toDTO( imagemSalva ) );
            }
        } catch ( Exception e )
        {
            chavesSalvas.forEach( storageService::deletar );

            throw new FileStorageException( "Erro ao fazer upload de imagens", e );
        }
        return imagensSalvas;
    }

    public ImagemResponseDTO updateImagem( MultipartFile file, Long id ) {

        Imagem imagem = imagemRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Imagem não encontrada" ) );

        try{
            storageService.deletar( imagem.getStorageKey() );

            String novaChave = storageService.salvar( file );

            imagem.setFileName( file.getOriginalFilename() );
            imagem.setFileType( file.getContentType() );
            imagem.setStorageKey( novaChave );

            Imagem imagemSalva = imagemRepository.save( imagem );

            return imagemMapper.toDTO( imagemSalva );
        } catch ( Exception e )
        {
            throw new FileStorageException( "Erro alterar as imagens", e );
        }
    }
}
