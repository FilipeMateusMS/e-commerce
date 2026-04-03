package com.project.api.ecommerce.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.support.DataEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @DisplayName("Deve retornar uma lista de produtos filtrados pelo nome da categoria")
    void deveRetornarProdutosPorNomeDaCategoria() {
        Categoria categoria1 = categoriaRepository.save(DataEntityFactory.categoria());
        Categoria categoria2 = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto1 = produtoRepository.save(DataEntityFactory.produto(categoria1));
        Produto produto2 = produtoRepository.save(DataEntityFactory.produto(categoria1));
        produtoRepository.save(DataEntityFactory.produto(categoria2));

        List<Produto> resultado = produtoRepository.findByCategoriaNome(categoria1.getNome());

        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado.getFirst().getCategoria().getNome())
                .isEqualTo(categoria1.getNome());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o nome da categoria pesquisada não existir")
    void deveRetornarListaVaziaQuandoCategoriaNaoExistir() {
        List<Produto> resultado = produtoRepository.findByCategoriaNome("categoria-inexistente");

        assertThat(resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar true quando existir um produto com o nome informado, ignorando maiúsculas e minúsculas")
    void deveRetornarTrueQuandoNomeExistirIgnorandoCase() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto = DataEntityFactory.produto(categoria);
        produto.setNome("Notebook");
        produtoRepository.save(produto);

        boolean exists = produtoRepository.existsByNomeIgnoreCase("notebook");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não houver nenhum produto com o nome informado (ignore case)")
    void deveRetornarFalseQuandoNomeNaoExistirIgnorandoCase() {
        boolean exists = produtoRepository.existsByNomeIgnoreCase("produto-inexistente");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false ao verificar duplicidade de nome se o nome pertencer ao próprio ID do produto")
    void deveRetornarFalseQuandoNomeExistirMasForMesmoId() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto = DataEntityFactory.produto(categoria);
        produto.setNome("Mouse");
        produto = produtoRepository.save(produto);

        boolean exists = produtoRepository.existsByNomeIgnoreCaseAndIdNot(
                "mouse",
                produto.getId()
        );

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true ao verificar duplicidade se o nome já estiver em uso por um produto de ID diferente")
    void deveRetornarTrueQuandoNomeExistirComIdDiferente() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto1 = DataEntityFactory.produto(categoria);
        produto1.setNome("Teclado");
        produtoRepository.save(produto1);

        Produto produto2 = DataEntityFactory.produto(categoria);
        produto2.setNome("Outro");
        produto2 = produtoRepository.save(produto2);

        boolean exists = produtoRepository.existsByNomeIgnoreCaseAndIdNot(
                "teclado",
                produto2.getId()
        );

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar todos os produtos cujos IDs estejam presentes na lista informada")
    void deveRetornarProdutosPorListaDeIds() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto1 = produtoRepository.save(DataEntityFactory.produto(categoria));
        Produto produto2 = produtoRepository.save(DataEntityFactory.produto(categoria));

        Set<Long> ids = Set.of(produto1.getId(), produto2.getId());

        List<Produto> resultado = produtoRepository.findByIdIn(ids);

        assertThat(resultado.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando nenhum dos IDs informados for encontrado no banco")
    void deveRetornarListaVaziaQuandoIdsNaoExistirem() {
        Set<Long> ids = Set.of(999L, 1000L);

        List<Produto> resultado = produtoRepository.findByIdIn(ids);

        assertThat(resultado.isEmpty()).isTrue();
    }
}
