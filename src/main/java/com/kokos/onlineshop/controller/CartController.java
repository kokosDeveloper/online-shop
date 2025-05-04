package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping
    public ResponseEntity<CartResponse> getUsersCart(
            Authentication authentication
    ){
        return ResponseEntity.ok(cartService.getUsersCartResponse(authentication));
    }

    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(
            Authentication authentication
    ){
        return ResponseEntity.ok(cartService.clearCart(authentication));
    }

}
