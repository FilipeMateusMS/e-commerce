package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.support.DataEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @DisplayName("Deve encontrar uma categoria pelo nome ignorando maiúsculas e minúsculas")
    void deveRetornarCategoriaQuandoNomeExistirIgnoreCase() {
        String categoriaNome = "Eletrônicos";
        Categoria categoria = categoriaRepository.save( DataEntityFactory.categoria( categoriaNome ) );

        Categoria result = categoriaRepository.findByNome("eletrônicos");

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo( categoriaNome );
    }

    @Test
    @DisplayName("Deve retornar null ao buscar por um nome de categoria que não existe")
    void deveRetornarNullQuandoNomeNaoExistir() {
        Categoria result = categoriaRepository.findByNome("inexistente");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve retornar true quando existir uma categoria cadastrada com o nome informado")
    void deveRetornarTrueQuandoNomeExistir() {
        Categoria categoria = DataEntityFactory.categoria();
        categoria.setNome("Games");
        categoriaRepository.save(categoria);

        boolean exists = categoriaRepository.existsByNome("Games");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não houver nenhuma categoria com o nome informado")
    void deveRetornarFalseQuandoNomeNaoExistir() {
        boolean exists = categoriaRepository.existsByNome("NaoExiste");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true quando o nome já existir em outra categoria (ID diferente)")
    void deveRetornarTrueQuandoNomeExistirComIdDiferente() {
        Categoria categoria = DataEntityFactory.categoria();
        categoria.setNome("Roupas");
        categoria = categoriaRepository.save(categoria);

        boolean exists = categoriaRepository.existsByNomeAndIdNot("Roupas", 999L);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando o nome existir apenas na própria categoria consultada (mesmo ID)")
    void deveRetornarFalseQuandoNomeExistirMasMesmoId() {
        Categoria categoria = DataEntityFactory.categoria();
        categoria.setNome("Roupas");
        categoria = categoriaRepository.save(categoria);

        boolean exists = categoriaRepository.existsByNomeAndIdNot("Roupas", categoria.getId());

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar uma página de categorias cujo nome contém o termo pesquisado ignorando case")
    void deveRetornarPaginaComNomeContendoIgnoreCase() {
        categoriaRepository.save( DataEntityFactory.categoria("Eletronicos"));
        categoriaRepository.save( DataEntityFactory.categoria("Eletrodomesticos"));
        categoriaRepository.save( DataEntityFactory.categoria("Roupas"));
        Pageable pageable = PageRequest.of(0, 10);

        Page<Categoria> result = categoriaRepository.findByNomeContainingIgnoreCase("eletro", pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Categoria::getNome)
                .containsExactlyInAnyOrder("Eletronicos", "Eletrodomesticos");
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando nenhum nome de categoria coincidir com o termo pesquisado")
    void deveRetornarPaginaVaziaQuandoNaoEncontrar() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Categoria> result = categoriaRepository.findByNomeContainingIgnoreCase("inexistente", pageable);

        assertThat(result.getContent()).isEmpty();
    }
}