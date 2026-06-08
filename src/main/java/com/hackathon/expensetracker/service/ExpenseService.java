package com.hackathon.expensetracker.service;

import com.hackathon.expensetracker.dto.ExpenseDto;
import com.hackathon.expensetracker.entity.Category;
import com.hackathon.expensetracker.entity.Expense;
import com.hackathon.expensetracker.entity.User;
import com.hackathon.expensetracker.exception.ResourceNotFoundException;
import com.hackathon.expensetracker.repository.CategoryRepository;
import com.hackathon.expensetracker.repository.ExpenseRepository;
import com.hackathon.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseDto createExpense(Long userId, ExpenseDto expenseDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + expenseDto.getCategoryId()));

        Expense expense = new Expense();
        expense.setTitle(expenseDto.getTitle());
        expense.setDescription(expenseDto.getDescription());
        expense.setAmount(expenseDto.getAmount());
        expense.setExpenseDate(expenseDto.getExpenseDate());
        expense.setPaymentMethod(Expense.PaymentMethod.valueOf(expenseDto.getPaymentMethod()));
        expense.setUser(user);
        expense.setCategory(category);
        expense.setCreatedAt(LocalDateTime.now());

        Expense savedExpense = expenseRepository.save(expense);
        return mapToDto(savedExpense);
    }

    public Page<ExpenseDto> getExpenses(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return expenseRepository.findByUser(user, pageable)
                .map(this::mapToDto);
    }

    public Page<ExpenseDto> getExpensesByCategory(Long userId, Long categoryId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return expenseRepository.findByUserAndCategoryId(user, categoryId, pageable)
                .map(this::mapToDto);
    }

    public ExpenseDto getExpenseById(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));
        return mapToDto(expense);
    }

    public ExpenseDto updateExpense(Long expenseId, ExpenseDto expenseDto) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));

        if (expenseDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(expenseDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + expenseDto.getCategoryId()));
            expense.setCategory(category);
        }

        expense.setTitle(expenseDto.getTitle());
        expense.setDescription(expenseDto.getDescription());
        expense.setAmount(expenseDto.getAmount());
        expense.setExpenseDate(expenseDto.getExpenseDate());
        if (expenseDto.getPaymentMethod() != null) {
            expense.setPaymentMethod(Expense.PaymentMethod.valueOf(expenseDto.getPaymentMethod()));
        }

        Expense updatedExpense = expenseRepository.save(expense);
        return mapToDto(updatedExpense);
    }

    public void deleteExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));
        expenseRepository.delete(expense);
    }

    public List<Expense> getExpensesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return expenseRepository.findExpensesByDateRange(user, startDate, endDate);
    }

    public BigDecimal getTotalExpenditure(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        List<Expense> expenses = expenseRepository.findByUser(user, Pageable.unpaged()).getContent();
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ExpenseDto mapToDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setPaymentMethod(expense.getPaymentMethod().toString());
        dto.setCategoryId(expense.getCategory().getId());
        dto.setCategoryName(expense.getCategory().getName());
        return dto;
    }
}
