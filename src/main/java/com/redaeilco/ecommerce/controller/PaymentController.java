package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.dto.PaymentConfirmationRequest;
import com.redaeilco.ecommerce.dto.PaymentIntentRequest;
import com.redaeilco.ecommerce.service.PaymentService;
import com.stripe.model.PaymentIntent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody PaymentIntentRequest request) {

        System.out.println("create payment " + request.getOrderId() + " " + request.getCardOwner());
        
        PaymentIntent intent = paymentService.initiatePayment(request.getOrderId(), request.getCardOwner());
        
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(@RequestBody PaymentConfirmationRequest request) {
        paymentService.confirmPayment(request.getPaymentIntentId());
        return ResponseEntity.ok().build();
    }
}