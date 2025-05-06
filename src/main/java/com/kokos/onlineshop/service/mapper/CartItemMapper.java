package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.entity.CartItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemMapper {
    public CartItemResponse toCartItemResponse(CartItem cartItem){
        BigDecimal unitPrice = cartItem.getProduct().getPrice();
        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(cartItem.getQuantity()));
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                unitPrice,
                totalPrice,
                cartItem.getProduct().getId(),
                cartItem.getProduct().getTitle()
        );
    }
}
