package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    public PedidoItem( Pedido pedido, Produto produto, int quantidade, BigDecimal precoVendaUnitario ) {
        this.quantidade = quantidade;
        this.precoVendaUnitario = precoVendaUnitario;
        this.pedido = pedido;
        this.produto = produto;
    }

    public BigDecimal calcularValorTotalItem(){
        return produto.getPrecoUnitario().multiply( new BigDecimal( quantidade ) );
    }
}
