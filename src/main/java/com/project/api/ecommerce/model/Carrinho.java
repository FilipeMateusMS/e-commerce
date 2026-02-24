package com.project.api.ecommerce.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Carrinho {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @OneToMany( mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true )
    private Set<CarrinhoItem> itensCarrinho = new HashSet<>(); ;

    @OneToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name= "usuario_id", nullable = false, unique = true )// Cada usuário pode ter somente um carrinho
    private Usuario usuario;

    public Carrinho(Long id, Set<CarrinhoItem> itensCarrinho) {
        this.id = id;
        this.itensCarrinho = itensCarrinho;
    }

    public Carrinho() {
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal valorSoma = BigDecimal.ZERO;
        for (CarrinhoItem item : itensCarrinho) {
            valorSoma = valorSoma.add( item.getProduto().getPrecoUnitario().multiply( BigDecimal.valueOf( item.getQuantidade() ) ) );
        }
        return valorSoma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CarrinhoItem> getItensCarrinho() {
        return itensCarrinho;
    }

    public void setItensCarrinho(Set<CarrinhoItem> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
