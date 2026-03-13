package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Carrinho {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @OneToMany( mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true )
    private Set<CarrinhoItem> itensCarrinho = new HashSet<>(); ;

    @OneToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name= "usuario_id", nullable = false, unique = true )// Cada usuário pode ter somente um carrinho
    private Usuario usuario;

    public BigDecimal calcularValorTotal() {
        BigDecimal valorSoma = BigDecimal.ZERO;
        for (CarrinhoItem item : itensCarrinho) {
            valorSoma = valorSoma.add( item.getProduto().getPrecoUnitario().multiply( BigDecimal.valueOf( item.getQuantidade() ) ) );
        }
        return valorSoma;
    }
}
