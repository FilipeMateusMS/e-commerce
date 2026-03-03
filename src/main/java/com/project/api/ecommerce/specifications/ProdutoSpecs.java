package com.project.api.ecommerce.specifications;

import com.project.api.ecommerce.dto.ProdutoSearchDTO;
import com.project.api.ecommerce.model.Produto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProdutoSpecs {

    public static Specification<Produto> nomeContem(String nome) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Produto> marcaContem(String marca) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("marca")), "%" + marca.toLowerCase() + "%");
    }

    public static Specification<Produto> descricaoContem(String descricao) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
    }

    public static Specification<Produto> precoMaiorQue(BigDecimal preco) {
        return (root, query, builder) ->
                builder.greaterThan(root.get("preco"), preco);
    }

    public static Specification<Produto> precoMenorQue(BigDecimal preco) {
        return (root, query, builder) ->
                builder.lessThan(root.get("preco"), preco);
    }

    public static Specification<Produto> quantidadeMaiorQue(Integer quantidade) {
        return (root, query, builder) ->
                builder.greaterThan(root.get("quantidade"), quantidade);
    }

    public static Specification<Produto> quantidadeMenorQue(Integer quantidade) {
        return (root, query, builder) ->
                builder.lessThan(root.get("quantidade"), quantidade);
    }

    public static Specification<Produto> categoriaIgual(String nmCategoria) {
        return (root, query, builder) ->
                builder.equal(root.get("categoria"), nmCategoria);
    }

    public static <T> Specification<T> emptySpec() {
        return (root, query, builder) ->
                builder.conjunction();
    }

    public static Specification<Produto> buildFromFilter(ProdutoSearchDTO dto) {
        Specification<Produto> spec = Specification.where( emptySpec() );

        if (dto.nome() != null && !dto.nome().isEmpty()) {
            spec = spec.and(nomeContem(dto.nome()));
        }
        if (dto.marca() != null && !dto.marca().isEmpty()) {
            spec = spec.and(marcaContem(dto.marca()));
        }
        if (dto.descricao() != null && !dto.descricao().isEmpty()) {
            spec = spec.and(descricaoContem(dto.descricao()));
        }
        if (dto.preco() != null) {
            spec = spec.and(precoMaiorQue(dto.preco()));
        }
        if (dto.quantidade() != null) {
            spec = spec.and(quantidadeMaiorQue(dto.quantidade()));
        }
        if (dto.nomeCategoria() != null) {
            spec = spec.and(categoriaIgual(dto.nomeCategoria()));
        }
        return spec;
    }
}
