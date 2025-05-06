package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.OrderResponse;
import com.kokos.onlineshop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
