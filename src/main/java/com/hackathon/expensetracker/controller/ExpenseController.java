package com.hackathon.expensetracker.controller;

import com.hackathon.expensetracker.dto.ExpenseDto;
import com.hackathon.expensetracker.dto.ApiResponse;
import com.hackathon.expensetracker.security.CurrentUser;
import com.hackathon.expensetracker.security.UserPrincipal;
import com.hackathon.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer JWT")
@Tag(name = "Expense Management", description = "APIs for managing user expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create new expense", description = "Create a new expense record for the authenticated user")
    public ResponseEntity<ApiResponse> createExpense(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody ExpenseDto expenseDto) {
        ExpenseDto createdExpense = expenseService.createExpense(userPrincipal.getId(), expenseDto);
        ApiResponse response = new ApiResponse(true, "Expense created successfully");
        response.setData(createdExpense);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get user expenses", description = "Retrieve all expenses for the authenticated user with pagination")
    public ResponseEntity<ApiResponse> getExpenses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "expenseDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpenseDto> expenses = expenseService.getExpenses(userPrincipal.getId(), pageable);
        ApiResponse response = new ApiResponse(true, "Expenses retrieved successfully");
        response.setData(expenses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get expenses by category", description = "Retrieve expenses filtered by category")
    public ResponseEntity<ApiResponse> getExpensesByCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExpenseDto> expenses = expenseService.getExpensesByCategory(userPrincipal.getId(), categoryId, pageable);
        ApiResponse response = new ApiResponse(true, "Expenses retrieved successfully");
        response.setData(expenses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get expense by ID", description = "Retrieve a specific expense record")
    public ResponseEntity<ApiResponse> getExpenseById(@PathVariable Long id) {
        ExpenseDto expense = expenseService.getExpenseById(id);
        ApiResponse response = new ApiResponse(true, "Expense retrieved successfully");
        response.setData(expense);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update expense", description = "Update an existing expense record")
    public ResponseEntity<ApiResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDto expenseDto) {
        ExpenseDto updatedExpense = expenseService.updateExpense(id, expenseDto);
        ApiResponse response = new ApiResponse(true, "Expense updated successfully");
        response.setData(updatedExpense);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete expense", description = "Delete an expense record")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(new ApiResponse(true, "Expense deleted successfully"));
    }
}
