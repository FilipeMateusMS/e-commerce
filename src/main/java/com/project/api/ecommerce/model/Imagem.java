package com.project.api.ecommerce.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.sql.Blob;

@Entity
public class Imagem {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private String fileName;

    @Column( nullable = false )
    private String fileType;

    @Column( nullable = false, unique = true )
    private String storageKey; // caminho local ou key do S3

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "produto_id", nullable = false )
    private Produto produto;

    public Imagem() {
    }

    public Imagem( String fileName, String fileType, String storageKey, Produto produto) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.storageKey = storageKey;
        this.produto = produto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
