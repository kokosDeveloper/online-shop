package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.entity.CartItem;
import com.kokos.onlineshop.domain.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartItemMapperTest {

    @Test
    void shouldMapCartItemToCartItemResponse(){
        CartItemMapper cartItemMapper = new CartItemMapper();
        Product droneFPV = Product.builder()
                .price(new BigDecimal(500))
                .title("Cetus PRO")
                .build();
        CartItem droneItem = CartItem.builder()
                .id(1L)
                .product(droneFPV)
                .quantity(5)
                .build();

        CartItemResponse response = cartItemMapper.toCartItemResponse(droneItem);

        assertEquals(droneItem.getId(), response.id());
        assertEquals(droneItem.getQuantity(), response.quantity());
        assertEquals(droneFPV.getPrice(), response.unitPrice());
        assertEquals(new BigDecimal(2500), response.totalPrice());
        assertEquals(droneFPV.getId(), response.productId());
        assertEquals(droneFPV.getTitle(), response.productName());
    }
}