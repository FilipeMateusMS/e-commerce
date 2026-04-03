package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.dto.response.PedidoResponseDTO;
import com.project.api.ecommerce.dto.response.PedidoStatusDTO;
import com.project.api.ecommerce.mappers.PedidoMapper;
import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.model.enums.PedidoStatus;
import com.project.api.ecommerce.repository.CarrinhoRepository;
import com.project.api.ecommerce.repository.PedidoRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.security.user.UserAuthenticatedService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CarrinhoService carrinhoService;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private UserAuthenticatedService userAuthenticatedService;

    @InjectMocks
    private PedidoService service;

    @Test
    @DisplayName("Deve buscar um pedido pelo ID e retornar seu DTO de resposta com sucesso")
    void deveBuscarPedidoPorIdComSucesso() {
        Pedido pedido = new Pedido();
        PedidoResponseDTO dto = DataDtoFactory.criarPedidoPadrao();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toDTO(pedido)).thenReturn(dto);

        PedidoResponseDTO response = service.getPedido(1L);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve listar todos os pedidos do usuário autenticado de forma paginada")
    void deveListarPedidosDoUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);

        Pedido pedido = new Pedido();
        PedidoResponseDTO dto = DataDtoFactory.criarPedidoPadrao();
        Page<Pedido> page = new PageImpl<>(List.of(pedido));

        when(userAuthenticatedService.getAuthenticatedUser()).thenReturn(usuario);
        when(pedidoRepository.findAllByUsuarioId(1L, pageable)).thenReturn(page);
        when(pedidoMapper.toDTO(pedido)).thenReturn(dto);

        PageResponse<PedidoResponseDTO> response = service.getUsuarioPedidos(pageable);

        assertThat(response.content()).hasSize(1);
    }

    @Test
    @DisplayName("Deve converter um carrinho em pedido, atualizar o estoque e remover o carrinho após finalizar")
    void deveFinalizarPedidoComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Produto produto = new Produto();
        produto.setQuantidade(10);
        produto.setPrecoUnitario(BigDecimal.TEN);

        CarrinhoItem item = new CarrinhoItem();
        item.setProduto(produto);
        item.setQuantidade(2);

        Carrinho carrinho = new Carrinho();
        carrinho.setId(5L);
        carrinho.setUsuario(usuario);
        carrinho.setItensCarrinho(Set.of(item));

        Pedido pedidoSalvo = new Pedido();
        PedidoResponseDTO dto = DataDtoFactory.criarPedidoPadrao();

        when(userAuthenticatedService.getAuthenticatedUser()).thenReturn(usuario);
        when(carrinhoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(carrinho));
        when(pedidoRepository.save(any())).thenReturn(pedidoSalvo);
        when(pedidoMapper.toDTO(pedidoSalvo)).thenReturn(dto);

        PedidoResponseDTO response = service.finalizarPedido();

        assertThat(response).isEqualTo(dto);
        verify(produtoRepository).save(produto);
        verify(carrinhoService).deletarCarrinho(5L);
    }

    @Test
    @DisplayName("Deve alterar o status de um pedido existente para o novo status informado")
    void deveAlterarStatusPedidoComSucesso() {
        Pedido pedido = new Pedido();
        pedido.setPedidoStatus(PedidoStatus.PENDENTE);

        Pedido pedidoSalvo = new Pedido();
        PedidoResponseDTO dto = DataDtoFactory.criarPedidoPadrao();

        PedidoStatusDTO request = new PedidoStatusDTO(PedidoStatus.ENTREGUE);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedidoSalvo);
        when(pedidoMapper.toDTO(pedidoSalvo)).thenReturn(dto);

        PedidoResponseDTO response = service.alterarPedidoStatus(1L, request);

        assertThat(response).isEqualTo(dto);
        assertThat(pedido.getPedidoStatus()).isEqualTo(PedidoStatus.ENTREGUE);
    }
}