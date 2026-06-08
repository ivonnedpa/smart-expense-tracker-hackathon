package com.hackathon.expensetracker.repository;

import com.hackathon.expensetracker.entity.Budget;
import com.hackathon.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
    Optional<Budget> findByUserAndCategoryIdAndMonthAndYear(User user, Long categoryId, Integer month, Integer year);
    List<Budget> findByUserAndMonth(User user, Integer month);
}
