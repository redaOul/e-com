package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.dto.OrderResponse;
import com.redaeilco.ecommerce.service.JWTService;
import com.redaeilco.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/place/{cartId}")
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("Authorization") String token, @PathVariable int cartId) {
        int userId = extractUserIdFromToken(token);
        System.out.println(cartId);
        return ResponseEntity.ok(orderService.placeOrder(userId, cartId));
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
    public ResponseEntity<OrderResponse> applyDiscount(@RequestHeader("Authorization") String token, @PathVariable int orderId, @RequestBody Map<String, String> request) {
        String voucherCode = request.get("voucherCode");
        System.out.println(voucherCode);
        System.out.println(orderId);
        return ResponseEntity.ok(orderService.applyDiscount(orderId, voucherCode));
    }

    private int extractUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }
}
