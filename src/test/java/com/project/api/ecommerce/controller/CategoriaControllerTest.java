package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.dto.request.CategoriaCreateRequestDTO;
import com.project.api.ecommerce.dto.request.CategoriaUpdateRequestDTO;
import com.project.api.ecommerce.dto.response.CategoriaResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerTest {

    private static final String URL = "/api/v1/categorias";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoriaResponseDTO categoriaPadrao;

    @BeforeEach
    void setUp() {
        categoriaPadrao = new CategoriaResponseDTO(
                1L,
                "Informática",
                new ArrayList<>() // Sem nenhum produto
        );
    }

    @Test
    @DisplayName("Deve retornar categoria por ID com sucesso")
    @WithMockUser
    void deveRetornarCategoriaPorId() throws Exception {

        when(categoriaService.findCategoriaById(1L))
                .thenReturn(categoriaPadrao);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Informática"));

        verify(categoriaService).findCategoriaById(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 quando não encontrar categoria")
    @WithMockUser
    void deveRetornar404() throws Exception {

        when(categoriaService.findCategoriaById(99L))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(get(URL + "/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar categorias paginadas")
    @WithMockUser
    void deveRetornarCategoriasPaginadas() throws Exception {

        Page<CategoriaResponseDTO> page = new PageImpl<>(List.of(categoriaPadrao));

        when(categoriaService.findAllByContendoNome(any(), any()))
                .thenReturn(PageResponse.of(page));

        mockMvc.perform(get(URL)
                        .param("nome", "Info")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Informática"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(categoriaService).findAllByContendoNome(any(), any());
    }

    @Test
    @DisplayName("Deve criar categoria com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveCriarCategoria() throws Exception {

        CategoriaCreateRequestDTO request = new CategoriaCreateRequestDTO("Periféricos", List.of( 1L, 2L, 3L ));

        when(categoriaService.addCategoria(any())).thenReturn( new CategoriaResponseDTO(2L, "Periféricos", new ArrayList<>() ) );

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nome").value("Periféricos"));

        verify(categoriaService).addCategoria(any());
    }

    @Test
    @DisplayName("Deve retornar 403 ao criar sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403AoCriar() throws Exception {

        CategoriaCreateRequestDTO request =
                new CategoriaCreateRequestDTO("Periféricos", List.of( 1L, 2L )  );

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 400 ao criar categoria inválida")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornar400AoCriarInvalido() throws Exception {

        CategoriaCreateRequestDTO request = new CategoriaCreateRequestDTO("", new ArrayList<>() );

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar categoria com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveAtualizarCategoria() throws Exception {

        CategoriaUpdateRequestDTO request = new CategoriaUpdateRequestDTO("Nova Categoria", List.of( 2L, 3L, 4L ) );

        when(categoriaService.updateCategoria(eq(1L), any())).thenReturn(categoriaPadrao);

        mockMvc.perform(put(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(categoriaService).updateCategoria(eq(1L), any());
    }

    @Test
    @DisplayName("Deve adicionar produto na categoria")
    @WithMockUser(authorities = "ADMIN")
    void deveAdicionarProdutoNaCategoria() throws Exception {

        doNothing().when(categoriaService)
                .adicionarProdutoNaCategoria(1L, 2L);

        mockMvc.perform(post(URL + "/1/produtos/2")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoriaService)
                .adicionarProdutoNaCategoria(1L, 2L);
    }

    @Test
    @DisplayName("Deve deletar categoria com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveDeletarCategoria() throws Exception {

        doNothing().when(categoriaService)
                .deleteCategoriaById(1L);

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoriaService).deleteCategoriaById(1L);
    }
}