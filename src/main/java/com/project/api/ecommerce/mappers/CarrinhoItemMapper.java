package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.CarrinhoItemResponseDTO;
import com.project.api.ecommerce.model.CarrinhoItem;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoItemMapper {

    private ProdutoMapper produtoMapper;

    public CarrinhoItemMapper(ProdutoMapper produtoMapper) {
        this.produtoMapper = produtoMapper;
    }

    public CarrinhoItemResponseDTO toDTO(CarrinhoItem carrinhoItem) {
        return new CarrinhoItemResponseDTO(
                carrinhoItem.getId(),
                produtoMapper.toDTO( carrinhoItem.getProduto() ),
                carrinhoItem.getQuantidade(),
                carrinhoItem.calcularPrecoItem() );
    }
}
