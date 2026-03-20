package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.response.PedidoResponseDTO;
import com.project.api.ecommerce.dto.response.PedidoStatusDTO;
import com.project.api.ecommerce.enums.PedidoStatus;
import com.project.api.ecommerce.exceptions.BusinessAlertException;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.PedidoMapper;
import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.commom.pagination.PageResponse;
import com.project.api.ecommerce.repository.CarrinhoRepository;
import com.project.api.ecommerce.repository.PedidoRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.security.user.UserAuthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final CarrinhoService carrinhoService;
    private final PedidoMapper pedidoMapper;
    private final UserAuthenticatedService userAuthenticatedService;

    @Cacheable(value = "pedidos", key = "#pedidoId")
    public PedidoResponseDTO getPedido(Long pedidoId ) {
        return pedidoMapper.toDTO( pedidoRepository.findById( pedidoId )
                .orElseThrow( () -> new ResourceNotFoundException( "Pedido não encontrado" ) ) );
    }

    // Não incluí no cache, pois é um dado sensível do usuário e pouco reutilizável
    public PageResponse<PedidoResponseDTO> getUsuarioPedidos(Pageable pageable ){
        Usuario usuario = userAuthenticatedService.getAuthenticatedUser();

        Page<PedidoResponseDTO> peditoDTOPage = pedidoRepository.findAllByUsuarioId( usuario.getId(), pageable )
                .map( pedidoMapper:: toDTO );
        return PageResponse.of( peditoDTOPage );
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "pedidos", allEntries = true),
                       @CacheEvict(value = "produtos", allEntries = true) // Estoque mudou
    })
    public PedidoResponseDTO finalizarPedido(){
        Usuario usuario = userAuthenticatedService.getAuthenticatedUser();

        Carrinho carrinho = carrinhoRepository.findByUsuarioId( usuario.getId() )
                .orElseThrow( () -> new ResourceNotFoundException( "Usuário não possui carrinho ativo" ) );

        Pedido pedidoSalvo = pedidoRepository.save( criarPedido( carrinho ) );
        carrinhoService.deletarCarrinho( carrinho.getId() );
        return pedidoMapper.toDTO( pedidoSalvo );
    }

    private Pedido criarPedido( Carrinho carrinho ){
        Pedido pedido = new Pedido();
        pedido.setDataPedido( LocalDate.now() );
        pedido.setPedidoStatus( PedidoStatus.PENDENTE );
        pedido.setUsuario( carrinho.getUsuario() );

        Set<PedidoItem> itensPedidos = carrinho.getItensCarrinho().stream().map(carrinhoitem -> {
            Produto produto = carrinhoitem.getProduto();

            if( produto.getQuantidade() < carrinhoitem.getQuantidade() )
                throw new BusinessAlertException( "Estoque insuficiente para o produto '" + produto.getNome() + "'" );

            produto.setQuantidade( produto.getQuantidade() - carrinhoitem.getQuantidade() );
            produtoRepository.save( produto );
            return new PedidoItem( pedido, produto, carrinhoitem.getQuantidade(), produto.getPrecoUnitario() );
        }).collect( Collectors.toSet() );

        pedido.setItensPedidos( itensPedidos );
        pedido.setPrecoVendaTotal( pedido.calcularPrecoTotal() );
        return pedido;
    }

    @Transactional
    @CacheEvict(value = "pedidos", allEntries = true)
    public PedidoResponseDTO alterarPedidoStatus(Long idPedido, PedidoStatusDTO pedidoStatusDTO ) {
        Pedido pedido = pedidoRepository.findById( idPedido )
                .orElseThrow( () -> new ResourceNotFoundException( "Pedido não encontrado" ) );

        if( pedido.getPedidoStatus() == pedidoStatusDTO.pedidoStatus() )
            throw new ResourceAlreadyExistsException( "O pedido já existe com o status" + pedidoStatusDTO.pedidoStatus().name() );

        pedido.setPedidoStatus( pedidoStatusDTO.pedidoStatus() );
        return pedidoMapper.toDTO( pedidoRepository.save( pedido ) );
    }
}