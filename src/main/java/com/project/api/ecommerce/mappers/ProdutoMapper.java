package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.dto.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.ProdutoResponseDTO;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    private final CategoriaMapper categoriaMapper;

    public ProdutoMapper(CategoriaMapper categoriaMapper) {
        this.categoriaMapper = categoriaMapper;
    }

    public ProdutoResponseDTO toDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getMarca(),
                produto.getDescricao(),
                produto.getPrecoUnitario(),
                produto.getQuantidade(),
                categoriaMapper.toCategoriaProdutoResponse( produto.getCategoria() )
        );
    }
    public Produto toProduto( ProdutoRequestDTO produtoDTO, Categoria categoria ) {
        return new Produto(
                produtoDTO.nome(),
                produtoDTO.marca(),
                produtoDTO.descricao(),
                produtoDTO.preco(),
                produtoDTO.quantidade(),
                categoria
        );
    }

//    public List<ProdutoCategoriaDTO> toListProdutoCategoria( List<Produto> produtos ) {
//        return produtos.stream().map( this::toProdutoCategoria ).toList();
//    }
//
//    public ProdutoCategoriaDTO toProdutoCategoria( Produto produto ) {
//        return new ProdutoCategoriaDTO(
//                produto.getId(),
//                produto.getNome(),
//                produto.getMarca(),
//                produto.getDescricao(),
//                produto.getPrecoUnitario(),
//                produto.getQuantidade() );
//    }
}
