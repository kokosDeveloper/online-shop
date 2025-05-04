package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.domain.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return new CartResponse(
                usersCart.getId(),
                usersCart.getUser().getId(),
                usersCart.getTotalAmount(),
                itemResponses
        );
    }
}
