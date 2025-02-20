package com.redaeilco.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }
    
    public PaymentIntent createPaymentIntent(double amount, String currency) {
        try {

            System.out.println("Creating payment intent for amount: " + amount + " " + currency);
            
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long)(amount*100))
                .setCurrency(currency)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();

            PaymentIntent intent = PaymentIntent.create(params);
            return intent;
        } catch (StripeException e) {
            throw new RuntimeException("Error creating payment intent", e);
        }
    }

    public PaymentIntent updatePaymentIntent(String paymentIntentId, double amount, String currency) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntentUpdateParams params = PaymentIntentUpdateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency(currency)
                .build();
            return paymentIntent.update(params);
        } catch (StripeException e) {
            throw new RuntimeException("Error updating payment intent", e);
        }
    }
}
