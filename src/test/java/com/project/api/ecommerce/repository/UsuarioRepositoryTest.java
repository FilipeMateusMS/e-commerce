package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.support.DataEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar true quando existir um usuário cadastrado com o nome informado")
    void deveRetornarTrueQuandoNomeExistir() {
        Usuario usuario = DataEntityFactory.usuario();
        usuario.setNome("João");

        usuarioRepository.save(usuario);

        boolean exists = usuarioRepository.existsByNome("João");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não houver nenhum usuário com o nome informado")
    void deveRetornarFalseQuandoNomeNaoExistir() {
        boolean exists = usuarioRepository.existsByNome("NomeInexistente");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar um Optional contendo o usuário quando o e-mail informado for encontrado")
    void deveRetornarUsuarioQuandoEmailExistir() {
        Usuario usuario = DataEntityFactory.usuario();
        usuario.setEmail("teste@email.com");

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");

        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get().getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio quando o e-mail informado não existir na base de dados")
    void deveRetornarOptionalVazioQuandoEmailNaoExistir() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("email@inexistente.com");

        assertThat(resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar true ao verificar a existência de um cadastro pelo e-mail informado")
    void deveRetornarTrueQuandoEmailExistir() {
        Usuario usuario = DataEntityFactory.usuario();
        usuario.setEmail("email@teste.com");

        usuarioRepository.save(usuario);

        boolean exists = usuarioRepository.existsByEmail("email@teste.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false ao verificar a existência de um e-mail que não está cadastrado")
    void deveRetornarFalseQuandoEmailNaoExistir() {
        boolean exists = usuarioRepository.existsByEmail("naoexiste@email.com");

        assertThat(exists).isFalse();
    }
}
