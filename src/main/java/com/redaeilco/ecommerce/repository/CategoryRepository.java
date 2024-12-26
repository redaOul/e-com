package com.redaeilco.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redaeilco.ecommerce.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
