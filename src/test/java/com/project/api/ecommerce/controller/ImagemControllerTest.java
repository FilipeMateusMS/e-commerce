package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.dto.response.ImagemResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.security.jwt.JwtAccessDeniedHandler;
import com.project.api.ecommerce.security.jwt.JwtAuthEntryPoint;
import com.project.api.ecommerce.security.jwt.JwtUtils;
import com.project.api.ecommerce.security.user.UsuarioDetailsService;
import com.project.api.ecommerce.service.ImagemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ImagemControllerTest {

    private static final String URL = "/api/v1/imagens";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImagemService imagemService;

    private ImagemResponseDTO imagemPadrao;

    @BeforeEach
    void setUp() {
        imagemPadrao = new ImagemResponseDTO(
                1L,
                "imagem.jpg",
                "http://localhost/imagens/1"
        );
    }

    @Test
    @DisplayName("Deve retornar imagens do produto")
    @WithMockUser
    void deveRetornarImagensDoProduto() throws Exception {

        Page<ImagemResponseDTO> page =
                new PageImpl<>(List.of(imagemPadrao));

        when(imagemService.obterImagensDoProduto(eq(1L), any()))
                .thenReturn(PageResponse.of(page));

        mockMvc.perform(get(URL + "/produtos/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(imagemService).obterImagensDoProduto(eq(1L), any());
    }

    @Test
    @DisplayName("Deve retornar todas imagens paginadas")
    @WithMockUser
    void deveRetornarTodasImagens() throws Exception {

        Page<ImagemResponseDTO> page =
                new PageImpl<>(List.of(imagemPadrao));

        when(imagemService.obterImagens(any()))
                .thenReturn(PageResponse.of(page));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(imagemService).obterImagens(any());
    }

    @Test
    @DisplayName("Deve fazer download da imagem")
    @WithMockUser
    void deveFazerDownloadImagem() throws Exception {

        ByteArrayResource resource = new ByteArrayResource("fake-image".getBytes());

        when(imagemService.downloadImagem(1L))
                .thenReturn(
                        ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource));

        mockMvc.perform(get(URL + "/1")).andExpect(status().isOk());

        verify(imagemService).downloadImagem(1L);
    }

    @Test
    @DisplayName("Deve fazer upload de imagem com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveUploadImagem() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagem.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );

        when(imagemService.uploadImagem(eq(1L), any()))
                .thenReturn(imagemPadrao);

        mockMvc.perform(multipart(URL + "/produtos/1")
                        .file(file)
                        .param("descricao", "Imagem do produto")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(imagemService).uploadImagem(eq(1L), any());
    }

    @Test
    @DisplayName("Deve retornar 403 ao fazer upload sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403Upload() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagem.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );

        mockMvc.perform(multipart(URL + "/produtos/1")
                        .file(file)
                        .param("descricao", "Imagem do produto")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve atualizar imagem com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveAtualizarImagem() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nova.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );

        when(imagemService.updateImagem(eq(1L), any()))
                .thenReturn(imagemPadrao);

        mockMvc.perform(multipart(URL + "/1")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .param("descricao", "Imagem do produto")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(imagemService).updateImagem(eq(1L), any());
    }

    @Test
    @DisplayName("Deve deletar imagem com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveDeletarImagem() throws Exception {

        doNothing().when(imagemService).deleteImagemById(1L);

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(imagemService).deleteImagemById(1L);
    }

    @Test
    @DisplayName("Deve retornar 403 ao deletar sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403Delete() throws Exception {

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
