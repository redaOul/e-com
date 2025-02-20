package com.redaeilco.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.redaeilco.ecommerce.enums.OrderStatus;
import com.redaeilco.ecommerce.model.Cart;
import com.redaeilco.ecommerce.model.Discount;
import com.redaeilco.ecommerce.model.Order;
import com.redaeilco.ecommerce.model.OrderItem;
import com.redaeilco.ecommerce.repository.CartRepository;
import com.redaeilco.ecommerce.repository.DiscountRepository;
import com.redaeilco.ecommerce.repository.OrderRepository;

import com.redaeilco.ecommerce.dto.OrderResponse;
import com.redaeilco.ecommerce.dto.OrderItemResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscountRepository discountRepository;

    public OrderResponse placeOrder(int userId, int cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getItems().stream()
            .map(cartItem -> new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice()))
            .collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotalPrice(orderItems.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum());

        orderRepository.save(order);
        cartRepository.delete(cart);

        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalPrice(), 0,
            orderItems.stream()
                .map(item -> new OrderItemResponse(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getImageUrl(),
                    item.getQuantity(),
                    item.getPrice()))
                .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrderHistory(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(order -> new OrderResponse(order.getId(), order.getStatus(), order.getTotalPrice(), 
                order.getDiscount() != null ? order.getDiscount().getPercentage() : 0,
                order.getItems().stream()
                    .map(item -> new OrderItemResponse(
                        item.getProduct().getId(), 
                        item.getProduct().getName(), 
                        item.getProduct().getImageUrl(),
                        item.getQuantity(), 
                        item.getPrice()))
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());
    }

    public OrderResponse getOrder(int userId, int orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalPrice(),
            order.getDiscount() != null ? order.getDiscount().getPercentage() : 0,
            order.getItems().stream()
                .map(item -> new OrderItemResponse(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getImageUrl(),
                    item.getQuantity(),
                    item.getPrice()))
                .collect(Collectors.toList()));
    }

    public OrderResponse applyDiscount(int orderId, String discountCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    
        Discount discount = discountRepository.findByCode(discountCode)
                .orElseThrow(() -> new RuntimeException("Invalid Discount Code"));
    
        if (!discount.isValid()) {
            throw new RuntimeException("Invalid Discount Code");
        }

        order.setDiscount(discount);
        orderRepository.save(order);

        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalPrice(),
            discount.getPercentage(),
            order.getItems().stream()
                .map(item -> new OrderItemResponse(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getImageUrl(),
                    item.getQuantity(),
                    item.getPrice()))
                .collect(Collectors.toList()));
    }

    // public void updateOrderStatus(int orderId, OrderStatus status) {
    //     Order order = orderRepository.findById(orderId)
    //             .orElseThrow(() -> new RuntimeException("Order not found"));
    //     order.setStatus(status);
    //     orderRepository.save(order);
    // }
}