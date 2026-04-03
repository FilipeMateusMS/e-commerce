package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.RegisterRequestDTO;
import com.project.api.ecommerce.dto.response.AuthResponseDTO;
import com.project.api.ecommerce.model.Role;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.RoleRepository;
import com.project.api.ecommerce.repository.UsuarioRepository;
import com.project.api.ecommerce.security.jwt.JwtUtils;
import com.project.api.ecommerce.security.user.ShopUsuarioDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private ShopUsuarioDetails userDetails;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar o token JWT quando as credenciais forem válidas")
    void deveRealizarLoginComSucesso() {
        // Arrange
        when(authenticationManager.authenticate(any())) .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(1L);
        when(jwtUtils.generateTokenForUser(authentication)).thenReturn("token-fake");

        // Act
        AuthResponseDTO response = authService.login("teste@email.com", "123456");

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.token()).isEqualTo("token-fake");
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso, salvar no banco e retornar o token de acesso")
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO(
                "User 21",
                "user21@email.com",
                "123456",
                "61999999999"
        );

        Role role = new Role();
        role.setNome("USER");

        when(roleRepository.findByNome("USER")).thenReturn(Optional.of(role));
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("senha-criptografada");

        // reaproveitando o mock do login
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(1L);
        when(jwtUtils.generateTokenForUser(authentication)).thenReturn("token-fake");

        // Act
        AuthResponseDTO response = authService.register(request);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.token()).isEqualTo("token-fake");

        verify(usuarioRepository).save(any(Usuario.class));
    }
}