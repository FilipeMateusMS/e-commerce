package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.security.config.ShopConfigSecurity;
import com.project.api.ecommerce.service.ProdutoService;
import com.project.api.ecommerce.dto.response.CategoriaProdutoResponseDTO;
import com.project.api.ecommerce.dto.request.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import com.project.api.ecommerce.service.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    private static final String URL = "/api/v1/produtos";

    @Autowired
    private MockMvc mockMvc; // Simula requisições HTTP para testar os Controllers sem subir um servidor real

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper; // Será utilizado para converter os DTOs em Strings para o Body das requisições

    private ProdutoResponseDTO produtoPadrao;

    @BeforeEach
    void setUp() {
        produtoPadrao = new ProdutoResponseDTO(
                1L,
                "Notebook",
                "Dell",
                "Notebook Dell Inspiron 15",
                new BigDecimal("3500.00"),
                10,
                new CategoriaProdutoResponseDTO(1L, "Informática")
        );
    }

    @Test
    @DisplayName("Deve retornar produto por ID com sucesso")
    @WithMockUser
    void deveRetornarProdutoCompletoPorId() throws Exception {

        // Arrange
        when(produtoService.getProdutoById(1L)).thenReturn(produtoPadrao); // Faz o Mock do método

        // Act & Assert
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Notebook"))
                .andExpect(jsonPath("$.marca").value("Dell"))
                .andExpect(jsonPath("$.descricao").value("Notebook Dell Inspiron 15"))
                .andExpect(jsonPath("$.precoUnitario").value(3500))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.categoria.nome").value("Informática"));

        verify(produtoService).getProdutoById(1L); // Verifica se passou por esse caminho
    }

    @Test
    @DisplayName("Deve retornar 404 quando produto não existir")
    @WithMockUser
    void deveRetornar404QuandoProdutoNaoExistir() throws Exception {

        // Arrange
        when(produtoService.getProdutoById(99L)).thenThrow(new ResourceNotFoundException("Produto não encontrado"));

        // Act & Assert
        mockMvc.perform(get(URL + "/99")).andExpect(status().isNotFound());

        verify(produtoService).getProdutoById(99L);
    }

    @Test
    @DisplayName("Deve retornar produtos paginados")
    @WithMockUser
    void deveRetornarProdutosPaginados() throws Exception {

        // Arrange
        Page<ProdutoResponseDTO> page = new PageImpl<>(List.of(produtoPadrao));

        when(produtoService.getAllProdutosFiltered(
                any(ProdutoFilterDTO.class),
                any(Pageable.class))
        ).thenReturn(PageResponse.of(page));

        // Act & Assert
        mockMvc.perform(get(URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Notebook"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(produtoService).getAllProdutosFiltered(any(), any());
    }

    @Test
    @DisplayName("Deve inserir produto com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveInserirProdutoComSucesso() throws Exception {

        // Arrange
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Mouse", "Logitech", "G502",
                new BigDecimal("300.00"),
                50,
                "Periféricos"
        );

        ProdutoResponseDTO response = new ProdutoResponseDTO(
                2L,
                "Mouse",
                "Logitech",
                "G502",
                new BigDecimal("300.00"),
                50,
                new CategoriaProdutoResponseDTO(2L, "Periféricos")
        );

        when(produtoService.insertProduto(any(ProdutoRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Body
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nome").value("Mouse"));

        verify(produtoService).insertProduto(any(ProdutoRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 403 ao tentar inserir sem permissão")
    @WithMockUser(roles = "USER")
    void deveRetornar403AoInserirSemPermissao() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Mouse", "Logitech", "G502",
                new BigDecimal("300.00"),
                50,
                "Periféricos"
        );

        mockMvc.perform( post( URL )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( objectMapper.writeValueAsString(request)) )
                .andExpect( status().isForbidden() );
    }

    @Test
    @DisplayName("Deve retornar 400 quando request inválido")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornar400QuandoRequestInvalido() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "", // inválido
                "Marca",
                "Desc",
                new BigDecimal("10"),
                10,
                "Categoria"
        );

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveAtualizarProdutoComSucesso() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Teclado", "Logitech", "K120",
                new BigDecimal("150.00"),
                20,
                "Periféricos"
        );

        when(produtoService.updateProduto(eq(1L), any(ProdutoRequestDTO.class)))
                .thenReturn(produtoPadrao);

        mockMvc.perform(put(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(produtoService).updateProduto(eq(1L), any(ProdutoRequestDTO.class));
    }

    @Test
    @DisplayName("Deve deletar produto com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveDeletarProdutoComSucesso() throws Exception {

        doNothing().when(produtoService).deleteProdutoById(1L);

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(produtoService).deleteProdutoById(1L);
    }
}