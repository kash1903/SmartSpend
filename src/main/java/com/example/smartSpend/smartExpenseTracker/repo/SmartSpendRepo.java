package com.example.smartSpend.smartExpenseTracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;

import java.util.Optional;

public interface SmartSpendRepo extends JpaRepository<SmartSpendUser,Long> {
    
    // Used during login using email
    Optional<SmartSpendUser> findByEmail(String email);
    
    // Used during login using username
    Optional <SmartSpendUser> findByUsername(String username);

      
    // Used during registration check
    boolean existsByEmail(String email);
}
