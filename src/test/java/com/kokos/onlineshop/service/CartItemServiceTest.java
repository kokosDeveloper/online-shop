package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.CartItemRequest;
import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.entity.Cart;
import com.kokos.onlineshop.domain.entity.CartItem;
import com.kokos.onlineshop.domain.entity.Product;
import com.kokos.onlineshop.domain.entity.User;
import com.kokos.onlineshop.exception.InsufficientStockException;
import com.kokos.onlineshop.repository.CartItemRepository;
import com.kokos.onlineshop.service.mapper.CartItemMapper;
import com.kokos.onlineshop.service.mapper.CartMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    @Mock
    private CartService cartService;
    @Mock
    private ProductService productService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartMapper cartMapper;

    private CartItemMapper cartItemMapper = new CartItemMapper();

    private CartItemService cartItemService;
    @BeforeEach
    void setUp(){
        cartItemService = new CartItemService(cartService, productService, cartItemMapper, cartItemRepository, cartMapper);
    }
    @Test
    void   shouldAddItemInCartIfAlreadyExistsInCartAndEnoughInventory(){
        //Given
        User mockUser = User.builder()
                .id(1L)
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 5);

        Product product = Product.builder()
                .id(1L)
                .inventory(10)
                .title("Test product")
                .price(new BigDecimal(100))
                .build();
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .build();
        Cart cart = Cart.builder()
                .cartItems(Set.of(cartItem))
                .build();

        //Mock the calls
        when(cartService.getCartByUserId(mockUser.getId())).thenReturn(cart);
        when(productService.getProductById(cartItemRequest.productId())).thenReturn(product);

        //When
        CartItemResponse actualResponse = cartItemService.addItemToCart(cartItemRequest, authentication);

        //Then
        assertEquals(1L, actualResponse.id());
        assertEquals(7, actualResponse.quantity());
        assertEquals(new BigDecimal(100), actualResponse.unitPrice());
        assertEquals(new BigDecimal(7*100), actualResponse.totalPrice());
        assertEquals(1L, actualResponse.productId());
        assertEquals("Test product", actualResponse.productName());

        verify(cartService, times(1)).getCartByUserId(1L);
        verify(productService, times(1)).getProductById(1L);
        verify(cartItemRepository, times(1)).save(cartItem);
    }
    @Test
    void shouldAddItemInCartIfNotExistsInCartAndEnoughInventory(){
        User mockUser = User.builder()
                .id(1L)
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 5);

        Product product = Product.builder()
                .id(1L)
                .inventory(10)
                .title("Test product")
                .price(new BigDecimal(100))
                .build();
        Cart cart = Cart.builder()
                .cartItems(new HashSet<>())
                .build();

        when(cartService.getCartByUserId(mockUser.getId())).thenReturn(cart);
        when(productService.getProductById(cartItemRequest.productId())).thenReturn(product);
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(
                invocation -> {
                    CartItem cartItem = invocation.getArgument(0);
                    cartItem.setId(1L);
                    return cartItem;
                });

        CartItemResponse actualResponse = cartItemService.addItemToCart(cartItemRequest, authentication);

        //Then
        assertEquals(1L, actualResponse.id());
        assertEquals(5, actualResponse.quantity());
        assertEquals(new BigDecimal(100), actualResponse.unitPrice());
        assertEquals(new BigDecimal(5*100), actualResponse.totalPrice());
        assertEquals(1L, actualResponse.productId());
        assertEquals("Test product", actualResponse.productName());

        verify(cartService, times(1)).getCartByUserId(1L);
        verify(productService, times(1)).getProductById(1L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));


    }
    @Test
    void shouldNotAddItemInCartIfAlreadyExistsInCartAndNotEnoughInventory(){
        //Given
        User mockUser = User.builder()
                .id(1L)
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 5);

        Product product = Product.builder()
                .id(1L)
                .inventory(10)
                .title("Test product")
                .price(new BigDecimal(100))
                .build();
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(7)
                .build();
        Cart cart = Cart.builder()
                .cartItems(Set.of(cartItem))
                .build();

        //Mock the calls
        when(cartService.getCartByUserId(mockUser.getId())).thenReturn(cart);
        when(productService.getProductById(cartItemRequest.productId())).thenReturn(product);


        //Then
        assertThrows(InsufficientStockException.class,
                () -> cartItemService.addItemToCart(cartItemRequest, authentication));
    }
    @Test
    void shouldNotAddItemInCartIfNotExistsInCartAndNotEnoughInventory(){
        User mockUser = User.builder()
                .id(1L)
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 11);

        Product product = Product.builder()
                .id(1L)
                .inventory(10)
                .title("Test product")
                .price(new BigDecimal(100))
                .build();
        Cart cart = Cart.builder()
                .cartItems(new HashSet<>())
                .build();

        when(cartService.getCartByUserId(mockUser.getId())).thenReturn(cart);
        when(productService.getProductById(cartItemRequest.productId())).thenReturn(product);

        //Then
        assertThrows(InsufficientStockException.class,
                () -> cartItemService.addItemToCart(cartItemRequest, authentication));
    }

}