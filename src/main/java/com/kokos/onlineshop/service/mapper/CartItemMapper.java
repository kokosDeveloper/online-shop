package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.entity.CartItem;
import org.springframework.stereotype.Service;

@Service
public class CartItemMapper {
    public CartItemResponse toCartItemResponse(CartItem cartItem){
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getTotalPrice(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getTitle()
        );
    }
}
