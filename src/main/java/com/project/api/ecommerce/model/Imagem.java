package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.sql.Blob;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Imagem {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private String nome;

    @Column( nullable = false )
    private String fileType;

    @Column( nullable = false)
    private String descricao;

    @Column( nullable = false )
    private String storageKey;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "produto_id", nullable = false )
    private Produto produto;

//    public Imagem(Long id, String nome, String fileType, String descricao, Produto produto) {
//        this.id = id;
//        this.nome = nome;
//        this.fileType = fileType;
//        this.descricao = descricao;
//        this.produto = produto;
//    }
}
