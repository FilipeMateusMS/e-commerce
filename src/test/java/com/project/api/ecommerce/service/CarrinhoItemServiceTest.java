package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.CarrinhoItemRequestDTO;
import com.project.api.ecommerce.dto.request.CarrinhoItemUpdateRequestDTO;
import com.project.api.ecommerce.dto.response.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.mappers.CarrinhoItemMapper;
import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.model.CarrinhoItem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.CarrinhoItemRepository;
import com.project.api.ecommerce.repository.CarrinhoRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.security.user.UserAuthenticatedService;
import com.project.api.ecommerce.support.DataDtoFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrinhoItemServiceTest {

    @Mock
    private CarrinhoItemRepository carrinhoItemRepository;

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CarrinhoItemMapper carrinhoItemMapper;

    @Mock
    private UserAuthenticatedService userAuthenticatedService;

    @InjectMocks
    private CarrinhoItemService service;

    @Test
    @DisplayName("Deve obter os detalhes de um item do carrinho com sucesso através do ID")
    void deveObterCarrinhoItemComSucesso() {
        CarrinhoItem item = new CarrinhoItem();
        CarrinhoItemResponseDTO dto = DataDtoFactory.criarItemPadrao();

        when(carrinhoItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(carrinhoItemMapper.toDTO(item)).thenReturn(dto);

        CarrinhoItemResponseDTO response = service.obterCarrinhoItem(1L);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve criar um novo carrinho e adicionar o item quando o usuário ainda não possuir um carrinho ativo")
    void deveCriarCarrinhoEAdicionarItem() {
        CarrinhoItemRequestDTO request = new CarrinhoItemRequestDTO(1L, 2);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setQuantidade(10);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        CarrinhoItemResponseDTO dto = DataDtoFactory.criarItemPadrao();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(userAuthenticatedService.getAuthenticatedUser()).thenReturn(usuario);
        when(carrinhoRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());
        when(carrinhoItemMapper.toDTO(any())).thenReturn(dto);
        when(carrinhoItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CarrinhoItemResponseDTO response = service.upsertItemNoCarrinho(request);

        assertThat(response).isEqualTo(dto);

        verify(carrinhoRepository).save(any(Carrinho.class));
        verify(carrinhoItemRepository).save(any(CarrinhoItem.class));
    }

    @Test
    @DisplayName("Deve alterar a quantidade de um item já existente no carrinho validando o estoque")
    void deveAlterarQuantidadeComSucesso() {
        CarrinhoItem item = new CarrinhoItem();
        item.setId(1L);

        Produto produto = new Produto();
        produto.setQuantidade(10);

        item.setProduto(produto);

        CarrinhoItemResponseDTO dto = DataDtoFactory.criarItemPadrao();

        when(carrinhoItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(carrinhoItemRepository.save(any())).thenReturn(item);
        when(carrinhoItemMapper.toDTO(item)).thenReturn(dto);

        CarrinhoItemUpdateRequestDTO request = new CarrinhoItemUpdateRequestDTO(5);

        CarrinhoItemResponseDTO response = service.alterarQuantidade(1L, request);

        assertThat(response).isEqualTo(dto);
        assertThat(item.getQuantidade()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve remover um item do carrinho e excluir o carrinho caso ele fique vazio após a remoção")
    void deveRemoverItemDoCarrinho() {
        CarrinhoItem item = new CarrinhoItem();
        Carrinho carrinho = new Carrinho();

        carrinho.setId(10L);
        item.setCarrinho(carrinho);

        when(carrinhoItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(carrinhoItemRepository.existsByCarrinhoId(10L)).thenReturn(false);

        service.removerItemDoCarrinho(1L);

        verify(carrinhoItemRepository).deleteById(1L);
        verify(carrinhoRepository).deleteById(10L);
    }
}