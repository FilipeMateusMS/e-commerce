package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Imagem;
import com.project.api.ecommerce.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImagemRepository extends JpaRepository<Imagem, Long>, JpaSpecificationExecutor<Imagem> {
}
