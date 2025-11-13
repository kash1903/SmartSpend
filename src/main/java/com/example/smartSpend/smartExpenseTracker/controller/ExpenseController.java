package com.example.smartSpend.smartExpenseTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.example.smartSpend.smartExpenseTracker.model.Expense;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import com.example.smartSpend.smartExpenseTracker.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*") // Allow frontend to call backend from any domain
public class ExpenseController {
       @Autowired
    private ExpenseService expenseService;

    @Autowired 
    private SmartSpendRepo userRepo;



    // Get Income vs Expense
    @GetMapping("/income-vs-expense")
public ResponseEntity<Map<String, Double>> getIncomeVsExpense(@RequestParam Long userId) {
    Map<String, Double> data = expenseService.getIncomeVsExpense(userId);
    return ResponseEntity.ok(data);
}

    // Get Spending by Catagory
    @GetMapping("/spending-by-category")
public ResponseEntity<Map<String, Double>> getSpendingByCategory(@RequestParam Long userId) {
    Map<String, Double> data = expenseService.getSpendingByCategory(userId);
    return ResponseEntity.ok(data);
}



      // Get Total Income
    @GetMapping("/total-income")
public double getTotalIncome(@RequestParam Long userId) {
    SmartSpendUser user = userRepo.findById(userId).orElseThrow();
    return expenseService.getTotalIncomeThisMonth(user);
}
    // Get Total Expense
@GetMapping("/total-expense")
public double getTotalExpense(@RequestParam Long userId) {
    SmartSpendUser user = userRepo.findById(userId).orElseThrow();
    return expenseService.getTotalExpenseThisMonth(user);
}

    // ✅ Add new expense for a specific user
    @PostMapping("/add/{userId}")
    public ResponseEntity<String> addExpense(@PathVariable Long userId, @RequestBody Expense expense) {
        String message = expenseService.addExpense(userId, expense);
        if (message.contains("❌")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    // ✅ Update existing expense
    @PutMapping("/update/{expenseId}")
    public ResponseEntity<String> updateExpense(@PathVariable Long expenseId, @RequestBody Expense expense) {
        String message = expenseService.updateExpense(expenseId, expense);
        if (message.contains("❌")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    // ✅ Delete expense
    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        String message = expenseService.deleteExpense(expenseId);
        if (message.contains("❌")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    // ✅ Get all expenses for a particular user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUser(userId);
        // if (expenses.isEmpty()) {
        //     return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        // }
        // ✅ Always return the list (even if empty)
        return ResponseEntity.ok(expenses);
    } 
}
