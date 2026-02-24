package com.project.api.ecommerce.repository;

import com.jayway.jsonpath.JsonPath;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.model.Produto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {

    @Query("SELECT c FROM Categoria c WHERE LOWER( c.nome ) = LOWER( :nome )")
    Categoria findByNome( String nome );

    boolean existsByNome( String nome );

    boolean existsByNomeAndIdNot( String nome, Long id);

    Page<Categoria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
