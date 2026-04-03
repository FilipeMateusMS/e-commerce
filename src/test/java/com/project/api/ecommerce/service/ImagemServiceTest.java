package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.ImagemUploadRequestDTO;
import com.project.api.ecommerce.dto.response.ImagemResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.mappers.ImagemMapper;
import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.repository.ImagemRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.service.storage.StorageService;
import com.project.api.ecommerce.support.DataDtoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImagemServiceTest {

    @Mock
    private ImagemRepository imagemRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ImagemMapper imagemMapper;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ImagemService service;

    @BeforeEach
    void setup() {
        service.allowedTypes = List.of("image/png", "image/jpeg");
    }

    @Test
    @DisplayName("Deve listar todas as imagens associadas a um produto específico de forma paginada")
    void deveListarImagensDoProdutoComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Imagem imagem = new Imagem();
        ImagemResponseDTO dto = DataDtoFactory.imagemResponsePadrao();
        Page<Imagem> page = new PageImpl<>(List.of(imagem));

        when(imagemRepository.findAllByProdutoId(1L, pageable)).thenReturn(page);
        when(imagemMapper.toDTO(imagem)).thenReturn(dto);

        PageResponse<ImagemResponseDTO> response = service.obterImagensDoProduto(1L, pageable);

        assertThat(response.content() ).hasSize(1);
    }

    @Test
    @DisplayName("Deve listar todas as imagens cadastradas no sistema com paginação")
    void deveListarImagensComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Imagem imagem = new Imagem();
        ImagemResponseDTO dto = DataDtoFactory.imagemResponsePadrao();
        Page<Imagem> page = new PageImpl<>(List.of(imagem));

        when(imagemRepository.findAll(pageable)).thenReturn(page);
        when(imagemMapper.toDTO(imagem)).thenReturn(dto);

        PageResponse<ImagemResponseDTO> response = service.obterImagens(pageable);

        assertThat(response.content() ).hasSize(1);
    }

    @Test
    @DisplayName("Deve realizar o download do arquivo de imagem e retornar um ResponseEntity com o recurso")
    void deveFazerDownloadImagemComSucesso() {
        Imagem imagem = new Imagem();
        imagem.setStorageKey("key");
        imagem.setFileType("image/png");
        imagem.setNome("arquivo.png");

        byte[] bytes = "teste".getBytes();

        when(imagemRepository.findById(1L)).thenReturn(Optional.of(imagem));
        when(storageService.carregar("key")).thenReturn(bytes);

        ResponseEntity<Resource> response = service.downloadImagem(1L);

        assertThat(response.getStatusCode().is2xxSuccessful() ).isTrue(); // Vei
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Deve remover o registro da imagem do banco de dados e excluir o arquivo físico no storage")
    void deveDeletarImagemComSucesso() {
        Imagem imagem = new Imagem();
        imagem.setStorageKey("key");

        when(imagemRepository.findById(1L)).thenReturn(Optional.of(imagem));

        service.deleteImagemById(1L);

        verify(storageService).deletar("key");
        verify(imagemRepository).delete(imagem);
    }

    @Test
    @DisplayName("Deve realizar o upload de uma nova imagem, salvar no storage e vincular ao produto")
    void deveFazerUploadImagemComSucesso() {
        MultipartFile file = mock(MultipartFile.class);

        ImagemUploadRequestDTO request = new ImagemUploadRequestDTO( "Descrição do arquivo", file );

        Produto produto = new Produto();
        produto.setId(1L);

        Imagem imagem = new Imagem();
        Imagem imagemSalva = new Imagem();
        ImagemResponseDTO dto = DataDtoFactory.imagemResponsePadrao();

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(storageService.salvar(file)).thenReturn("key");

        when(imagemMapper.toEntity(request, produto, "key")).thenReturn(imagem);
        when(imagemRepository.save(imagem)).thenReturn(imagemSalva);
        when(imagemMapper.toDTO(imagemSalva)).thenReturn(dto);

        ImagemResponseDTO response = service.uploadImagem(1L, request);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve atualizar uma imagem existente, substituindo o arquivo no storage e removendo o antigo")
    void deveAtualizarImagemComSucesso() {
        MultipartFile file = mock(MultipartFile.class);

        ImagemUploadRequestDTO request = new ImagemUploadRequestDTO( "Nova descrição", file );

        Imagem imagem = new Imagem();
        imagem.setStorageKey("old-key");

        Imagem imagemSalva = new Imagem();
        ImagemResponseDTO dto = DataDtoFactory.imagemResponsePadrao();

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");

        when(imagemRepository.findById(1L)).thenReturn(Optional.of(imagem));
        when(storageService.salvar(file)).thenReturn("new-key");

        when(imagemRepository.save(imagem)).thenReturn(imagemSalva);
        when(imagemMapper.toDTO(imagemSalva)).thenReturn(dto);

        ImagemResponseDTO response = service.updateImagem(1L, request);

        assertThat(response).isEqualTo(dto);
        verify(storageService).deletar("old-key");
    }
}
