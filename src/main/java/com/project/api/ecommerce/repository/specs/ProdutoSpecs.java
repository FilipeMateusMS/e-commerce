package com.project.api.ecommerce.repository.specs;

import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import com.project.api.ecommerce.model.Produto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecs {

    public static Specification<Produto> buildFromFilter(ProdutoFilterDTO dto) {

        // Irá retornar todos os registros caso não seja fornecido no filtro
        Specification<Produto> spec = Specs.alwaysTrue();

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            spec = spec.and(Specs.like("nome", dto.getNome() ) );
        }

        if (dto.getMarca() != null && !dto.getMarca().isBlank()) {
            spec = spec.and(Specs.like("marca", dto.getMarca()));
        }

        if (dto.getDescricao() != null && !dto.getDescricao().isBlank()) {
            spec = spec.and(Specs.like("descricao", dto.getDescricao()));
        }

        if (dto.getPrecoMin() != null) {
            spec = spec.and(Specs.greaterThanOrEqual("precoUnitario", dto.getPrecoMin()));
        }

        if (dto.getPrecoMax() != null) {
            spec = spec.and(Specs.lessThanOrEqual("precoUnitario", dto.getPrecoMax()));
        }

        if (dto.getPrecoMin() != null && dto.getPrecoMax() != null) {
            spec = spec.and(Specs.between("precoUnitario", dto.getPrecoMin(), dto.getPrecoMax()));
        }

        if (dto.getNomeCategoria() != null && !dto.getNomeCategoria().isBlank()) {
            spec = spec.and(Specs.like("categoria.nome", dto.getNomeCategoria()));
        }

        return spec;
    }
}
