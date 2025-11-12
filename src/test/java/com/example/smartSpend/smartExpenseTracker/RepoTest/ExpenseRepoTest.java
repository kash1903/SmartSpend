package com.example.smartSpend.smartExpenseTracker.RepoTest;

import com.example.smartSpend.smartExpenseTracker.model.Expense;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.ExpenseRepo;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class ExpenseRepoTest {
      @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private SmartSpendRepo userRepo;

    private SmartSpendUser user;

    @BeforeEach
    void setup() {
        // Create and save a dummy user before each test
        user = new SmartSpendUser();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("12345");
        userRepo.save(user);

        // Add some expenses
        Expense e1 = new Expense();
        e1.setUser(user);
        e1.setCategory("Food");
        e1.setAmount(500.0);
        e1.setType("Expense");
        e1.setDate(LocalDate.of(2025, 11, 1));

        Expense e2 = new Expense();
        e2.setUser(user);
        e2.setCategory("Travel");
        e2.setAmount(1000.0);
        e2.setType("Expense");
        e2.setDate(LocalDate.of(2025, 11, 5));

        Expense e3 = new Expense();
        e3.setUser(user);
        e3.setCategory("Salary");
        e3.setAmount(3000.0);
        e3.setType("Income");
        e3.setDate(LocalDate.of(2025, 11, 3));

        expenseRepo.saveAll(List.of(e1, e2, e3));
    }

    @Test
    void testFindByUser() {
        List<Expense> expenses = expenseRepo.findByUser(user);
        assertThat(expenses).hasSize(3);
    }

    @Test
    void testFindByUserAndCategory() {
        List<Expense> foodExpenses = expenseRepo.findByUserAndCategory(user, "Food");
        assertThat(foodExpenses).hasSize(1);
        assertThat(foodExpenses.get(0).getCategory()).isEqualTo("Food");
    }

    @Test
    void testFindByUserAndDateBetween() {
        LocalDate start = LocalDate.of(2025, 11, 2);
        LocalDate end = LocalDate.of(2025, 11, 6);

        List<Expense> expensesInRange = expenseRepo.findByUserAndDateBetween(user, start, end);
        assertThat(expensesInRange).hasSize(2); // Travel + Salary
    }

    @Test
    void testFindByUserAndTypeAndDateBetween() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 10);

        List<Expense> incomeExpenses = expenseRepo.findByUserAndTypeAndDateBetween(user, "Income", start, end);
        assertThat(incomeExpenses).hasSize(1);
        assertThat(incomeExpenses.get(0).getCategory()).isEqualTo("Salary");
    }
}
