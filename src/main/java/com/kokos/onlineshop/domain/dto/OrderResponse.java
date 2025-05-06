package com.kokos.onlineshop.domain.dto;

import com.kokos.onlineshop.domain.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponse(
        Long userId,
        Order.Status status,
        LocalDateTime createdAt,
        BigDecimal totalAmount,
        Set<OrderItemResponse> orderItemResponses
) {
}
