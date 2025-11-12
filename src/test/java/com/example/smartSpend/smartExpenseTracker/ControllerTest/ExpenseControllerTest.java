package com.example.smartSpend.smartExpenseTracker.ControllerTest;

import com.example.smartSpend.smartExpenseTracker.controller.ExpenseController;
import com.example.smartSpend.smartExpenseTracker.model.Expense;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import com.example.smartSpend.smartExpenseTracker.service.ExpenseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseControllerTest {
     @Mock
    private ExpenseService expenseService;

    @Mock
    private SmartSpendRepo userRepo;

    @InjectMocks
    private ExpenseController expenseController;

    private SmartSpendUser mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new SmartSpendUser();
        mockUser.setId(1L);
    }

    @Test
    void testGetIncomeVsExpense() {
        Map<String, Double> mockData = Map.of("Income", 1000.0, "Expense", 500.0);
        when(expenseService.getIncomeVsExpense(1L)).thenReturn(mockData);

        ResponseEntity<Map<String, Double>> response = expenseController.getIncomeVsExpense(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1000.0, response.getBody().get("Income"));
    }

    @Test
    void testGetSpendingByCategory() {
        Map<String, Double> mockData = Map.of("Food", 200.0, "Travel", 150.0);
        when(expenseService.getSpendingByCategory(1L)).thenReturn(mockData);

        ResponseEntity<Map<String, Double>> response = expenseController.getSpendingByCategory(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("Food"));
    }

    @Test
    void testGetTotalIncome() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(expenseService.getTotalIncomeThisMonth(mockUser)).thenReturn(1200.0);

        double result = expenseController.getTotalIncome(1L);
        assertEquals(1200.0, result);
    }

    @Test
    void testGetTotalExpense() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(expenseService.getTotalExpenseThisMonth(mockUser)).thenReturn(800.0);

        double result = expenseController.getTotalExpense(1L);
        assertEquals(800.0, result);
    }

    @Test
    void testAddExpense_Success() {
        Expense expense = new Expense();
        when(expenseService.addExpense(eq(1L), any(Expense.class))).thenReturn("✅ Expense added successfully!");

        ResponseEntity<String> response = expenseController.addExpense(1L, expense);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("✅"));
    }

    @Test
    void testAddExpense_Failure() {
        Expense expense = new Expense();
        when(expenseService.addExpense(eq(1L), any(Expense.class))).thenReturn("❌ User not found!");

        ResponseEntity<String> response = expenseController.addExpense(1L, expense);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testUpdateExpense_Success() {
        Expense expense = new Expense();
        when(expenseService.updateExpense(eq(1L), any(Expense.class))).thenReturn("✅ Expense updated!");

        ResponseEntity<String> response = expenseController.updateExpense(1L, expense);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("✅"));
    }

    @Test
    void testUpdateExpense_NotFound() {
        Expense expense = new Expense();
        when(expenseService.updateExpense(eq(1L), any(Expense.class))).thenReturn("❌ Expense not found!");

        ResponseEntity<String> response = expenseController.updateExpense(1L, expense);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteExpense_Success() {
        when(expenseService.deleteExpense(1L)).thenReturn("🗑️ Expense deleted!");

        ResponseEntity<String> response = expenseController.deleteExpense(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("🗑️"));
    }

    @Test
    void testDeleteExpense_NotFound() {
        when(expenseService.deleteExpense(1L)).thenReturn("❌ Expense not found!");

        ResponseEntity<String> response = expenseController.deleteExpense(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetExpensesByUser_Success() {
        List<Expense> mockList = List.of(new Expense(), new Expense());
        when(expenseService.getExpensesByUser(1L)).thenReturn(mockList);

        ResponseEntity<List<Expense>> response = expenseController.getExpensesByUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetExpensesByUser_Empty() {
        when(expenseService.getExpensesByUser(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Expense>> response = expenseController.getExpensesByUser(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
