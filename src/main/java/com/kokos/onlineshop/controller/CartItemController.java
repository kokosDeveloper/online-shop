package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.CartItemRequest;
import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;
    @PostMapping
    public ResponseEntity<CartItemResponse> addItemToCart(
            @Valid @RequestBody CartItemRequest request,
            Authentication authentication
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartItemService.addItemToCart(request, authentication));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> deleteCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication
    ){
        return ResponseEntity.ok(cartItemService.deleteCartItemById(cartItemId, authentication));
    }

    @PatchMapping
    public ResponseEntity<CartResponse> updateItemQuantity(
            @Valid @RequestBody CartItemRequest request,
            Authentication authentication
    ){
        return ResponseEntity.ok(cartItemService.updateItemQuantity(request, authentication));
    }
}
