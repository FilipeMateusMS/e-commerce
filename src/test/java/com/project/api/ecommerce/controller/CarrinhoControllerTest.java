package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.dto.response.CarrinhoResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.service.CarrinhoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CarrinhoControllerTest {

    private static final String URL = "/api/v1/carrinhos";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarrinhoService carrinhoService;

    @Autowired
    private ObjectMapper objectMapper;

    private CarrinhoResponseDTO carrinhoPadrao;

    @BeforeEach
    void setUp() {
        carrinhoPadrao = new CarrinhoResponseDTO(
                1L,
                new HashSet<>(),
                new BigDecimal("500.00")
        );
    }

    @Test
    @DisplayName("Deve retornar carrinho por ID com sucesso (USER)")
    @WithMockUser(authorities = "USER")
    void deveRetornarCarrinhoPorIdUser() throws Exception {

        when(carrinhoService.getCarrinhoById(1L))
                .thenReturn(carrinhoPadrao);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valorTotal").value(500.00));

        verify(carrinhoService).getCarrinhoById(1L);
    }

    @Test
    @DisplayName("Deve retornar carrinho por ID com sucesso (ADMIN)")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornarCarrinhoPorIdAdmin() throws Exception {

        when(carrinhoService.getCarrinhoById(1L))
                .thenReturn(carrinhoPadrao);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk());

        verify(carrinhoService).getCarrinhoById(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 quando carrinho não existir")
    @WithMockUser(authorities = "USER")
    void deveRetornar404QuandoNaoEncontrar() throws Exception {

        when(carrinhoService.getCarrinhoById(99L))
                .thenThrow(new ResourceNotFoundException("Carrinho não encontrado"));

        mockMvc.perform(get(URL + "/99"))
                .andExpect(status().isNotFound());

        verify(carrinhoService).getCarrinhoById(99L);
    }

    @Test
    @DisplayName("Deve retornar 401 sem autenticação")
    void deveRetornar401SemAuth() throws Exception {

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar carrinhos paginados (ADMIN)")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornarCarrinhosPaginados() throws Exception {

        Page<CarrinhoResponseDTO> page = new PageImpl<>(List.of(carrinhoPadrao));

        when(carrinhoService.getCarrinhos(any(Pageable.class)))
                .thenReturn(PageResponse.of(page));

        mockMvc.perform(get(URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(carrinhoService).getCarrinhos(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar 403 ao acessar carrinhos sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403AoBuscarTodos() throws Exception {
        mockMvc.perform(get(URL)).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve deletar carrinho com sucesso (USER)")
    @WithMockUser(authorities = "USER")
    void deveDeletarCarrinhoUser() throws Exception {

        doNothing().when(carrinhoService).deletarCarrinho(1L);

        mockMvc.perform(delete(URL + "/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(carrinhoService).deletarCarrinho(1L);
    }

    @Test
    @DisplayName("Deve deletar carrinho com sucesso (ADMIN)")
    @WithMockUser(authorities = "ADMIN")
    void deveDeletarCarrinhoAdmin() throws Exception {

        doNothing().when(carrinhoService).deletarCarrinho(1L);

        mockMvc.perform(delete(URL + "/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 403 ao deletar sem permissão")
    @WithMockUser(authorities = "GUEST")
    void deveRetornar403AoDeletarSemPermissao() throws Exception {

        mockMvc.perform(delete(URL + "/1").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
