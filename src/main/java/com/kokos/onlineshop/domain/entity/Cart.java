package com.kokos.onlineshop.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<CartItem> cartItems = new HashSet<>();

    public void addItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItemById(Long cartItemId){
        cartItems.removeIf(cartItem -> cartItemId.equals(cartItem.getId()));
        updateTotalAmount();
    }
    public void updateTotalAmount() {
        totalAmount = cartItems.stream()
                .map(item -> {
                    return item.getUnitPrice() == null
                            ? BigDecimal.ZERO
                            : item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
