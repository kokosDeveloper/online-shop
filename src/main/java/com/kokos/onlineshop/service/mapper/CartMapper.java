package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.domain.entity.Cart;
import com.kokos.onlineshop.domain.entity.CartItem;
import com.kokos.onlineshop.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartMapper {
    private final CartItemMapper cartItemMapper;
    public CartResponse toCartResponse(Cart usersCart) {
        Set<CartItemResponse> itemResponses = usersCart.getCartItems().stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toSet());
        BigDecimal cartTotalAmount = itemResponses.stream()
                .map(CartItemResponse::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(
                usersCart.getId(),
                usersCart.getUser().getId(),
                cartTotalAmount,
                itemResponses
        );
    }
}
