package com.kokos.onlineshop.domain.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartResponse(
        Long id,
        Long userId,
        BigDecimal totalAmount,
        Set<CartItemResponse> cartItemResponses
) {
}
