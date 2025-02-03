package com.redaeilco.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddToCartRequest {
    private int productId;
    private int quantity;
}
