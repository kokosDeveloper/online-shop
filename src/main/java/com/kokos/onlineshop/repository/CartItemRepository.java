package com.kokos.onlineshop.repository;

import com.kokos.onlineshop.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
