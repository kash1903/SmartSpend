package com.example.smartSpend.smartExpenseTracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.smartSpend.smartExpenseTracker.model.Budget;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import java.util.List;

public interface BudgetRepo extends JpaRepository<Budget,Long> {
        // Get all budgets for a specific user
    List<Budget> findByUser(SmartSpendUser user);

    
}
