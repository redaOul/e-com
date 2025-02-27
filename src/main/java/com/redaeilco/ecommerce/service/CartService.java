package com.redaeilco.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.redaeilco.ecommerce.dto.UpdateCartItemRequest;
import com.redaeilco.ecommerce.dto.CartItemResponse;
import com.redaeilco.ecommerce.dto.CartResponse;
import com.redaeilco.ecommerce.model.Cart;
import com.redaeilco.ecommerce.model.CartItem;
import com.redaeilco.ecommerce.model.Product;
import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.CartRepository;
import com.redaeilco.ecommerce.repository.ProductRepository;
import com.redaeilco.ecommerce.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

	@Autowired
    private CartRepository cartRepository;

	@Autowired
    private ProductRepository productRepository;

	@Autowired
    private UserRepository userRepository;
    
    public CartResponse getCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getQuantity(),
                        item.getQuantity() * item.getProduct().getPrice()))
                .collect(Collectors.toList());

        double totalPrice = items.stream().mapToDouble(CartItemResponse::getPrice).sum();

        return new CartResponse(cart.getId(), items, totalPrice);
    }

    public CartResponse addToCart(int userId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == request.getProductId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem(cart, product, request.getQuantity());
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return getCart(userId);
    }

    public CartResponse removeFromCart(int userId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == request.getProductId())
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (request.getQuantity() == -1 || item.getQuantity() <= request.getQuantity()) {
                cart.getItems().remove(item);
            } else {
                item.setQuantity(item.getQuantity() - request.getQuantity());
            }
        } else {
            throw new RuntimeException("Product not found in cart");
        }

        cartRepository.save(cart);
        return getCart(userId);
    }

    public CartResponse removeFromCart(int userId, int productId) {
        UpdateCartItemRequest request = new UpdateCartItemRequest(productId, -1);
        return removeFromCart(userId, request);
    }

    private Cart createCartForUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = new Cart(user, new ArrayList<>());
        return cartRepository.save(cart);
    }
}
