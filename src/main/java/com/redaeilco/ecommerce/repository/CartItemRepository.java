package com.redaeilco.ecommerce.repository;

import com.redaeilco.ecommerce.model.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}