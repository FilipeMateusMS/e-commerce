package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.dto.request.CarrinhoItemRequestDTO;
import com.project.api.ecommerce.dto.response.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.dto.request.CarrinhoItemUpdateRequestDTO;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.service.CarrinhoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CarrinhoItemControllerTest {

    private static final String URL = "/api/v1/carrinhos/itens";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarrinhoItemService carrinhoItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private CarrinhoItemResponseDTO responsePadrao;

    @BeforeEach
    void setUp() {
        ProdutoResponseDTO produto = new ProdutoResponseDTO(
                2L,
                "Nome do produto",
                "Marca",
                "Descrição do produto",
                BigDecimal.valueOf( 20.00 ),
                2,
                null
        );

        responsePadrao = new CarrinhoItemResponseDTO(
                1L, // id item
                produto,
                2,
                new BigDecimal("300.00")
        );
    }

    @Test
    @DisplayName("Deve inserir/atualizar item no carrinho com sucesso (USER)")
    @WithMockUser(authorities = "USER")
    void deveUpsertItemComSucesso() throws Exception {

        CarrinhoItemRequestDTO request = new CarrinhoItemRequestDTO( 1L, 2 );

        when(carrinhoItemService.upsertItemNoCarrinho(any(CarrinhoItemRequestDTO.class)))
                .thenReturn(responsePadrao);

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantidade").value(2))
                .andExpect(jsonPath("$.precoTotalItem").value(300.00));

        verify(carrinhoItemService).upsertItemNoCarrinho(any(CarrinhoItemRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 403 ao inserir sem permissão")
    @WithMockUser(authorities = "GUEST")
    void deveRetornar403AoUpsertSemPermissao() throws Exception {

        CarrinhoItemRequestDTO request = new CarrinhoItemRequestDTO(1L, 2);

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 400 quando request inválido")
    @WithMockUser(authorities = "USER")
    void deveRetornar400QuandoRequestInvalido() throws Exception {

        CarrinhoItemRequestDTO request = new CarrinhoItemRequestDTO( null, 0 );

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar quantidade com sucesso")
    @WithMockUser(authorities = "USER")
    void deveAtualizarQuantidade() throws Exception {

        CarrinhoItemUpdateRequestDTO request = new CarrinhoItemUpdateRequestDTO(5);

        when(carrinhoItemService.alterarQuantidade(eq(1L), any(CarrinhoItemUpdateRequestDTO.class)))
                .thenReturn(responsePadrao);

        mockMvc.perform(put(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(carrinhoItemService).alterarQuantidade(eq(1L), any());
    }

    @Test
    @DisplayName("Deve remover item com sucesso")
    @WithMockUser(authorities = "USER")
    void deveRemoverItem() throws Exception {

        doNothing().when(carrinhoItemService).removerItemDoCarrinho(1L);

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(carrinhoItemService).removerItemDoCarrinho(1L);
    }

    @Test
    @DisplayName("Deve retornar 403 ao remover sem permissão")
    @WithMockUser(authorities = "GUEST")
    void deveRetornar403AoRemoverSemPermissao() throws Exception {

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 401 sem autenticação")
    void deveRetornar401SemAuth() throws Exception {

        mockMvc.perform(post(URL)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}