package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.domain.entity.Cart;
import com.kokos.onlineshop.domain.entity.CartItem;
import com.kokos.onlineshop.domain.entity.Product;
import com.kokos.onlineshop.domain.entity.User;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CartMapperTest {

    @Mock
    CartItemMapper cartItemMapper;
    @InjectMocks
    CartMapper cartMapper;

    @Test
    void shouldMapCartToCartResponse() {
        Product droneFPV = Product.builder()
                .id(1L)
                .price(new BigDecimal(500))
                .title("Cetus PRO")
                .build();
        Product gogglesFPV = Product.builder()
                .id(2L)
                .price(new BigDecimal(220))
                .title("Skyzone Cobra")
                .build();
        CartItem droneItem = CartItem.builder()
                .id(1L)
                .product(droneFPV)
                .quantity(5)
                .build();
        CartItemResponse droneItemResponse = new CartItemResponse
                (1L, 5,new BigDecimal(500), new BigDecimal(5*500), 1L, "Cetus PRO");
        CartItem gogglesItem = CartItem.builder()
                .id(2L)
                .product(gogglesFPV)
                .quantity(4)
                .build();
        CartItemResponse gogglesItemResponse = new CartItemResponse
                (2L, 4, new BigDecimal(220), new BigDecimal(220 * 4), 2L, "Skyzone Cobra");
        User user = User.builder()
                .id(1L)
                .build();
        Cart cart = Cart.builder()
                .id(1L)
                .user(user)
                .cartItems(Set.of(droneItem, gogglesItem))
                .build();
        when(cartItemMapper.toCartItemResponse(droneItem)).thenReturn(droneItemResponse);
        when(cartItemMapper.toCartItemResponse(gogglesItem)).thenReturn(gogglesItemResponse);

        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        assertEquals(cart.getId(), cartResponse.id());
        assertEquals(user.getId(), cartResponse.userId());
        assertEquals(new BigDecimal(5 * 500 + 4 * 220), cartResponse.totalAmount());
        Set<CartItemResponse> expectedItemResponseSet = Set.of(droneItemResponse, gogglesItemResponse);
        assertEquals(expectedItemResponseSet, cartResponse.cartItemResponses());
    }

}