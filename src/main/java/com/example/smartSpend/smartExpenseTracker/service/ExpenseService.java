package com.example.smartSpend.smartExpenseTracker.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smartSpend.smartExpenseTracker.model.Expense;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.ExpenseRepo;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import java.util.Map;

@Service
public class ExpenseService {
    
        @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private SmartSpendRepo userRepo;


    // For chart get Income vs expense 
    public Map<String, Double> getIncomeVsExpense(Long userId) {
    Optional<SmartSpendUser> userOpt = userRepo.findById(userId);
    if (userOpt.isEmpty()) return Map.of();

    SmartSpendUser user = userOpt.get();

    double income = getTotalIncomeThisMonth(user);
    double expense = getTotalExpenseThisMonth(user);

    return Map.of(
        "Income", income,
        "Expense", expense
    );
}


     // For Chart Spending by Catagory (pie chart)
    public Map<String, Double> getSpendingByCategory(Long userId) {
    Optional<SmartSpendUser> userOpt = userRepo.findById(userId);
    if (userOpt.isEmpty()) return Map.of();

    SmartSpendUser user = userOpt.get();
    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

    List<Expense> expenses = expenseRepo.findByUserAndTypeAndDateBetween(user, "EXPENSE", start, end);

    return expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.summingDouble(Expense::getAmount)
            ));
}


    // In ExpenseService.java (Total income of this month)
public double getTotalIncomeThisMonth(SmartSpendUser user) {
    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    return expenseRepo.findByUserAndTypeAndDateBetween(user, "INCOME", start, end)
                      .stream()
                      .mapToDouble(Expense::getAmount)
                      .sum();
}

public double getTotalExpenseThisMonth(SmartSpendUser user) {
    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    return expenseRepo.findByUserAndTypeAndDateBetween(user, "EXPENSE", start, end)
                      .stream()
                      .mapToDouble(Expense::getAmount)
                      .sum();
}

    // ✅ Add new expense
    public String addExpense(Long userId, Expense expense) {
        Optional<SmartSpendUser> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return "❌ User not found!";
        }

        expense.setUser(userOpt.get());
        expenseRepo.save(expense);
        return "✅ Expense added successfully!";
    }

    // ✅ Update an existing expense
    public String updateExpense(Long expenseId, Expense updatedExpense) {
        Optional<Expense> existingOpt = expenseRepo.findById(expenseId);
        if (existingOpt.isEmpty()) {
            return "❌ Expense not found!";
        }

        Expense existing = existingOpt.get();
        existing.setTitle(updatedExpense.getTitle());
        existing.setCategory(updatedExpense.getCategory());
        existing.setType(updatedExpense.getType());
        existing.setAmount(updatedExpense.getAmount());
        existing.setDate(updatedExpense.getDate());
        existing.setDescription(updatedExpense.getDescription());

        expenseRepo.save(existing);
        return "✅ Expense updated successfully!";
    }

    // ✅ Delete expense
    public String deleteExpense(Long expenseId) {
        if (!expenseRepo.existsById(expenseId)) {
            return "❌ Expense not found!";
        }

        expenseRepo.deleteById(expenseId);
        return "🗑️ Expense deleted successfully!";
    }

    // ✅ Get all expenses for a user
    public List<Expense> getExpensesByUser(Long userId) {
        Optional<SmartSpendUser> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }
        return expenseRepo.findByUser(userOpt.get());
    }
}
