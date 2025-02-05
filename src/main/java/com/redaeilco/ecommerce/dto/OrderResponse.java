package com.redaeilco.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

import com.redaeilco.ecommerce.enums.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private int orderId;
    private OrderStatus status;
    private double totalPrice;
    private double discount;
    private List<OrderItemResponse> items;
}