package com.redaeilco.ecommerce.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import javax.validation.constraints.*;

import org.springframework.validation.annotation.Validated;

@Entity
@Validated
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name of the category is required")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    // toString
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
