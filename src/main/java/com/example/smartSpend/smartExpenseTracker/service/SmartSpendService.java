package com.example.smartSpend.smartExpenseTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import java.util.Optional;

@Service
public class SmartSpendService {
       @Autowired
    private SmartSpendRepo userRepo;

    // Registration 
    public String registerUser(SmartSpendUser user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            return " ❌ Email already registered!";
        }
        userRepo.save(user);
        return " ✅ User registered successfully!";
    }

    // ✅ Login using email
    public Optional<SmartSpendUser> loginByEmail(String email, String password) {
        Optional<SmartSpendUser> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            SmartSpendUser user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }


     // ✅ Login using username
    public Optional<SmartSpendUser> loginByUsername(String username, String password) {
        Optional<SmartSpendUser> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent()) {
            SmartSpendUser user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
