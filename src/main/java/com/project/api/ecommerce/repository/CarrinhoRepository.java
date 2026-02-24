package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByUsuarioId( Long usuarioId );
}
