package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.dto.AddToCartRequest;
import com.redaeilco.ecommerce.dto.CartResponse;
import com.redaeilco.ecommerce.service.CartService;
import com.redaeilco.ecommerce.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;

    @Autowired
    private JWTService jwtService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestHeader("Authorization") String token) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestHeader("Authorization") String token, @RequestBody AddToCartRequest request) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @DeleteMapping("/remove/{productId}/{quantity}")
    public ResponseEntity<CartResponse> removeFromCart(@RequestHeader("Authorization") String token, @PathVariable int productId, @PathVariable int quantity) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId, quantity));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(@RequestHeader("Authorization") String token, @PathVariable int productId) {
        int userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    private int extractUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }

    
}
