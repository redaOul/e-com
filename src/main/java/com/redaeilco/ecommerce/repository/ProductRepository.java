package com.redaeilco.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redaeilco.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<List<Product>> findByCategories_IdIn(List<Integer> categoryIds);
    Optional<List<Product>> findByPriceLessThanEqual(double maxPrice);
    Optional<List<Product>> findByNameContainingIgnoreCase(String name);
}