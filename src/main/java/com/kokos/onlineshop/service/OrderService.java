package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.OrderResponse;
import com.kokos.onlineshop.domain.dto.OrderItemResponse;
import com.kokos.onlineshop.domain.entity.*;
import com.kokos.onlineshop.repository.OrderRepository;
import com.kokos.onlineshop.service.business_rules.BusinessRuleEngine;
import com.kokos.onlineshop.service.business_rules.Facts;
import com.kokos.onlineshop.service.business_rules.Rule;
import com.kokos.onlineshop.service.business_rules.RuleBuilder;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final EmailService emailService;


    public OrderResponse createOrder(Authentication authentication) throws MessagingException {
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
        order.setTotalAmount(calculateTotalAmount(order));

        //business rules
        Facts facts = new Facts();
        BusinessRuleEngine ruleEngine = new BusinessRuleEngine(facts);
        facts.addFact("order", order);
        Rule rule = RuleBuilder
                .when(fact -> {
                    Order factOrder =(Order) facts.getFact("order");
                    BigDecimal totalAmount = factOrder.getTotalAmount();
                    return totalAmount.compareTo(new BigDecimal(5000)) > 0;
                }).then(fact -> {
                    Order factOrder =(Order) facts.getFact("order");
                    factOrder.setTotalAmount(factOrder.getTotalAmount().multiply(new BigDecimal(0.5)));
                });
        ruleEngine.addRule(rule);
        ruleEngine.run();
        orderRepository.save(order);
        cartService.clearCart(authentication);

        emailService.sendOrderConfirmation(user.getEmail(), user.getFullName(), EmailTemplateName.ORDER_CONFIRMATION, order);

        return toOrderResponse(order, user.getId());
    }
    private OrderResponse toOrderResponse(Order order, Long userId){
        return new OrderResponse(
                userId,
                order.getStatus(),
                order.getCreatedAt(),
                order.getTotalAmount(),
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

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        User user = userService.getUserById(userId);
        return toOrderResponses(new ArrayList<>(user.getOrders()), user.getId());
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("No order found with id: " + orderId));
        return toOrderResponse(order, order.getUser().getId());
    }
}
