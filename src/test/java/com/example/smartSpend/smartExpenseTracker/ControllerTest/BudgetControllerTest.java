package com.example.smartSpend.smartExpenseTracker.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import com.example.smartSpend.smartExpenseTracker.controller.BudgetController;
import com.example.smartSpend.smartExpenseTracker.model.Budget;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import com.example.smartSpend.smartExpenseTracker.service.BudgetService;

public class BudgetControllerTest {
      @InjectMocks
    private BudgetController budgetController;

    @Mock
    private BudgetService budgetService;

    @Mock
    private SmartSpendRepo userRepo;

    private SmartSpendUser mockUser;
    private Budget mockBudget;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockUser = new SmartSpendUser();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        mockBudget = new Budget();
        mockBudget.setId(1L);
        mockBudget.setCategory("Food");
        mockBudget.setLimitAmount(1000.0);
        mockBudget.setUser(mockUser);
    }

    @Test
    void testAddBudget_UserFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        String result = budgetController.addBudget(mockBudget, 1L);

        verify(budgetService, times(1)).saveBudget(mockBudget);
        assertEquals("✅ Budget added successfully!", result);
    }

    @Test
    void testAddBudget_UserNotFound() {
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        String result = budgetController.addBudget(mockBudget, 2L);

        verify(budgetService, never()).saveBudget(any());
        assertEquals("❌ User not found!", result);
    }

    @Test
    void testGetBudgetsByUser_UserFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(budgetService.getBudgetsByUser(mockUser)).thenReturn(List.of(mockBudget));

        List<Budget> result = budgetController.getBudgetsByUser(1L);

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getCategory());
    }

    @Test
    void testGetBudgetsByUser_UserNotFound() {
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        List<Budget> result = budgetController.getBudgetsByUser(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateBudget_BudgetFound() {
        Budget updated = new Budget();
        updated.setCategory("Travel");
        updated.setLimitAmount(2000.0);

        when(budgetService.getBudgetById(1L)).thenReturn(Optional.of(mockBudget));

        String result = budgetController.updateBudget(1L, updated);

        verify(budgetService, times(1)).saveBudget(any());
        assertEquals("✅ Budget updated successfully!", result);
        assertEquals("Travel", mockBudget.getCategory());
    }

    @Test
    void testUpdateBudget_BudgetNotFound() {
        when(budgetService.getBudgetById(1L)).thenReturn(Optional.empty());

        String result = budgetController.updateBudget(1L, mockBudget);

        assertEquals("❌ Budget not found!", result);
        verify(budgetService, never()).saveBudget(any());
    }

    @Test
    void testDeleteBudget() {
        String result = budgetController.deleteBudget(1L);

        verify(budgetService, times(1)).deleteBudget(1L);
        assertEquals("🗑️ Budget deleted successfully!", result);
    }
}
