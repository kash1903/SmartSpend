package com.example.smartSpend.smartExpenseTracker.controller;


import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.service.SmartSpendService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allows frontend calls from any domain (for now)
public class SmartSpendController {
        @Autowired
    private SmartSpendService userService;


    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SmartSpendUser user) {
        String response = userService.registerUser(user);

        if (response.contains("already")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

  

    // new code 
    // ✅ Login using email
@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody SmartSpendUser request) {
    // Extract email and password from request body (sent as JSON)
    String email = request.getEmail();
    String password = request.getPassword();

    Optional<SmartSpendUser> userOptional = userService.loginByEmail(email, password);

    if (userOptional.isPresent()) {
        SmartSpendUser user = userOptional.get();

        // Create a simple response map (like a mini JSON)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Login successful!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("❌ Invalid email or password!");
    }
}

    
    
    // ✅ Login using username
@PostMapping("/login/username")
public ResponseEntity<?> loginWithUsername(@RequestBody SmartSpendUser request) {
    // Extract username and password from the JSON request body
    String username = request.getUsername();
    String password = request.getPassword();

    Optional<SmartSpendUser> userOptional = userService.loginByUsername(username, password);

    if (userOptional.isPresent()) {
        SmartSpendUser user = userOptional.get();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Login successful using username!");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("❌ Invalid username or password!");
    }

}


    }

