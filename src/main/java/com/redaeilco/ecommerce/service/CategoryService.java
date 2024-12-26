package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.model.Category;
import com.redaeilco.ecommerce.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        // If the product is not found, throw an exception and catch it in the controller
        return categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category categoryDetails) {
        Category category = getCategoryById(id);
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(int id) {
        getCategoryById(id); // will throw an exception if the category is not found
        categoryRepository.deleteById(id);
    }
}