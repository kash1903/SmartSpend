package com.example.smartSpend.smartExpenseTracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Expense {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;   // e.g. Food, Travel, Health
    private String type;       // EXPENSE or INCOME
    private Double amount;
    private LocalDate date;
    private String description;

    // Each expense belongs to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SmartSpendUser user;
}
