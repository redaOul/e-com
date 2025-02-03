package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.model.Product;
import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.ProductRepository;
import com.redaeilco.ecommerce.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        // If the product is not found, throw an exception and catch it in the controller
        return productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    public Product createProduct(Product product, int userId) {
        User user = userRepository.findById(userId).get();
        product.setCreatedBy(user);
        return productRepository.save(product);
    }

    public Product updateProduct(int id, Product productDetails, int userId) {
        Product product = getProductById(id);
        User user = userRepository.findById(userId).get();
        if (user != product.getCreatedBy()) {
            throw new RuntimeException("You can only update your own products.");
        }
        /////////
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImageUrl(productDetails.getImageUrl());

        if (productDetails.getCategories() != null) {
            product.setCategories(productDetails.getCategories());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(int id, int userId) {
        Product product = getProductById(id);
        User user = userRepository.findById(userId).get();
        if (user != product.getCreatedBy()) {
            throw new RuntimeException("You can only delete your own products.");
        }
        productRepository.deleteById(id);
    }
}