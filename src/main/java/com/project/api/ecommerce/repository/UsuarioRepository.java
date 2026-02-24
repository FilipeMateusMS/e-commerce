package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByNome(String nome);
    Optional<Usuario> findByEmail(String email );
}
