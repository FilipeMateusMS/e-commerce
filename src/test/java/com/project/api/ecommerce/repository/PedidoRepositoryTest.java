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
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoItemRepository pedidoItemRepository;

    @Test
    @DisplayName("Deve retornar uma página com todos os pedidos vinculados ao ID de um usuário específico")
    void deveRetornarPedidosPorUsuarioId() {
        Usuario usuario1 = usuarioRepository.save(DataEntityFactory.usuario());
        Usuario usuario2 = usuarioRepository.save(DataEntityFactory.usuario());

        // Pedidos do usuário 1
        pedidoRepository.save( criarPedidoCompleto( usuario1 ) );
        pedidoRepository.save( criarPedidoCompleto( usuario1 ) );

        // Pedido do usuário 2 (não deve vir)
        pedidoRepository.save( criarPedidoCompleto( usuario2 ));

        Page<Pedido> resultado = pedidoRepository.findAllByUsuarioId(
                usuario1.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando o usuário informado não possuir pedidos cadastrados")
    void deveRetornarPaginaVaziaQuandoUsuarioNaoPossuirPedidos() {
        Usuario usuario = usuarioRepository.save(DataEntityFactory.usuario());

        // ação
        Page<Pedido> resultado = pedidoRepository.findAllByUsuarioId(
                usuario.getId(),
                PageRequest.of(0, 10)
        );

        // verificação
        assertThat(resultado.getContent().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve garantir que a busca filtre corretamente apenas os pedidos pertencentes ao usuário informado")
    void deveRetornarSomentePedidosDoUsuarioInformado() {
        Usuario usuario1 = usuarioRepository.save(DataEntityFactory.usuario());
        Usuario usuario2 = usuarioRepository.save(DataEntityFactory.usuario());

        pedidoRepository.save(DataEntityFactory.pedido(usuario1));
        pedidoRepository.save(DataEntityFactory.pedido(usuario2));

        Page<Pedido> resultado = pedidoRepository.findAllByUsuarioId(
                usuario1.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent().size()).isEqualTo(1);
        assertThat(resultado.getContent().getFirst().getUsuario().getId())
                .isEqualTo(usuario1.getId());
    }

    private Pedido criarPedidoCompleto(Usuario usuario) {
        Categoria categoria = categoriaRepository.save(DataEntityFactory.categoria());
        Produto produto = produtoRepository.save(DataEntityFactory.produto(categoria));

        Pedido pedido = pedidoRepository.save(DataEntityFactory.pedido(usuario));

        PedidoItem item = DataEntityFactory.pedidoItem(pedido, produto);
        pedidoItemRepository.save(item);

        pedido.getItensPedidos().add( item );
        pedidoRepository.save( pedido );

        return pedido;
    }
}