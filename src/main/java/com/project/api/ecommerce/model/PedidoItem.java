package com.project.api.ecommerce.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
public class PedidoItem {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private int quantidade;

    @Column( nullable = false, precision = 12, scale = 2 )
    private BigDecimal precoVendaUnitario;

    @ManyToOne( optional = false )
    @JoinColumn( name = "pedido_id", nullable = false )
    private Pedido pedido;

    @ManyToOne( optional = false )
    @JoinColumn( name = "produto_id", nullable = false )
    private Produto produto;

    public PedidoItem() {
    }

    public PedidoItem( Pedido pedido, Produto produto, int quantidade, BigDecimal precoVendaUnitario ) {
        this.quantidade = quantidade;
        this.precoVendaUnitario = precoVendaUnitario;
        this.pedido = pedido;
        this.produto = produto;
    }

    public BigDecimal calcularValorTotalItem(){
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

    public BigDecimal getPrecoVendaUnitario() {
        return precoVendaUnitario;
    }

    public void setPrecoVendaUnitario(BigDecimal precoVendaUnitario) {
        this.precoVendaUnitario = precoVendaUnitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
