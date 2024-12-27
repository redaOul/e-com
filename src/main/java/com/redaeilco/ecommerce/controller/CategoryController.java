package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.model.Category;
import com.redaeilco.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin')") 
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")  // Only Admin can create a product
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category, @RequestHeader("Authorization") String token) {
        Category createdCategory = categoryService.createCategory(category, token);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")   // Only Admin can create a product
    public ResponseEntity<?> updateCategory(@PathVariable int id, @Valid @RequestBody Category categoryDetails, @RequestHeader("Authorization") String token) {
        Category updatededCategory =  categoryService.updateCategory(id, categoryDetails, token);
        return ResponseEntity.ok(updatededCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")  // Only Admin can create a product
    public ResponseEntity<?> deleteCategory(@PathVariable int id, @RequestHeader("Authorization") String token) {
        categoryService.deleteCategory(id, token);
        return ResponseEntity.ok("Category with id " + id + " deleted successfully");
    }
}
