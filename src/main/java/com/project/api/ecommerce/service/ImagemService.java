package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.ImagemUploadRequestDTO;
import com.project.api.ecommerce.dto.response.ImagemResponseDTO;
import com.project.api.ecommerce.exceptions.FileStorageException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.ImagemMapper;
import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.repository.ImagemRepository;
import com.project.api.ecommerce.service.storage.StorageService;
import com.project.api.ecommerce.validators.ImageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImagemService {

    private final ImagemRepository imagemRepository;
    private final ProdutoService produtoService;
    private final ImagemMapper imagemMapper;
    private final ImageValidator imageValidator;

    private final StorageService storageService;

    public PageResponse<ImagemResponseDTO> obterImagensDoProduto(Long idProduto, Pageable pageable )
    {
         Page<ImagemResponseDTO> pages = imagemRepository.findAllByProdutoId( idProduto, pageable )
                .map( imagemMapper :: toDTO );
         return PageResponse.of( pages );
    }

    public PageResponse<ImagemResponseDTO> obterImagens( Pageable pageable )
    {
        Page<ImagemResponseDTO> pages = imagemRepository.findAll( pageable )
                .map( imagemMapper :: toDTO );
        return PageResponse.of( pages );
    }

    public ResponseEntity<Resource> downloadImagem( Long id ) {
        Imagem imagem = imagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Imagem não encontrada" ) );

        try
        {
            byte[] fileBytes = storageService.carregar( imagem.getStorageKey() );
            ByteArrayResource resource = new ByteArrayResource( fileBytes );

            MediaType mediaType;
            try {
                mediaType = MediaType.parseMediaType( imagem.getFileType() );
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .contentType( mediaType )
                    .contentLength( fileBytes.length )
                    .header( HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename( imagem.getNome() )
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

    public void deleteImagemById( Long id ) {
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
    public ImagemResponseDTO uploadImagem(Long idProduto, ImagemUploadRequestDTO imagemUploadRequestDTO ) {
        Produto produto = produtoService.getProdutoEntity( idProduto );
        MultipartFile file = imagemUploadRequestDTO.getFile();

        imageValidator.validar( file );

        try{
            String storageKey = storageService.salvar( file );
            Imagem imagemRecebida = imagemMapper.toEntity( imagemUploadRequestDTO, produto, storageKey );
            Imagem imagemSalva = imagemRepository.save( imagemRecebida );
            return imagemMapper.toDTO( imagemSalva );
        } catch ( Exception e )
        {
            throw new FileStorageException( "Erro ao fazer upload de imagens", e );
        }
    }

    public ImagemResponseDTO updateImagem(Long idImagem, ImagemUploadRequestDTO imagemUploadRequestDTO ) {

        imageValidator.validar( imagemUploadRequestDTO.getFile() );
        Imagem imagem = imagemRepository.findById( idImagem )
                .orElseThrow( () -> new ResourceNotFoundException( "Imagem não encontrada" ) );

        try{
            storageService.deletar( imagem.getStorageKey() );

            String novaChave = storageService.salvar( imagemUploadRequestDTO.getFile() );

            imagem.setStorageKey( novaChave );
            imagem.setDescricao( imagemUploadRequestDTO.getDescricao() );
            Imagem imagemSalva = imagemRepository.save( imagem );
            return imagemMapper.toDTO( imagemSalva );
        } catch ( Exception e )
        {
            throw new FileStorageException( "Erro alterar as imagens", e );
        }
    }
}
