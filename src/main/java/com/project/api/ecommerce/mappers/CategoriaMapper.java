package com.project.api.ecommerce.mappers;

import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.dto.CategoriaProdutoResponseDTO;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.dto.CategoriaResponseDTO;
import com.project.api.ecommerce.dto.ProdutoCategoriaDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriaMapper {

    public CategoriaResponseDTO toDTO( Categoria categoria ) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                toProdutosDaCategoria( categoria ) );
    }

    private List<ProdutoCategoriaDTO> toProdutosDaCategoria(Categoria categoria) {
        if( categoria.getProdutos() == null ) return new ArrayList<>();
        return categoria.getProdutos().stream().map( this::toProdutoCategoria ).toList();
    }

    // Não pode estar em ProdutoMapper, pois não pode injetar o mapper do entidade pai na do filho
    private ProdutoCategoriaDTO toProdutoCategoria( Produto produto ) {
        return new ProdutoCategoriaDTO(
                produto.getId(),
                produto.getNome(),
                produto.getMarca(),
                produto.getDescricao(),
                produto.getPrecoUnitario(),
                produto.getQuantidade()
        );
    }

    // Não retorna produtos
    public CategoriaProdutoResponseDTO toCategoriaProdutoResponse(Categoria categoria ) {
        return new CategoriaProdutoResponseDTO(
                categoria.getId() == null ? null : categoria.getId(),
                categoria.getNome() );
    }
}
