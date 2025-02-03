package com.redaeilco.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemResponse {
    private int productId;
    private String productName;
    private int quantity;
    private double price;
}

