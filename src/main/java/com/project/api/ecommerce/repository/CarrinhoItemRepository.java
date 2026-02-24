package com.project.api.ecommerce.repository;

import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.model.CarrinhoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Long> {
    void deleteAllByCarrinhoId(Long id);
}
