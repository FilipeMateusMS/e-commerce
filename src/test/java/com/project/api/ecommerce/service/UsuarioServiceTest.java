package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.UsuarioRequestDTO;
import com.project.api.ecommerce.dto.request.UsuarioUpdateRequestDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.dto.response.UsuarioResponseDTO;
import com.project.api.ecommerce.mappers.UsuarioMapper;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.UsuarioRepository;
import com.project.api.ecommerce.support.DataDtoFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    @DisplayName("Deve listar todos os usuários cadastrados de forma paginada com sucesso")
    void deveListarUsuariosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuario = new Usuario();
        UsuarioResponseDTO dto = DataDtoFactory.criarUsuarioPadrao();
        Page<Usuario> page = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findAll(pageable)).thenReturn(page);
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);

        PageResponse<UsuarioResponseDTO> response = service.findAllUsuario(pageable);

        assertThat(response.content()).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar um usuário específico pelo ID e retornar seu DTO de resposta")
    void deveBuscarUsuarioPorIdComSucesso() {
        Usuario usuario = new Usuario();
        UsuarioResponseDTO dto = DataDtoFactory.criarUsuarioPadrao();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);

        UsuarioResponseDTO response = service.findUsuarioById(1L);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve criar um novo usuário, criptografar a senha e validar a unicidade do nome")
    void deveCriarUsuarioComSucesso() {
        UsuarioRequestDTO request = new UsuarioRequestDTO("User 1", "user1@gmail.com", "12345678");

        Usuario usuario = new Usuario();
        Usuario usuarioSalvo = new Usuario();
        UsuarioResponseDTO dto = DataDtoFactory.criarUsuarioPadrao();

        when(usuarioRepository.existsByNome( anyString() )).thenReturn(false);
        when(usuarioMapper.toEntity(request)).thenReturn(usuario);
        when(passwordEncoder.encode("12345678")).thenReturn("senha-criptografada");
        when(usuarioRepository.save(usuario)).thenReturn(usuarioSalvo);
        when(usuarioMapper.toDTO(usuarioSalvo)).thenReturn(dto);

        UsuarioResponseDTO response = service.criarUsuario(request);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve alterar os dados cadastrais de um usuário existente com base no ID informado")
    void deveAlterarUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setNome("Antigo");

        UsuarioUpdateRequestDTO request = new UsuarioUpdateRequestDTO("Novo");
        Usuario usuarioSalvo = new Usuario();
        UsuarioResponseDTO dto = DataDtoFactory.criarUsuarioPadrao();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuarioSalvo);
        when(usuarioMapper.toDTO(usuarioSalvo)).thenReturn(dto);

        UsuarioResponseDTO response = service.alterarUsuario(request, 1L);

        assertThat(response).isEqualTo(dto);
        assertThat(usuario.getNome()).isEqualTo("Novo");
    }

    @Test
    @DisplayName("Deve remover um usuário do sistema após validar sua existência no banco de dados")
    void deveDeletarUsuarioComSucesso() {
        Usuario usuario = new Usuario();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        service.deletarUsuario(1L);

        verify(usuarioRepository).delete(usuario);
    }
}