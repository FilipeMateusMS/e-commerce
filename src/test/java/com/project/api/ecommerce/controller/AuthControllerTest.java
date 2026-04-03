package com.project.api.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.ecommerce.dto.request.LoginRequestDTO;
import com.project.api.ecommerce.dto.request.RegisterRequestDTO;
import com.project.api.ecommerce.dto.response.AuthResponseDTO;
import com.project.api.ecommerce.security.config.ShopConfigSecurity;
import com.project.api.ecommerce.security.jwt.JwtAccessDeniedHandler;
import com.project.api.ecommerce.security.jwt.JwtAuthEntryPoint;
import com.project.api.ecommerce.security.jwt.JwtUtils;
import com.project.api.ecommerce.security.user.UsuarioDetailsService;
import com.project.api.ecommerce.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class)
@Import(ShopConfigSecurity.class)
class AuthControllerTest {

    private static final String URL = "/api/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthResponseDTO responsePadrao;

    // Beans do configuração de segurança para serem injetados no ShopConfigSecurity
    @MockBean
    private UsuarioDetailsService userDetailsService;
    @MockBean
    private JwtAuthEntryPoint authEntryPoint;
    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        responsePadrao = new AuthResponseDTO(
                1L,
                "fake-jwt-token"
        );
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void deveRealizarLogin() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO(
                "user@email.com",
                "123456"
        );

        when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(responsePadrao);

        mockMvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authService).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 ao enviar login inválido")
    void deveRetornar400LoginInvalido() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("", "" );

        mockMvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve registrar usuário com sucesso")
    void deveRegistrarUsuario() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "(61) 99999-9999",
                "João",
                "joao@email.com",
                "12345678"
        );
        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(responsePadrao);

        mockMvc.perform(post(URL + "/register")
                        .with( csrf() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authService).register(any(RegisterRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 ao registrar com dados inválidos")
    void deveRetornar400RegisterInvalido() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO("", "", "", "" );

        mockMvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}