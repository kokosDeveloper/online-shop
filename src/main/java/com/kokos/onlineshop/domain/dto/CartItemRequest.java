package com.kokos.onlineshop.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
        @NotNull(message = "Product id is required")
        Long productId,
        @Min(value = 1, message = "At least one product is required")
        int quantity
) {
}
