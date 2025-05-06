package com.kokos.onlineshop.domain.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long orderId,
        Long productId,
        String productTitle,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
