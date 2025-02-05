package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.dto.PlaceOrderRequest;
import com.redaeilco.ecommerce.enums.PaymentMethod;
import com.redaeilco.ecommerce.dto.OrderResponse;
import com.redaeilco.ecommerce.service.JWTService;
import com.redaeilco.ecommerce.service.OrderService;
import com.redaeilco.ecommerce.service.PaymentService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private PaymentService paymentService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("Authorization") String token, @RequestBody PlaceOrderRequest request) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(orderService.placeOrder(userId/*, request */));
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getOrderHistory(@RequestHeader("Authorization") String token) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@RequestHeader("Authorization") String token, @PathVariable int orderId) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(orderService.getOrder(userId, orderId));
    }

    // check typed discount
    @PostMapping("/{orderId}/apply-discount")
    public ResponseEntity<OrderResponse> applyDiscount(@RequestHeader("Authorization") String token, @PathVariable int orderId, @RequestBody String request) {
        return ResponseEntity.ok(orderService.applyDiscount(orderId, request));
    }

    @PostMapping("/pay")
    public ResponseEntity<String> payOrder(@RequestParam int orderId, @RequestParam PaymentMethod paymentMethod) {
        paymentService.processPayment(orderId, paymentMethod);
        return ResponseEntity.ok("Payment successful, order completed");
    }

    private int extractUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }
}
