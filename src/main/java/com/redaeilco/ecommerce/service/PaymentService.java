package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.enums.OrderStatus;
import com.redaeilco.ecommerce.enums.PaymentMethod;
import com.redaeilco.ecommerce.enums.PaymentStatus;
import com.redaeilco.ecommerce.model.Order;
import com.redaeilco.ecommerce.model.Payment;
import com.redaeilco.ecommerce.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    @Autowired
    private OrderRepository orderRepository;

    public void processPayment(int orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(paymentMethod);
        payment.setStatus(PaymentStatus.SUCCESS);
        
        order.setPayment(payment);
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }
}
