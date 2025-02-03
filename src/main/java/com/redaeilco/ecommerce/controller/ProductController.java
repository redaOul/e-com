package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.model.Product;
import com.redaeilco.ecommerce.service.JWTService;
import com.redaeilco.ecommerce.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JWTService jwtService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")  // Only Admin can create a product
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, @RequestHeader("Authorization") String token) {
        // ToDo: Handle a reel image upload
        int userId = jwtService.extractUserId(token);
        Product createdProduct = productService.createProduct(product, userId);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")  // Only Admin can create a product
    public ResponseEntity<?> updateProduct(@PathVariable int id, @Valid @RequestBody Product productDetails, @RequestHeader("Authorization") String token) {
        // ToDo: Handle a reel image upload
        int userId = jwtService.extractUserId(token);
        Product updatedProduct = productService.updateProduct(id, productDetails, userId);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")  // Only Admin can create a product
    public ResponseEntity<?> deleteProduct(@PathVariable int id, @RequestHeader("Authorization") String token) {
        int userId = jwtService.extractUserId(token);
        productService.deleteProduct(id, userId);
        return ResponseEntity.ok("Product with id " + id + " deleted successfully");
    }
}