package com.project.api.ecommerce.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "carrinho_item",
        // Não pode ter o mesmo produto mais de uma vez no mesmo carrinho
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "carrinho_id", "produto_id" }
        )
)
public class CarrinhoItem {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private int quantidade;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name="produto_id", nullable = false )
    private Produto produto;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name="carrinho_id", nullable = false )
    private Carrinho carrinho;

    public CarrinhoItem() {
    }

    public CarrinhoItem(Carrinho carrinho, Produto produto, int quantidade) {
        this.carrinho = carrinho;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public BigDecimal calcularPrecoItem() {
        return produto.getPrecoUnitario().multiply( new BigDecimal( quantidade ) );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}
