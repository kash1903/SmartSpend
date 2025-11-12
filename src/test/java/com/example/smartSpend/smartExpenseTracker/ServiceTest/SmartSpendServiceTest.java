package com.example.smartSpend.smartExpenseTracker.ServiceTest;

import com.example.smartSpend.smartExpenseTracker.model.SmartSpendUser;
import com.example.smartSpend.smartExpenseTracker.repo.SmartSpendRepo;
import com.example.smartSpend.smartExpenseTracker.service.SmartSpendService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SmartSpendServiceTest {
       @Mock
    private SmartSpendRepo userRepo;

    @InjectMocks
    private SmartSpendService userService;

    private SmartSpendUser user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new SmartSpendUser();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("12345");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepo.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepo.save(any(SmartSpendUser.class))).thenReturn(user);

        String result = userService.registerUser(user);

        assertThat(result).contains("✅");
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepo.existsByEmail("john@example.com")).thenReturn(true);

        String result = userService.registerUser(user);

        assertThat(result).contains("❌ Email already registered");
        verify(userRepo, never()).save(any());
    }

    @Test
    void testLoginByEmail_Success() {
        when(userRepo.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<SmartSpendUser> result = userService.loginByEmail("john@example.com", "12345");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("john");
    }

    @Test
    void testLoginByEmail_WrongPassword() {
        when(userRepo.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<SmartSpendUser> result = userService.loginByEmail("john@example.com", "wrongpass");

        assertThat(result).isEmpty();
    }

    @Test
    void testLoginByEmail_UserNotFound() {
        when(userRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<SmartSpendUser> result = userService.loginByEmail("unknown@example.com", "12345");

        assertThat(result).isEmpty();
    }

    @Test
    void testLoginByUsername_Success() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<SmartSpendUser> result = userService.loginByUsername("john", "12345");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testLoginByUsername_WrongPassword() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<SmartSpendUser> result = userService.loginByUsername("john", "wrong");

        assertThat(result).isEmpty();
    }

    @Test
    void testLoginByUsername_UserNotFound() {
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<SmartSpendUser> result = userService.loginByUsername("unknown", "12345");

        assertThat(result).isEmpty();
    }
}
