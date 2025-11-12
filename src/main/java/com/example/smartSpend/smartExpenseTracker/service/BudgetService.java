package com.example.smartSpend.smartExpenseTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.smartSpend.smartExpenseTracker.model.Budget;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.BudgetRepo;

@Service
public class BudgetService {
      @Autowired
    private BudgetRepo budgetRepo;

    // ➕ Add or update a budget
    public Budget saveBudget(Budget budget) {
        // Automatically calculate remaining & alert status
        if (budget.getLimitAmount() != null && budget.getSpent() != null) {
            double remaining = budget.getLimitAmount() - budget.getSpent();
            budget.setRemaining(remaining);

            if (remaining <= 0) {
                budget.setAlertStatus("EXCEEDED LIMIT");
            } else if (remaining <= (0.2 * budget.getLimitAmount())) {
                budget.setAlertStatus("NEAR LIMIT");
            } else {
                budget.setAlertStatus("SAFE");
            }
        }
        return budgetRepo.save(budget);
    }

    // 🧾 Get all budgets for a user
    public List<Budget> getBudgetsByUser(SmartSpendUser user) {
        return budgetRepo.findByUser(user);
    }

    // 🆔 Get a single budget by ID
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepo.findById(id);
    }

    // ❌ Delete a budget
    public void deleteBudget(Long id) {
        budgetRepo.deleteById(id);
    }
}
