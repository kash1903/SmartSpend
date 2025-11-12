package com.example.smartSpend.smartExpenseTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.smartSpend.smartExpenseTracker.model.Budget;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import com.example.smartSpend.smartExpenseTracker.service.BudgetService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @Autowired
    private SmartSpendRepo userRepo;

    // ➕ Add or update a budget
    @PostMapping("/add")
    public String addBudget(@RequestBody Budget budget, @RequestParam Long userId) {
        Optional<SmartSpendUser> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return "❌ User not found!";
        }
        budget.setUser(userOpt.get());
        budgetService.saveBudget(budget);
        return "✅ Budget added successfully!";
    }

    // 📋 Get all budgets for a user
    @GetMapping("/user/{userId}")
    public List<Budget> getBudgetsByUser(@PathVariable Long userId) {
        Optional<SmartSpendUser> userOpt = userRepo.findById(userId);
        return userOpt.map(budgetService::getBudgetsByUser).orElse(List.of());
    }

    // ✏️ Update a budget
    @PutMapping("/update/{id}")
    public String updateBudget(@PathVariable Long id, @RequestBody Budget updatedBudget) {
        Optional<Budget> existing = budgetService.getBudgetById(id);
        if (existing.isEmpty()) {
            return "❌ Budget not found!";
        }

        Budget budget = existing.get();
        budget.setCategory(updatedBudget.getCategory());
        budget.setLimitAmount(updatedBudget.getLimitAmount());
        budget.setSpent(updatedBudget.getSpent());
        budget.setRemaining(updatedBudget.getRemaining());
        budget.setAlertStatus(updatedBudget.getAlertStatus());

        budgetService.saveBudget(budget);
        return "✅ Budget updated successfully!";
    }

    // 🗑️ Delete a budget
    @DeleteMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return "🗑️ Budget deleted successfully!";
    }
}
