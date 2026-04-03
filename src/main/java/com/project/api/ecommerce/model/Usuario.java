package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column( nullable = false, length = 30 )
    private String senha;

    @Column( nullable = false, length = 120 )
    private String telefone;

    @OneToOne( mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true )
    private Carrinho carrinho;

    @OneToMany( mappedBy = "usuario", cascade = CascadeType.ALL )
    private List<Pedido> pedidos;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "user_roles",
            joinColumns= @JoinColumn( name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn( name="role_id", referencedColumnName = "id" ) )
    private List<Role> roles = new ArrayList<>();
}
