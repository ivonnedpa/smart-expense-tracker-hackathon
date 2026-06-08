package com.hackathon.expensetracker.controller;

import com.hackathon.expensetracker.dto.CategoryDto;
import com.hackathon.expensetracker.dto.ApiResponse;
import com.hackathon.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing expense categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all available expense categories")
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        ApiResponse response = new ApiResponse(true, "Categories retrieved successfully");
        response.setData(categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        ApiResponse response = new ApiResponse(true, "Category retrieved successfully");
        response.setData(category);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new category", description = "Create a new expense category")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        ApiResponse response = new ApiResponse(true, "Category created successfully");
        response.setData(createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        ApiResponse response = new ApiResponse(true, "Category updated successfully");
        response.setData(updatedCategory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete an expense category")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse(true, "Category deleted successfully"));
    }
}
