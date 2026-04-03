package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.support.DataEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles( "test"  )
class CarrinhoItemRepositoryTest {

    @Autowired
    private CarrinhoItemRepository carrinhoItemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar true quando existir um carrinho com o ID informado" )
    void deveRetornarTrueQuandoExistirCarrinhoId()  {
        Categoria categoria = categoriaRepository.save( DataEntityFactory.categoria()  );
        Produto produto = produtoRepository.save( DataEntityFactory.produto(categoria ) );

        Usuario usuario = usuarioRepository.save( DataEntityFactory.usuario()  );
        Carrinho carrinho = carrinhoRepository.save( DataEntityFactory.carrinho(usuario ) );

        carrinhoItemRepository.save( DataEntityFactory.carrinhoItem(carrinho, produto ) );

        assertThat(carrinhoItemRepository.existsByCarrinhoId(carrinho.getId()  ) )
                .isTrue() ;
    }

    @Test
    @DisplayName("Deve retornar false quando não existir nenhum item para o ID do carrinho informado")
    void deveRetornarFalseQuandoNaoExistirCarrinhoId()  {
        // cenário
        Long carrinhoId = 999L;

        // ação
        boolean exists = carrinhoItemRepository.existsByCarrinhoId(carrinhoId );

        // verificação
        assertThat(exists ).isFalse() ;
    }

    @Test
    @DisplayName("Deve deletar todos os itens vinculados a um carrinho específico sem afetar outros carrinhos")
    void deveDeletarTodosOsItensPorCarrinhoId()  {
        Categoria categoria = categoriaRepository.save( DataEntityFactory.categoria()  );

        Produto produto1 = produtoRepository.save( DataEntityFactory.produto(categoria ) );
        Produto produto2 = produtoRepository.save( DataEntityFactory.produto(categoria ) );

        Usuario usuario1 = usuarioRepository.save( DataEntityFactory.usuario()  );
        Usuario usuario2 = usuarioRepository.save( DataEntityFactory.usuario()  );

        Carrinho carrinho1 = carrinhoRepository.save( DataEntityFactory.carrinho(usuario1 ) );
        Carrinho carrinho2 = carrinhoRepository.save( DataEntityFactory.carrinho(usuario2 ) );

        // Itens do carrinho 1
        carrinhoItemRepository.save( DataEntityFactory.carrinhoItem(carrinho1, produto1 ) );
        carrinhoItemRepository.save( DataEntityFactory.carrinhoItem(carrinho1, produto2 ) );

        // Itens do carrinho 2 ( não devem ser deletados  )
        carrinhoItemRepository.save( DataEntityFactory.carrinhoItem(carrinho2, produto2 ) );

        carrinhoItemRepository.deleteAllByCarrinhoId(carrinho1.getId()  );

        assertThat(carrinhoItemRepository.existsByCarrinhoId(carrinho1.getId()  ) ).isFalse() ;
        assertThat(carrinhoItemRepository.existsByCarrinhoId(carrinho2.getId()  ) ).isTrue() ;
    }
}
