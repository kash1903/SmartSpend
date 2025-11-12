package com.example.smartSpend.smartExpenseTracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {


    
     @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard"; // Renders templates/dashboard.html
    } 
}
