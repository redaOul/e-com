package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.model.Product;
import com.redaeilco.ecommerce.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        // If the product is not found, throw an exception and catch it in the controller
        return productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(int id, Product productDetails) {
        Product product = getProductById(id);
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

    public void deleteProduct(int id) {
        getProductById(id); // will throw an exception if the product is not found
        productRepository.deleteById(id);
    }
}