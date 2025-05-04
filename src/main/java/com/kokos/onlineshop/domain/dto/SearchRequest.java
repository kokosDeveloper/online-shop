package com.kokos.onlineshop.domain.dto;

public record SearchRequest(
        String title,
        String brand,
        String categoryTitle
) {
}
