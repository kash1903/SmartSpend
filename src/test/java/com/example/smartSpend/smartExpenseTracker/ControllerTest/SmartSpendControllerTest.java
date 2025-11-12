package com.example.smartSpend.smartExpenseTracker.ControllerTest;

import com.example.smartSpend.smartExpenseTracker.controller.SmartSpendController;
import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.service.SmartSpendService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SmartSpendController.class)

public class SmartSpendControllerTest {
     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SmartSpendService userService;

    // ✅ Test Register - Successful
    @Test
    void testRegisterUserSuccess() throws Exception {
        SmartSpendUser user = new SmartSpendUser();
        user.setEmail("test@example.com");
        user.setPassword("12345");

        Mockito.when(userService.registerUser(Mockito.any(SmartSpendUser.class)))
                .thenReturn("✅ User registered successfully!");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\", \"password\":\"12345\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("✅ User registered successfully!"));
    }

    // ❌ Test Register - Already Exists
    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        Mockito.when(userService.registerUser(Mockito.any(SmartSpendUser.class)))
                .thenReturn("❌ User already exists!");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\", \"password\":\"12345\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("❌ User already exists!"));
    }

    // ✅ Test Login by Email - Success
    @Test
    void testLoginByEmailSuccess() throws Exception {
        SmartSpendUser user = new SmartSpendUser();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");

        Mockito.when(userService.loginByEmail("john@example.com", "12345"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\", \"password\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("✅ Login successful!"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("john"));
    }

    // ❌ Test Login by Email - Invalid
    @Test
    void testLoginByEmailInvalid() throws Exception {
        Mockito.when(userService.loginByEmail("wrong@example.com", "badpass"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"wrong@example.com\", \"password\":\"badpass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("❌ Invalid email or password!"));
    }

    // ✅ Test Login by Username - Success
    @Test
    void testLoginByUsernameSuccess() throws Exception {
        SmartSpendUser user = new SmartSpendUser();
        user.setId(2L);
        user.setUsername("mike");

        Mockito.when(userService.loginByUsername("mike", "pass123"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users/login/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"mike\", \"password\":\"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("✅ Login successful using username!"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.username").value("mike"));
    }

    // ❌ Test Login by Username - Invalid
    @Test
    void testLoginByUsernameInvalid() throws Exception {
        Mockito.when(userService.loginByUsername("unknown", "wrong"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/login/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"unknown\", \"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("❌ Invalid username or password!"));
    }
}
