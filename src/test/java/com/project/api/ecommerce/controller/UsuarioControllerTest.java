package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.dto.request.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.request.UsuarioUpdateRequestDTO;
import com.project.api.ecommerce.dto.response.UsuarioResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.service.UsuarioService;
import com.project.api.ecommerce.service.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    private static final String URL = "/api/v1/usuarios";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StorageService storageService;

    private UsuarioResponseDTO usuarioPadrao;

    @BeforeEach
    void setUp() {
        usuarioPadrao = new UsuarioResponseDTO(
                1L,
                "João",
                "joao@email.com"
        );
    }
    @Test
    @DisplayName("Deve retornar usuário por ID (USER)")
    @WithMockUser(authorities = "USER")
    void deveRetornarUsuarioPorId() throws Exception {
        when(usuarioService.findUsuarioById(1L))
                .thenReturn(usuarioPadrao);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(usuarioService).findUsuarioById(1L);
    }

    @Test
    @DisplayName("Deve retornar 403 para ADMIN acessar endpoint de USER")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornar403Admin() throws Exception {
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 401 sem autenticação")
    void deveRetornar401() throws Exception {

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar usuários paginados (ADMIN)")
    @WithMockUser(authorities = "ADMIN")
    void deveRetornarUsuarios() throws Exception {

        Page<UsuarioResponseDTO> page =
                new PageImpl<>(List.of(usuarioPadrao));

        when(usuarioService.findAllUsuario(any(Pageable.class)))
                .thenReturn(PageResponse.of(page));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(usuarioService).findAllUsuario(any());
    }

    @Test
    @DisplayName("Deve retornar 403 ao acessar lista sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403List() throws Exception {

        mockMvc.perform(get(URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void deveCriarUsuario() throws Exception {

        UsuarioRequestDTO request = new UsuarioRequestDTO("Maria", "maria@email.com", "123456");

        when(usuarioService.criarUsuario(any()))
                .thenReturn(new UsuarioResponseDTO(2L, "Maria", "maria@email.com"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));

        verify(usuarioService).criarUsuario(any());
    }

    @Test
    @DisplayName("Deve retornar 403 ao criar sem ser ADMIN")
    @WithMockUser(authorities = "USER")
    void deveRetornar403Criar() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "Maria",
                "email",
                "123"
        );

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso (USER)")
    @WithMockUser(authorities = "USER")
    void deveAtualizarUsuario() throws Exception {

        UsuarioUpdateRequestDTO request = new UsuarioUpdateRequestDTO("Novo Nome");

        when(usuarioService.alterarUsuario(any(), eq(1L)))
                .thenReturn(usuarioPadrao);

        mockMvc.perform(put(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(usuarioService).alterarUsuario(any(), eq(1L));
    }

    @Test
    @DisplayName("Deve retornar 403 ao atualizar sem permissão")
    @WithMockUser(authorities = "GUEST")
    void deveRetornar403Update() throws Exception {

        UsuarioUpdateRequestDTO request =
                new UsuarioUpdateRequestDTO("Nome");

        mockMvc.perform(put(URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    @WithMockUser(authorities  = "ADMIN")
    void deveDeletarUsuario() throws Exception {

        doNothing().when(usuarioService).deletarUsuario(1L);

        mockMvc.perform(delete(URL + "/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(usuarioService).deletarUsuario(1L);
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
