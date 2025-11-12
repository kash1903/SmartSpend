package com.example.smartSpend.smartExpenseTracker.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    
       @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;     // e.g. Food, Travel, Health, etc.
    private Double limitAmount;  // Budget limit set by user
    private Double spent;        // Amount spent so far
    private Double remaining;    // Remaining amount
    private String alertStatus;  // SAFE / NEAR_LIMIT / EXCEEDED

    // Each budget belongs to a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SmartSpendUser user;
}
