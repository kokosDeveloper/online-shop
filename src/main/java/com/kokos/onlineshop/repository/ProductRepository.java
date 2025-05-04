package com.kokos.onlineshop.repository;

import com.kokos.onlineshop.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByTitleAndBrand(String title, String brand);
    boolean existsByTitleAndIdNot(String title, Long id);
}
