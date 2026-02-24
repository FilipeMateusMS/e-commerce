package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.CarrinhoItemRequestDTO;
import com.project.api.ecommerce.dto.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.dto.CarrinhoItemUpdateDTO;
import com.project.api.ecommerce.exceptions.EstoqueInsuficienteException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.CarrinhoItemMapper;
import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.model.CarrinhoItem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.model.Usuario;
import com.project.api.ecommerce.repository.CarrinhoItemRepository;
import com.project.api.ecommerce.repository.CarrinhoRepository;
import com.project.api.ecommerce.security.user.UserAuthenticatedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class CarrinhoItemService {

    private final CarrinhoItemRepository carrinhoItemRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoService produtoService;
    private final CarrinhoItemMapper carrinhoItemMapper;
    private final UserAuthenticatedService userAuthenticatedService;

    private static final Logger log = LoggerFactory.getLogger( CarrinhoItemService.class );

    public CarrinhoItemService(CarrinhoItemRepository carrinhoItemRepository, CarrinhoRepository carrinhoRepository, ProdutoService produtoService, CarrinhoItemMapper carrinhoItemMapper, UserAuthenticatedService userAuthenticatedService) {
        this.carrinhoItemRepository = carrinhoItemRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.produtoService = produtoService;
        this.carrinhoItemMapper = carrinhoItemMapper;
        this.userAuthenticatedService = userAuthenticatedService;
    }

    public CarrinhoItemResponseDTO obterCarrinhoItem(Long idCarrinhoItem )
    {
        return carrinhoItemMapper.toDTO( carrinhoItemRepository.findById( idCarrinhoItem )
                .orElseThrow( () -> new ResourceNotFoundException( "Item do carrinho não encontrado" ) ) );
    }

    /*
        Caso não exista o carrinho, cria carrinho
        Caso o produto já esteja no carrinho altera a quantidade
        Caso o produto não esteja no carrinho insere um novo CarrinhoItem
        Obtem o carrinho do usuário
     */
    @Transactional
    public CarrinhoItemResponseDTO upsertItemNoCarrinho( CarrinhoItemRequestDTO carrinhoItemDTO ) {
        Produto produto = produtoService.getProdutoEntity( carrinhoItemDTO.idProduto() );
        if( carrinhoItemDTO.quantidade() > produto.getQuantidade() )
            throw new EstoqueInsuficienteException( "Estoque insuficiente para o produto '" + produto.getNome() + "'" );

        Carrinho carrinho;
        CarrinhoItem carrinhoItem;

        Usuario usuarioAutenticado = userAuthenticatedService.getAuthenticatedUser();
        Optional<Carrinho> carrinhoOptional = carrinhoRepository.findByUsuarioId( usuarioAutenticado.getId() );

        if( carrinhoOptional.isPresent() )
        {
            carrinho = carrinhoOptional.get();

            Optional<CarrinhoItem> carrinhoItemOptional = carrinho.getItensCarrinho()
                    .stream()
                    .filter(item -> Objects.equals( item.getProduto().getId(), carrinhoItemDTO.idProduto() ) )
                    .findFirst();

            if( carrinhoItemOptional.isEmpty() ) { // Se o carrinho não tem o mesmo produto
                carrinhoItem = new CarrinhoItem( carrinho, produto, carrinhoItemDTO.quantidade() );
                carrinho.getItensCarrinho().add( carrinhoItem ); // Novo item no carrinho
            }
            else
            {
                // Atualiza quantidade do item existente
                carrinhoItem = carrinhoItemOptional.get();
                carrinhoItem.setQuantidade( carrinhoItem.getQuantidade() + carrinhoItemDTO.quantidade() );
            }
        }
        else // Caso não tenha o carrinho não foi criado para o usuário autenticado
        {
            // Criou carrinho e adiciona um item
            carrinho = new Carrinho();
            carrinho.setUsuario( usuarioAutenticado );
            carrinhoRepository.save( carrinho );

            carrinhoItem = new CarrinhoItem( carrinho, produto, carrinhoItemDTO.quantidade() );
            carrinho.getItensCarrinho().add( carrinhoItem );
        }
        carrinhoRepository.save( carrinho );
        return carrinhoItemMapper.toDTO( carrinhoItemRepository.save( carrinhoItem ) );
    }

    public CarrinhoItemResponseDTO alterarQuantidade( Long idCarrinhoItem, CarrinhoItemUpdateDTO carrinhoItemUpdateDTO ){
        CarrinhoItem carrinhoItem = carrinhoItemRepository.findById( idCarrinhoItem )
                        .orElseThrow(() -> new ResourceNotFoundException( "Carrinho não encontrado" ) );
        Produto produto = carrinhoItem.getProduto();

        if( produto.getQuantidade() - carrinhoItemUpdateDTO.quantidade() <= 0 )
            throw new EstoqueInsuficienteException( "Quantidade do produto deve ser maior do que 0" );

        carrinhoItem.setQuantidade( carrinhoItemUpdateDTO.quantidade() );

        log.info( "Quantidade atualizada idItem={} novaQuantidade={}",
                carrinhoItem.getId(),
                carrinhoItem.getQuantidade() );

        return carrinhoItemMapper.toDTO( carrinhoItemRepository.save( carrinhoItem ) );
    }

    public void removerItemDoCarrinho( Long idItemCarrinho ) {
        log.info("Removendo item carrinho idItem={}", idItemCarrinho);
        carrinhoItemRepository.deleteById( idItemCarrinho );
    }
}