package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Produto {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, unique = true, length = 120 )
    private String nome;

    @Column( nullable = false, length = 120 )
    private String marca;

    @Column( nullable = false )
    private String descricao;

    @Column( nullable = false, precision = 12, scale = 2 )
    private BigDecimal precoUnitario;

    @Column( nullable = false )
    private Integer quantidade;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "categoria_id" )
    private Categoria categoria;

    @OneToMany(
            mappedBy = "produto",
            cascade = CascadeType.ALL,
            orphanRemoval = true // A exclusão de um produto remove todas as suas imagens.
    )
    private List<Imagem> imagens = new ArrayList<>();;

    public Produto(String nome, String marca, String descricao, BigDecimal precoUnitario, Integer quantidade, Categoria categoria) {
        this.nome = nome;
        this.marca = marca;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }
}
