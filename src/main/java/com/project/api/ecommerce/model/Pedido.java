package com.project.api.ecommerce.model;

import com.project.api.ecommerce.model.enums.PedidoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
/*
    Entidade que representa a finalização do compra do carrinho
    O usuário clica em "Finalizar Compra"
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Pedido {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "data_pedido", nullable = false, updatable = false)
    private LocalDate dataPedido;

    @Column(name = "preco_venda_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoVendaTotal;

    @Enumerated( EnumType.STRING )
    @Column(name = "status", nullable = false, length = 30)
    private PedidoStatus pedidoStatus;

    @OneToMany( mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true )
    private Set<PedidoItem> itensPedidos = new HashSet<>();

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name= "usuario_id", nullable = false, updatable = false,
            foreignKey = @ForeignKey( name = "fk_pedido_usuario" ) )
    private Usuario usuario;

    public BigDecimal calcularPrecoTotal() {
        BigDecimal valorSoma = BigDecimal.ZERO;
        for (PedidoItem item : itensPedidos) {
            valorSoma = valorSoma.add(item.getPrecoVendaUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return valorSoma;
    }
}
