package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, length = 120 )
    private String nome;

    @NaturalId
    @Column( nullable = false, length = 120 )
    private String email;

    @Column( nullable = false, length = 120 )
    private String senha;

    @Column( nullable = false, length = 120 )
    private String telefone;

    @OneToOne( mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true )
    private Carrinho carrinho;

    @OneToMany( mappedBy = "usuario", cascade = CascadeType.ALL )
    private List<Pedido> pedidos;

    @ManyToMany( fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinTable(
            name = "user_roles",
            joinColumns= @JoinColumn( name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn( name="role_id", referencedColumnName = "id" ) )
    private List<Role> roles = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, Carrinho carrinho, List<Pedido> pedidos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.carrinho = carrinho;
        this.pedidos = pedidos;
    }

    public Usuario(Long id, String nome, String email, String senha, String telefone, Carrinho carrinho, List<Pedido> pedidos, List<Role> roles) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.carrinho = carrinho;
        this.pedidos = pedidos;
        this.roles = roles;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
