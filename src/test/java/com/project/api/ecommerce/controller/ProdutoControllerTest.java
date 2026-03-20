package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.commom.pagination.PageResponse;
import com.project.api.ecommerce.service.ProdutoService;
import com.project.api.ecommerce.dto.response.CategoriaProdutoResponseDTO;
import com.project.api.ecommerce.dto.request.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest( ProdutoController.class )
@WithMockUser
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService; // mock do service

    @Value("${api.v1.prefix:/api/v1}")
    private String preffixAPI;

    @Autowired
    private ObjectMapper objectMapper;

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
    void deveRetornarProdutoCompletoPorId() throws Exception {

        when( produtoService.getProdutoById( 1L ) ).thenReturn( produtoPadrao );

        mockMvc.perform( get(preffixAPI + "/produtos/1" ) )
                .andExpect( status().isOk() )
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Notebook"))
                .andExpect(jsonPath("$.marca").value("Dell"))
                .andExpect(jsonPath("$.descricao").value("Notebook Dell Inspiron 15"))
                .andExpect(jsonPath("$.precoUnitario").value(3500.00))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.categoria.nome").value("Informática"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o produto não existir")
    void deveRetornar404QuandoProdutoNaoExistir() throws Exception {
        Long idInexistente = 99L;

        when(produtoService.getProdutoById( idInexistente ) )
                .thenThrow(new ResourceNotFoundException("Produto não encontrado"));

        // 3. Execução e Verificação
        mockMvc.perform( get(preffixAPI + "/produtos/" + idInexistente)
                        .contentType(MediaType.APPLICATION_JSON ) )
                .andExpect(status().isNotFound()); // Verifica se retornou 404
    }

    @Test
    void deveRetornarProdutosPaginados() throws Exception {
        Page<ProdutoResponseDTO> page = new PageImpl<>(List.of( produtoPadrao ) );

        when( produtoService.getAllProdutosFiltered( any( ProdutoFilterDTO.class ),
                any( Pageable.class ) ) )
                .thenReturn(PageResponse.of( page ) );

        mockMvc.perform( get(preffixAPI + "/produtos" )
                        .param("page", "0")
                        .param("size", "10" ) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Notebook"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveInserirProdutoComSucesso() throws Exception {
        // Arrange
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mouse", "Logitech", "G502", new BigDecimal("300.00"), 50, "Periféricos");
        ProdutoResponseDTO response = new ProdutoResponseDTO(2L,
                "Mouse",
                "Logitech",
                "G502",
                new BigDecimal("300.00"),
                50,
                new CategoriaProdutoResponseDTO( 2L, "Periféricos" ) );

        when(produtoService.insertProduto(any(ProdutoRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post(preffixAPI + "/produtos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( objectMapper.writeValueAsString( request ) ) ) // ObjectMapper converte objeto para JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nome").value("Mouse"));
    }
}
