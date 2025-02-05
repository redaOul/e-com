package com.redaeilco.ecommerce.repository;

import com.redaeilco.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
    Optional<Order> findByIdAndUserId(int orderId, int userId);
}