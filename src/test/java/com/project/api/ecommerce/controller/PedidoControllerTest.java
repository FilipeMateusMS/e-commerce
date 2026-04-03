package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.dto.response.*;
import com.project.api.ecommerce.model.enums.PedidoStatus;
import com.project.api.ecommerce.service.PedidoService;
import com.project.api.ecommerce.service.storage.StorageService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    private static final String URL = "/api/v1/pedidos";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PedidoResponseDTO pedidoPadrao;

    @BeforeEach
    void setUp() {
        pedidoPadrao = new PedidoResponseDTO(
                1L,
                new UsuarioResponseDTO(
                        10L,
                        "User 1",
                        "user1@email.com"
                ),
                Set.of(
                        new PedidoItemResponseDTO(
                                100L,
                                1,
                                new BigDecimal("3500.00"),
                                new BigDecimal("3500.00"),
                                null
                        ),
                        new PedidoItemResponseDTO(
                                101L,
                                2,
                                new BigDecimal("300.00"),
                                new BigDecimal("150.00"),
                                null
                        )
                ),
                PedidoStatus.ENTREGUE,
                new BigDecimal("3800.00"),
                LocalDate.now()
        );
    }

    @Test
    @DisplayName("Deve retornar pedido por ID (USER)")
    @WithMockUser(authorities = "USER")
    void deveRetornarPedidoPorIdUser() throws Exception {

        when(pedidoService.getPedido(1L)).thenReturn(pedidoPadrao);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(pedidoService).getPedido(1L);
    }

    @Test
    @DisplayName("Deve retornar pedido por ID (ADMIN)")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornarPedidoPorIdAdmin() throws Exception {

        when(pedidoService.getPedido(1L)).thenReturn(pedidoPadrao);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 401 sem autenticação")
    void deveRetornar401() throws Exception {

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Deve retornar pedidos do usuário paginados")
    @WithMockUser(authorities = "USER")
    void deveRetornarPedidosDoUsuario() throws Exception {

        Page<PedidoResponseDTO> page = new PageImpl<>(List.of(pedidoPadrao));

        when(pedidoService.getUsuarioPedidos(any(Pageable.class)))
                .thenReturn(PageResponse.of(page));

        mockMvc.perform(get(URL + "/usuario")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(pedidoService).getUsuarioPedidos(any());
    }

    @Test
    @DisplayName("Deve finalizar pedido com sucesso")
    @WithMockUser(authorities = "USER")
    void deveFinalizarPedido() throws Exception {

        when(pedidoService.finalizarPedido())
                .thenReturn(pedidoPadrao);

        mockMvc.perform(post(URL + "/usuario/finalizar")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(pedidoService).finalizarPedido();
    }

    @Test
    @DisplayName("Deve retornar 403 ao finalizar sem permissão")
    @WithMockUser(authorities = "GUEST")
    void deveRetornar403Finalizar() throws Exception {

        mockMvc.perform(post(URL + "/usuario/finalizar")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve alterar status do pedido (ADMIN)")
    @WithMockUser(authorities = "ADMIN")
    void deveAlterarStatusPedido() throws Exception {

        PedidoStatusDTO request = new PedidoStatusDTO(PedidoStatus.ENTREGUE);

        when(pedidoService.alterarPedidoStatus(eq(1L), any()))
                .thenReturn(pedidoPadrao);

        mockMvc.perform(patch(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(pedidoService).alterarPedidoStatus(eq(1L), any());
    }

    @Test
    @DisplayName("Deve retornar 403 ao alterar status sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403AlterarStatus() throws Exception {

        PedidoStatusDTO request = new PedidoStatusDTO( PedidoStatus.PROCESSANDO );

        mockMvc.perform(patch(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
