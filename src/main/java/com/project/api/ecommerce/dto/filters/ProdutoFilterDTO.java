package com.project.api.ecommerce.dto.filters;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // Utilizado pelo Spring cache para comparar a SpringKey
@ToString
public class ProdutoFilterDTO {
    private String nome;
    private String marca;

    @EqualsAndHashCode.Exclude // Não faz a comparação de descrição no Spring Cache
    @ToString.Exclude
    private String descricao;

    private BigDecimal precoMin;
    private BigDecimal precoMax;
    private String nomeCategoria;
}
