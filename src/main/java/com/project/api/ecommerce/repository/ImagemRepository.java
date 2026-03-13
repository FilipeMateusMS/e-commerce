package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Imagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ImagemRepository extends JpaRepository<Imagem, Long>, JpaSpecificationExecutor<Imagem> {

    Page<Imagem> findAllByProdutoId(Long idProduto, Pageable pageable);
}
