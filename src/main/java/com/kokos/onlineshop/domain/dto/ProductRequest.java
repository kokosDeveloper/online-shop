package com.kokos.onlineshop.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.sql.Update;

import java.math.BigDecimal;

public record ProductRequest(
        @NotNull(groups = {Update.class}, message = "Product id is required for update")
        Long id,
        @NotBlank(message = "Product title is required")
        String title,
        @NotBlank(message = "Product brand is required")
        String brand,
        String description,
        @NotNull(message = "Product price is required")
        BigDecimal price,
        @NotNull(message = "Product inventory is required")
        int inventory,
        @NotNull(message = "Category id is required")
        Long categoryId
) {
}
