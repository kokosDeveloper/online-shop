package com.kokos.onlineshop.domain.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String title,
        String brand,
        String description,
        BigDecimal price,
        int inventory,
        Long categoryId
) {
}
