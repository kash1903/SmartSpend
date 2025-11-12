package com.example.smartSpend.smartExpenseTracker.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.smartSpend.smartExpenseTracker.model.Expense;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {
    

     // Find all expenses of a particular user
    List<Expense> findByUser(SmartSpendUser user);

    // Filter by category
    List<Expense> findByUserAndCategory(SmartSpendUser user, String category);

    // Filter by date range
    List<Expense> findByUserAndDateBetween(SmartSpendUser user, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndTypeAndDateBetween(SmartSpendUser user, String type, LocalDate startDate, LocalDate endDate);

}
