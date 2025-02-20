package com.redaeilco.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redaeilco.ecommerce.enums.PaymentStatus;
import com.redaeilco.ecommerce.model.Order;
import com.redaeilco.ecommerce.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByStripePaymentId(String stripePaymentId);
    Optional<Payment> findByOrderAndStatus(Order order, PaymentStatus status);
}