package com.example.smartSpend.smartExpenseTracker.ServiceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.smartSpend.smartExpenseTracker.model.Budget;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.BudgetRepo;
import com.example.smartSpend.smartExpenseTracker.service.BudgetService;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BudgetServiceTest {
      @Mock
    private BudgetRepo budgetRepo;

    @InjectMocks
    private BudgetService budgetService;

    private Budget budget;
    private SmartSpendUser user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new SmartSpendUser();
        user.setId(1L);
        user.setUsername("testUser");

        budget = new Budget();
        budget.setLimitAmount(1000.0);
        budget.setSpent(850.0);
        budget.setUser(user);
    }

    @Test
    void testSaveBudget_ShouldSetStatusNearLimit() {
        when(budgetRepo.save(any(Budget.class))).thenReturn(budget);

        Budget savedBudget = budgetService.saveBudget(budget);

        assertThat(savedBudget.getRemaining()).isEqualTo(150.0);
        assertThat(savedBudget.getAlertStatus()).isEqualTo("NEAR LIMIT");
    }

    @Test
    void testSaveBudget_ShouldSetStatusExceededLimit() {
        budget.setSpent(1100.0);
        when(budgetRepo.save(any(Budget.class))).thenReturn(budget);

        Budget saved = budgetService.saveBudget(budget);

        assertThat(saved.getAlertStatus()).isEqualTo("EXCEEDED LIMIT");
    }

    @Test
    void testGetBudgetsByUser() {
        when(budgetRepo.findByUser(user)).thenReturn(Arrays.asList(budget));

        List<Budget> result = budgetService.getBudgetsByUser(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getUsername()).isEqualTo("testUser");
    }

    @Test
    void testGetBudgetById() {
        when(budgetRepo.findById(1L)).thenReturn(Optional.of(budget));

        Optional<Budget> found = budgetService.getBudgetById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getLimitAmount()).isEqualTo(1000.0);
    }

    @Test
    void testDeleteBudget() {
        budgetService.deleteBudget(1L);
        verify(budgetRepo, times(1)).deleteById(1L);
    }
}
