package com.kokos.onlineshop.repository;

import com.kokos.onlineshop.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
}
