package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.CartResponse;
import com.kokos.onlineshop.domain.entity.Cart;
import com.kokos.onlineshop.domain.entity.User;
import com.kokos.onlineshop.repository.CartRepository;
import com.kokos.onlineshop.service.mapper.CartMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    public Cart getCartByUserId(Long id) {
        return cartRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("No cart found with user id " + id));
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public CartResponse getUsersCartResponse(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart usersCart = getCartByUserId(user.getId());
        return cartMapper.toCartResponse(usersCart);
    }

    @Transactional
    public CartResponse clearCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart usersCart = getCartByUserId(user.getId());

        usersCart.getCartItems().clear();
        return cartMapper.toCartResponse(usersCart);
    }
}
