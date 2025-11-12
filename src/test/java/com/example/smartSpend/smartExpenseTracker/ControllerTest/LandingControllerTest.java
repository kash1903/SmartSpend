package com.example.smartSpend.smartExpenseTracker.ControllerTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.smartSpend.smartExpenseTracker.controller.LandingController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LandingController.class)
public class LandingControllerTest {
      @Autowired
    private MockMvc mockMvc;

    @Test
    void testShowLandingPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("rgtr2"));
    }

    @Test
    void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testShowExpensePage() throws Exception {
        mockMvc.perform(get("/expense"))
                .andExpect(status().isOk())
                .andExpect(view().name("expense"));
    }

    @Test
    void testShowBudgetPage() throws Exception {
        mockMvc.perform(get("/budget"))
                .andExpect(status().isOk())
                .andExpect(view().name("budget"));
    }
}
