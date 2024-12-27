package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.model.Product;
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
        Product createdProduct = productService.createProduct(product, token);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")  // Only Admin can create a product
    public ResponseEntity<?> updateProduct(@PathVariable int id, @Valid @RequestBody Product productDetails, @RequestHeader("Authorization") String token) {
        // ToDo: Handle a reel image upload
        Product updatedProduct = productService.updateProduct(id, productDetails, token);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")  // Only Admin can create a product
    public ResponseEntity<?> deleteProduct(@PathVariable int id, @RequestHeader("Authorization") String token) {
        productService.deleteProduct(id, token);
        return ResponseEntity.ok("Product with id " + id + " deleted successfully");
    }
}