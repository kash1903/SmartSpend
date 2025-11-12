package com.example.smartSpend.smartExpenseTracker.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.smartSpend.smartExpenseTracker.controller.DashBoardController;

public class DashBoardControllerTest {
    

      @Test
    void testShowDashboard_ReturnsDashboardView() {
        DashBoardController controller = new DashBoardController();
        String viewName = controller.showDashboard();

        assertEquals("dashboard", viewName, "Dashboard view name should be 'dashboard'");
    }
}
