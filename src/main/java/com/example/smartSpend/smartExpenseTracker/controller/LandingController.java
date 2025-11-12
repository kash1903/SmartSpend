package com.example.smartSpend.smartExpenseTracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LandingController {
    @GetMapping("/")
    public String showLandingPage() {
        return "index"; // Renders templates/index.html
    }

   
        @GetMapping("/register")
    public String showRegisterPage() {
        return "rgtr2"; 
    } 

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Later you can create login.html
    }


    // Expense page
@GetMapping("/expense")
public String showExpensePage() {
    return "expense"; // Renders templates/expense.html
}


// Budget page
@GetMapping("/budget")
public String showBudgetPage(){
    return "budget";
}


    

 

  
}
