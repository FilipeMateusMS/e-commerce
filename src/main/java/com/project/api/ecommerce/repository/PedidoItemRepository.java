package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
