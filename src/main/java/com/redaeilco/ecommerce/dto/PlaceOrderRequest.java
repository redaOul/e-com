package com.redaeilco.ecommerce.dto;

import com.redaeilco.ecommerce.enums.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderRequest {
    private PaymentMethod method;
    private String promoCode;
}