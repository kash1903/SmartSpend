package com.example.smartSpend.smartExpenseTracker.RepoTest;

import org.junit.jupiter.api.Test;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.smartSpend.smartExpenseTracker.model.Budget;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.BudgetRepo;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetRepoTest {
      @Autowired
    private BudgetRepo budgetRepo;

    @Autowired
    private SmartSpendRepo userRepo;

    @Test
    void testFindByUser() {
        // Create a dummy user
        SmartSpendUser user = new SmartSpendUser();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("12345");
        userRepo.save(user);

        // Create two budgets for that user
        Budget b1 = new Budget();
        b1.setCategory("Food");
        b1.setLimitAmount(1000.0);
        b1.setUser(user);

        Budget b2 = new Budget();
        b2.setCategory("Travel");
        b2.setLimitAmount(2000.0);
        b2.setUser(user);

        budgetRepo.save(b1);
        budgetRepo.save(b2);

        // Fetch by user
        List<Budget> budgets = budgetRepo.findByUser(user);

        // Validate
        assertThat(budgets).hasSize(2);
        assertThat(budgets.get(0).getUser().getUsername()).isEqualTo("john");
    }

    @Test
    void testFindByUser_NoBudgetsFound() {
        // Create a user with no budgets
        SmartSpendUser user = new SmartSpendUser();
        user.setUsername("emptyUser");
        user.setEmail("empty@example.com");
        user.setPassword("abc123");
        userRepo.save(user);

        // Fetch budgets for this user
        List<Budget> budgets = budgetRepo.findByUser(user);

        // Expect empty result
        assertThat(budgets).isEmpty();
    }
}
