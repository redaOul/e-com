package com.redaeilco.ecommerce.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import javax.validation.constraints.*;

import org.springframework.validation.annotation.Validated;

@Entity
@Validated
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name of the product is required")
    private String name;

    @NotNull(message = "Description of the product is required")
    private String description;

    @NotNull(message = "Price of the product is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;

    @NotNull(message = "Stock quantity of the product is required")
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private int stockQuantity;

    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<Category> getCategories() {
        return categories;
    }
    
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @PrePersist
    public void prePersist() {
        // Set the createdAt and updatedAt fields to the current time
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Set the default image URL if not provided
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            this.imageUrl = "https://via.placeholder.com/150";
        }
    }

    @PreUpdate
    public void preUpdate() {
        // Set the updatedAt field to the current time
        this.updatedAt = LocalDateTime.now();
    }

    // toString
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories=" + categories +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
