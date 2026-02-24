package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findAllByUsuarioId( Long usuarioId, Pageable pageable );
}
