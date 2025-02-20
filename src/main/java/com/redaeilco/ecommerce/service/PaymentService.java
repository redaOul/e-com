package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.enums.OrderStatus;
import com.redaeilco.ecommerce.enums.PaymentMethod;
import com.redaeilco.ecommerce.enums.PaymentStatus;
import com.redaeilco.ecommerce.model.Order;
import com.redaeilco.ecommerce.model.Payment;
import com.redaeilco.ecommerce.repository.OrderRepository;
import com.redaeilco.ecommerce.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private ProductService ProductService;
    
    public PaymentIntent initiatePayment(int orderId, String cardOwner) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        double amount = (order.getDiscount() != null) 
            ? order.getDiscount().calculateDiscount(order.getTotalPrice()) 
            : order.getTotalPrice();

        Payment existingPayment = paymentRepository.findByOrderAndStatus(order, PaymentStatus.PENDING).orElse(null);
        PaymentIntent intent;

        if (existingPayment != null) {
            intent = stripeService.updatePaymentIntent(existingPayment.getStripePaymentId(), amount, "usd");
            existingPayment.setAmount(amount);
            existingPayment.setCardOwner(cardOwner);
            paymentRepository.save(existingPayment);
        } else {
            intent = stripeService.createPaymentIntent(amount, "usd");
            Payment payment = new Payment();
            payment.setStripePaymentId(intent.getId());
            payment.setCardOwner(cardOwner);
            payment.setAmount(amount);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setMethod(PaymentMethod.CARD);
            payment.setOrder(order);
            paymentRepository.save(payment);
        }
        
        return intent;
    }

    public void confirmPayment(String paymentIntentId) {
        Payment payment = paymentRepository.findByStripePaymentId(paymentIntentId)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
            
        payment.setStatus(PaymentStatus.SUCCESS);

        
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.COMPLETED);
        
        order.getItems().forEach(
            item -> ProductService.reduceProductQuantity(
                item.getProduct().getId(), item.getQuantity()
            )
        );

        paymentRepository.save(payment);
        orderRepository.save(order);
    }
}


