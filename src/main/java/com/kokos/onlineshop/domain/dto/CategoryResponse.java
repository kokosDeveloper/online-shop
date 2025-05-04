package com.kokos.onlineshop.domain.dto;

public record CategoryResponse(
        Long id,
        String title,
        int productsCount
) {
}
