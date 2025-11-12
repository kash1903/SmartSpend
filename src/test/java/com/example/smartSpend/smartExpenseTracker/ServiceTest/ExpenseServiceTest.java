package com.example.smartSpend.smartExpenseTracker.ServiceTest;

import com.example.smartSpend.smartExpenseTracker.model.Expense;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.ExpenseRepo;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import com.example.smartSpend.smartExpenseTracker.service.ExpenseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {
      @Mock
    private ExpenseRepo expenseRepo;

    @Mock
    private SmartSpendRepo userRepo;

    @InjectMocks
    private ExpenseService expenseService;

    private SmartSpendUser user;
    private Expense expense;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new SmartSpendUser();
        user.setId(1L);
        user.setUsername("testUser");

        expense = new Expense();
        expense.setId(1L);
        expense.setAmount(500.0);
        expense.setCategory("Food");
        expense.setType("EXPENSE");
        expense.setUser(user);
        expense.setDate(LocalDate.now());
    }

    @Test
    void testAddExpense_UserExists() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepo.save(any(Expense.class))).thenReturn(expense);

        String result = expenseService.addExpense(1L, expense);

        assertThat(result).contains("✅");
        verify(expenseRepo, times(1)).save(expense);
    }

    @Test
    void testAddExpense_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        String result = expenseService.addExpense(1L, expense);

        assertThat(result).contains("❌ User not found");
    }

    @Test
    void testUpdateExpense_Found() {
        Expense updated = new Expense();
        updated.setTitle("Updated");
        updated.setCategory("Bills");
        updated.setType("EXPENSE");
        updated.setAmount(1000.0);
        updated.setDate(LocalDate.now());
        updated.setDescription("Updated expense");

        when(expenseRepo.findById(1L)).thenReturn(Optional.of(expense));
        when(expenseRepo.save(any(Expense.class))).thenReturn(updated);

        String result = expenseService.updateExpense(1L, updated);

        assertThat(result).contains("✅");
        verify(expenseRepo).save(any(Expense.class));
    }

    @Test
    void testUpdateExpense_NotFound() {
        when(expenseRepo.findById(1L)).thenReturn(Optional.empty());

        String result = expenseService.updateExpense(1L, expense);

        assertThat(result).contains("❌ Expense not found");
    }

    @Test
    void testDeleteExpense_Found() {
        when(expenseRepo.existsById(1L)).thenReturn(true);

        String result = expenseService.deleteExpense(1L);

        assertThat(result).contains("🗑️");
        verify(expenseRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteExpense_NotFound() {
        when(expenseRepo.existsById(1L)).thenReturn(false);

        String result = expenseService.deleteExpense(1L);

        assertThat(result).contains("❌ Expense not found");
    }

    @Test
    void testGetExpensesByUser_Found() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepo.findByUser(user)).thenReturn(List.of(expense));

        List<Expense> result = expenseService.getExpensesByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Food");
    }

    @Test
    void testGetExpensesByUser_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        List<Expense> result = expenseService.getExpensesByUser(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void testGetTotalIncomeThisMonth() {
        Expense income = new Expense();
        income.setAmount(2000.0);
        income.setType("INCOME");
        when(expenseRepo.findByUserAndTypeAndDateBetween(any(), eq("INCOME"), any(), any()))
                .thenReturn(List.of(income));

        double total = expenseService.getTotalIncomeThisMonth(user);

        assertThat(total).isEqualTo(2000.0);
    }

    @Test
    void testGetTotalExpenseThisMonth() {
        Expense e1 = new Expense();
        e1.setAmount(500.0);
        e1.setType("EXPENSE");
        when(expenseRepo.findByUserAndTypeAndDateBetween(any(), eq("EXPENSE"), any(), any()))
                .thenReturn(List.of(e1));

        double total = expenseService.getTotalExpenseThisMonth(user);

        assertThat(total).isEqualTo(500.0);
    }

    @Test
    void testGetIncomeVsExpense() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepo.findByUserAndTypeAndDateBetween(any(), eq("INCOME"), any(), any()))
                .thenReturn(List.of(new Expense(null, "Salary", "INCOME", "Work", 1000.0, LocalDate.now(), null, user)));
        when(expenseRepo.findByUserAndTypeAndDateBetween(any(), eq("EXPENSE"), any(), any()))
                .thenReturn(List.of(new Expense(null, "Food", "EXPENSE", "Food", 500.0, LocalDate.now(), null, user)));

        Map<String, Double> result = expenseService.getIncomeVsExpense(1L);

        assertThat(result.get("Income")).isEqualTo(1000.0);
        assertThat(result.get("Expense")).isEqualTo(500.0);
    }

    @Test
    void testGetSpendingByCategory() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepo.findByUserAndTypeAndDateBetween(any(), eq("EXPENSE"), any(), any()))
                .thenReturn(List.of(expense));

        Map<String, Double> result = expenseService.getSpendingByCategory(1L);

        assertThat(result).containsKey("Food");
        assertThat(result.get("Food")).isEqualTo(500.0);
    }
}
