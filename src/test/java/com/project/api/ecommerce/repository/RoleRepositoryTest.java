package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Role;
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
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Deve retornar um Optional contendo a Role quando o nome informado existir no banco")
    void deveRetornarRoleQuandoNomeExistir() {
        Role role = DataEntityFactory.roleAdmin();

        roleRepository.save(role);

        Optional<Role> resultado = roleRepository.findByNome("ADMIN");

        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get().getNome()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio quando não houver nenhuma Role com o nome informado")
    void deveRetornarOptionalVazioQuandoNomeNaoExistir() {
        Optional<Role> resultado = roleRepository.findByNome("ROLE_INEXISTENTE");

        assertThat(resultado.isEmpty()).isTrue();
    }
}
