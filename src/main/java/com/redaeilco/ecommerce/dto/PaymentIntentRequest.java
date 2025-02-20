package com.redaeilco.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentIntentRequest {
    private int orderId;
    private String cardOwner;
}