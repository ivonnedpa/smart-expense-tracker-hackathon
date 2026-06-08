package com.hackathon.expensetracker.repository;

import com.hackathon.expensetracker.entity.Expense;
import com.hackathon.expensetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUser(User user, Pageable pageable);
    Page<Expense> findByUserAndCategoryId(User user, Long categoryId, Pageable pageable);
    
    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :startDate AND :endDate")
    List<Expense> findExpensesByDateRange(User user, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.expenseDate >= :startDate")
    List<Expense> findRecentExpenses(User user, LocalDate startDate);
}
