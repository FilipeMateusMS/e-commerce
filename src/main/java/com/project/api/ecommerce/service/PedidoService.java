package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.PedidoDTO;
import com.project.api.ecommerce.dto.PedidoStatusDTO;
import com.project.api.ecommerce.enums.PedidoStatus;
import com.project.api.ecommerce.exceptions.EstoqueInsuficienteException;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.PedidoMapper;
import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.model.Pedido;
import com.project.api.ecommerce.model.PedidoItem;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.repository.CarrinhoRepository;
import com.project.api.ecommerce.repository.PedidoRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final CarrinhoService carrinhoService;
    private final PedidoMapper pedidoMapper;

    private static final Logger logger = LoggerFactory.getLogger( PedidoService.class );

    public PedidoService(PedidoRepository pedidoRepository, CarrinhoRepository carrinhoRepository, ProdutoRepository produtoRepository, CarrinhoService carrinhoService, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
        this.carrinhoService = carrinhoService;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidoDTO getPedido(Long pedidoId ) {
        return pedidoMapper.toDTO( pedidoRepository.findById( pedidoId )
                .orElseThrow( () -> new ResourceNotFoundException( "Pedido não encontrado" ) ) );
    }

    @Transactional
    public PedidoDTO finalizarPedido( Long usuarioId ){
        Carrinho carrinho = carrinhoRepository.findByUsuarioId( usuarioId )
                .orElseThrow( () -> new ResourceNotFoundException( "Usuário não encontrado" ) );

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
                throw new EstoqueInsuficienteException( "Estoque insuficiente para o produto '" + produto.getNome() + "'" );

            produto.setQuantidade( produto.getQuantidade() - carrinhoitem.getQuantidade() );
            produtoRepository.save( produto );
            return new PedidoItem( pedido, produto, carrinhoitem.getQuantidade(), produto.getPrecoUnitario() );
        }).collect( Collectors.toSet() );

        pedido.setItensPedidos( itensPedidos );
        pedido.setPrecoVendaTotal( calcularPrecoTotal( itensPedidos ) );
        return pedido;
    }

    private BigDecimal calcularPrecoTotal( Set<PedidoItem> itensPedidos ) {
        BigDecimal valorSoma = BigDecimal.ZERO;
        for (PedidoItem item : itensPedidos) {
            valorSoma = valorSoma.add( item.getPrecoVendaUnitario().multiply( BigDecimal.valueOf( item.getQuantidade() ) ) );
        }
        return valorSoma;
    }

    public PageResponse<PedidoDTO> getUsuarioPedidos( Long usuarioId, Pageable pageable ){
        Page<PedidoDTO> peditoDTOPage = pedidoRepository.findAllByUsuarioId( usuarioId, pageable )
                .map( pedidoMapper:: toDTO );
        return PageResponse.of( peditoDTOPage );
    }

    public PedidoDTO alterarPedidoStatus( Long idPedido, PedidoStatusDTO pedidoStatusDTO ) {
        Pedido pedido = pedidoRepository.findById( idPedido )
                .orElseThrow( () -> new ResourceNotFoundException( "Pedido não encontrado" ) );

        if( pedido.getPedidoStatus() == pedidoStatusDTO.pedidoStatus() )
            throw new ResourceAlreadyExistsException( "O pedido já existe com o status" + pedidoStatusDTO.pedidoStatus().name() );

        pedido.setPedidoStatus( pedidoStatusDTO.pedidoStatus() );
        return pedidoMapper.toDTO( pedidoRepository.save( pedido ) );
    }
}
