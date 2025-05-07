package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.OrderResponse;
import com.kokos.onlineshop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(authentication));
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllUsersOrders(
            Authentication authentication
    ){
        return ResponseEntity.ok(orderService.getAllUsersOrders(authentication));
    }

    @GetMapping("/{order-id}")
    @PreAuthorize("hasAuthority('getUserOrders')")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable("order-id") Long orderId
    ){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
    @GetMapping("/user/{user-id}")
    @PreAuthorize("hasAuthority('getUserOrders')")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByUserId(
            @PathVariable("user-id") Long userId
    ){
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}
