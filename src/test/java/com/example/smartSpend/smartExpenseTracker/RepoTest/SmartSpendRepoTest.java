package com.example.smartSpend.smartExpenseTracker.RepoTest;

import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class SmartSpendRepoTest {
    

      @Autowired
    private SmartSpendRepo smartSpendRepo;

    private SmartSpendUser user;

    @BeforeEach
    void setup() {
        user = new SmartSpendUser();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        smartSpendRepo.save(user);
    }

    @Test
    void testFindByEmail() {
        Optional<SmartSpendUser> foundUser = smartSpendRepo.findByEmail("john@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");
    }

    @Test
    void testFindByUsername() {
        Optional<SmartSpendUser> foundUser = smartSpendRepo.findByUsername("john_doe");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testExistsByEmailTrue() {
        boolean exists = smartSpendRepo.existsByEmail("john@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmailFalse() {
        boolean exists = smartSpendRepo.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }
}
