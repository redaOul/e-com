package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.model.Category;
import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.CategoryRepository;
import com.redaeilco.ecommerce.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        // If the product is not found, throw an exception and catch it in the controller
        return categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));
    }

    public Category createCategory(Category category, int userId) {
        User user = userRepository.findById(userId).get();
        category.setCreatedBy(user);
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category categoryDetails, int userId) {
        Category category = getCategoryById(id);
        User user = userRepository.findById(userId).get();
        if (user != category.getCreatedBy()) {
            throw new RuntimeException("You can only update your own categories.");
        }
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(int id, int userId) {
        Category category = getCategoryById(id);
        User user = userRepository.findById(userId).get();
        if (user != category.getCreatedBy()) {
            throw new RuntimeException("You can only delete your own categories.");
        }
        categoryRepository.deleteById(id);
    }
}