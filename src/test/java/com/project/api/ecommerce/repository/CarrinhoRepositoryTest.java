package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.support.DataEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CarrinhoRepositoryTest {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar um Optional contendo o carrinho quando o ID do usuário existir")
    void deveRetornarCarrinhoQuandoExistirUsuarioId() {
        Usuario usuario = usuarioRepository.save( DataEntityFactory.usuario() );

        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(usuario);
        carrinhoRepository.save(carrinho);

        Optional<Carrinho> result = carrinhoRepository.findByUsuarioId(usuario.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsuario().getId()).isEqualTo(usuario.getId());
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio quando não houver carrinho vinculado ao ID do usuário")
    void deveRetornarVazioQuandoNaoExistirUsuarioId() {
        Long usuarioId = 999L;

        Optional<Carrinho> result = carrinhoRepository.findByUsuarioId(usuarioId);

        assertThat(result).isEmpty();
    }
}