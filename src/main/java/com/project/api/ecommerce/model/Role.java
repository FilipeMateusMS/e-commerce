package com.project.api.ecommerce.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
public class Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, length = 100, unique = true )
    private String nome;

    @ManyToMany( mappedBy = "roles" )
    private Collection<Usuario> usuarios = new HashSet<>();

    public Role() {
    }

    public Role(Long id, String nome, Collection<Usuario> usuarios) {
        this.id = id;
        this.nome = nome;
        this.usuarios = usuarios;
    }

    public Role(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Role( String nome) {
        this.nome = nome;
    }

    public Collection<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Collection<Usuario> usuarios) {
        this.usuarios = usuarios;
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
}
