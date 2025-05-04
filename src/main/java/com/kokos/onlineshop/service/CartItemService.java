package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.CartItemRequest;
import com.kokos.onlineshop.domain.dto.CartItemResponse;
import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.domain.entity.Cart;
import com.kokos.onlineshop.domain.entity.CartItem;
import com.kokos.onlineshop.domain.entity.Product;
import com.kokos.onlineshop.domain.entity.User;
import com.kokos.onlineshop.exception.InsufficientStockException;
import com.kokos.onlineshop.repository.CartItemRepository;
import com.kokos.onlineshop.service.mapper.CartItemMapper;
import com.kokos.onlineshop.service.mapper.CartMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartService cartService;
    private final ProductService productService;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartItemResponse addItemToCart(CartItemRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(user.getId());
        Product product = productService.getProductById(request.productId());
        CartItem existingItemOrNew = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.productId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setProduct(product);
                    item.setQuantity(0);
                    item.setUnitPrice(product.getPrice());
                    cart.addItem(item);
                    return item;
                });

        int currentQuantity = existingItemOrNew.getQuantity();
        int newQuantity = currentQuantity + request.quantity();

        if (product.getInventory() < newQuantity)
            throw new InsufficientStockException("Not enough product in stock");

        existingItemOrNew.setQuantity(newQuantity);
        existingItemOrNew.setTotalPrice();
        cart.updateTotalAmount();
        cartItemRepository.save(existingItemOrNew);
        return cartItemMapper.toCartItemResponse(existingItemOrNew);
    }

    @Transactional
    public CartResponse deleteCartItemById(Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(user.getId());
        cart.removeItemById(cartItemId);
        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse updateItemQuantity(CartItemRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(user.getId());
        Product product = productService.getProductById(request.productId());
        CartItem forUpdate = cart.getCartItems().stream()
                .filter(ci -> request.productId().equals(ci.getProduct().getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No cart item with product id: " + request.productId()));
        if (product.getInventory() < request.quantity())
            throw new InsufficientStockException("Not enough product in stock");
        forUpdate.setQuantity(request.quantity());
        forUpdate.setTotalPrice();
        cart.updateTotalAmount();
        return cartMapper.toCartResponse(cart);
    }
}
