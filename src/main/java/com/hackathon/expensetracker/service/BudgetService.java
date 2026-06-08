package com.hackathon.expensetracker.service;

import com.hackathon.expensetracker.dto.BudgetDto;
import com.hackathon.expensetracker.entity.Budget;
import com.hackathon.expensetracker.entity.Category;
import com.hackathon.expensetracker.entity.User;
import com.hackathon.expensetracker.exception.BudgetExceededException;
import com.hackathon.expensetracker.exception.ResourceNotFoundException;
import com.hackathon.expensetracker.repository.BudgetRepository;
import com.hackathon.expensetracker.repository.CategoryRepository;
import com.hackathon.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public BudgetDto createBudget(Long userId, BudgetDto budgetDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = categoryRepository.findById(budgetDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + budgetDto.getCategoryId()));

        Budget budget = new Budget();
        budget.setMonth(budgetDto.getMonth());
        budget.setYear(budgetDto.getYear());
        budget.setLimitAmount(budgetDto.getLimitAmount());
        budget.setCurrentSpent(BigDecimal.ZERO);
        budget.setStatus(Budget.BudgetStatus.ACTIVE);
        budget.setUser(user);
        budget.setCategory(category);

        Budget savedBudget = budgetRepository.save(budget);
        return mapToDto(savedBudget);
    }

    public List<BudgetDto> getUserBudgets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return budgetRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public BudgetDto updateBudgetSpent(Long budgetId, BigDecimal amountSpent) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + budgetId));

        BigDecimal newSpent = budget.getCurrentSpent().add(amountSpent);
        
        if (newSpent.compareTo(budget.getLimitAmount()) > 0) {
            budget.setStatus(Budget.BudgetStatus.EXCEEDED);
            budgetRepository.save(budget);
            throw new BudgetExceededException("Budget limit exceeded for category: " + budget.getCategory().getName());
        }

        budget.setCurrentSpent(newSpent);
        if (newSpent.compareTo(budget.getLimitAmount()) == 0) {
            budget.setStatus(Budget.BudgetStatus.COMPLETED);
        }

        Budget updatedBudget = budgetRepository.save(budget);
        return mapToDto(updatedBudget);
    }

    public BudgetDto getBudgetStatus(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + budgetId));
        return mapToDto(budget);
    }

    public void deleteBudget(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + budgetId));
        budgetRepository.delete(budget);
    }

    private BudgetDto mapToDto(Budget budget) {
        BudgetDto dto = new BudgetDto();
        dto.setId(budget.getId());
        dto.setMonth(budget.getMonth());
        dto.setYear(budget.getYear());
        dto.setLimitAmount(budget.getLimitAmount());
        dto.setCurrentSpent(budget.getCurrentSpent());
        dto.setStatus(budget.getStatus().toString());
        dto.setCategoryId(budget.getCategory().getId());
        dto.setCategoryName(budget.getCategory().getName());
        return dto;
    }
}
