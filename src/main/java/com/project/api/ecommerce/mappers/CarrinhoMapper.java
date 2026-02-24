package com.project.api.ecommerce.mappers;
import com.project.api.ecommerce.dto.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.dto.CarrinhoResponseDTO;
import com.project.api.ecommerce.dto.ProdutoResponseDTO;
import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.model.CarrinhoItem;
import com.project.api.ecommerce.model.Produto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CarrinhoMapper {

    private final ProdutoMapper produtoMapper;
    private final CarrinhoItemMapper carrinhoItemMapper;

    public CarrinhoMapper(ProdutoMapper produtoMapper, CarrinhoItemMapper carrinhoItemMapper) {
        this.produtoMapper = produtoMapper;
        this.carrinhoItemMapper = carrinhoItemMapper;
    }


    public CarrinhoResponseDTO toResponseDTO( Carrinho carrinho ){
        return new CarrinhoResponseDTO(
                carrinho.getId(),
                toItensCarrinhoResponse( carrinho.getItensCarrinho() ),
                carrinho.calcularValorTotal() );
    }

    private Set<CarrinhoItemResponseDTO> toItensCarrinhoResponse(Set<CarrinhoItem> itensCarrinho) {
        if( itensCarrinho == null ) return null;
        Set<CarrinhoItemResponseDTO> carrinhoItensDTO = new HashSet<>();
        itensCarrinho.forEach( carrinhoItem -> {
            carrinhoItensDTO.add( carrinhoItemMapper.toDTO( carrinhoItem ));
        } );
        return carrinhoItensDTO;
    }
}
