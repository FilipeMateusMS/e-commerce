package com.project.api.ecommerce.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.support.DataEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ImagemRepositoryTest {

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @DisplayName("Deve retornar uma página com todas as imagens vinculadas ao ID de um produto específico")
    void deveRetornarImagensPorProdutoId() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto1 = produtoRepository.save( DataEntityFactory.produto(categoria));
        Produto produto2 = produtoRepository.save( DataEntityFactory.produto(categoria));

        // Imagens do produto 1
        imagemRepository.save( DataEntityFactory.imagem( produto1 ));
        imagemRepository.save( DataEntityFactory.imagem( produto1));

        // Imagem do produto 2 (não deve vir na busca)
        imagemRepository.save(DataEntityFactory.imagem(produto2));

        Page<Imagem> resultado = imagemRepository.findAllByProdutoId(
                produto1.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando o produto informado não possuir imagens cadastradas")
    void deveRetornarPaginaVaziaQuandoProdutoNaoPossuirImagens() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());
        Produto produto = produtoRepository.save(DataEntityFactory.produto(categoria));

        // ação
        Page<Imagem> resultado = imagemRepository.findAllByProdutoId(
                produto.getId(),
                PageRequest.of(0, 10)
        );

        // verificação
        assertThat(resultado.getContent().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve garantir que a busca retorne estritamente as imagens do produto solicitado, filtrando outros produtos")
    void deveRetornarSomenteImagensDoProdutoInformado() {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());

        Produto produto1 = produtoRepository.save(DataEntityFactory.produto(categoria));
        Produto produto2 = produtoRepository.save(DataEntityFactory.produto(categoria));

        imagemRepository.save( DataEntityFactory.imagem( produto1 ) );
        imagemRepository.save( DataEntityFactory.imagem( produto2 ) );

        // Somente o produto 1 deve ser retornado
        Page<Imagem> resultado = imagemRepository.findAllByProdutoId(
                produto1.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent().size()).isEqualTo(1);
        assertThat(resultado.getContent().getFirst().getProduto().getId()).isEqualTo(produto1.getId());
    }
}