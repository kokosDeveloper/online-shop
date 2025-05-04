package com.kokos.onlineshop.domain.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        Long productId,
        String productName
) {
}
