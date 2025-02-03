package com.redaeilco.ecommerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private double totalPrice;
}