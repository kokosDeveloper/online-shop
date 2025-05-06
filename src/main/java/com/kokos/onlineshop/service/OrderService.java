package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.OrderResponse;
import com.kokos.onlineshop.domain.dto.OrderItemResponse;
import com.kokos.onlineshop.domain.entity.*;
import com.kokos.onlineshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderRepository orderRepository;


    public OrderResponse createOrder(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(user.getId());

        if (cart.getCartItems().isEmpty()){
            throw new IllegalStateException("No items in cart");
        }
        Order order = Order.builder()
                .user(user)
                .status(Order.Status.PREPARING)
                .build();

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    int newInventory = product.getInventory() - cartItem.getQuantity();
                    productService.updateProductInventory(product, newInventory);
                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(cartItem.getQuantity())
                            .unitPrice(product.getPrice())
                            .build();
                }).collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartService.clearCart(authentication);
        return toOrderResponse(order, user.getId());
    }
    private OrderResponse toOrderResponse(Order order, Long userId){
        return new OrderResponse(
                userId,
                order.getStatus(),
                order.getCreatedAt(),
                calculateTotalAmount(order),
                toOrderItemResponses(order.getOrderItems())
        );
    }
    private List<OrderResponse> toOrderResponses(List<Order> orders, Long userId){
        return orders.stream()
                .map(order -> toOrderResponse(order, userId))
                .collect(Collectors.toList());
    }

    private Set<OrderItemResponse> toOrderItemResponses(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toSet());
    }

    private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getTitle(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity()))
        );
    }

    private BigDecimal calculateTotalAmount(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem ->
                        orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<OrderResponse> getAllUsersOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> userOrders = orderRepository.findAllByUserId(user.getId());
        return toOrderResponses(userOrders, user.getId());
    }
}
