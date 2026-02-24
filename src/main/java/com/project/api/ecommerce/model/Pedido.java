package com.project.api.ecommerce.model;

import com.project.api.ecommerce.enums.PedidoStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
/*
    Entidade que representa a finalização do compra do carrinho
    O usuário clica em "Finalizar Compra"
 */
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
    @JoinColumn( name= "usuario_id", nullable = false, updatable = false, foreignKey = @ForeignKey( name = "fk_pedido_usuario" ) )
    private Usuario usuario;

    public Pedido() {
    }

    public Pedido(Long id, LocalDate dataPedido, BigDecimal precoVendaTotal, PedidoStatus pedidoStatus, Set<PedidoItem> itensPedidos, Usuario usuario) {
        this.id = id;
        this.dataPedido = dataPedido;
        this.precoVendaTotal = precoVendaTotal;
        this.pedidoStatus = pedidoStatus;
        this.itensPedidos = itensPedidos;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getPrecoVendaTotal() {
        return precoVendaTotal;
    }

    public void setPrecoVendaTotal(BigDecimal precoVendaTotal) {
        this.precoVendaTotal = precoVendaTotal;
    }

    public PedidoStatus getPedidoStatus() {
        return pedidoStatus;
    }

    public void setPedidoStatus(PedidoStatus pedidoStatus) {
        this.pedidoStatus = pedidoStatus;
    }

    public Set<PedidoItem> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(Set<PedidoItem> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
