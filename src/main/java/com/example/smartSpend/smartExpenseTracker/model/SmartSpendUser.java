package com.example.smartSpend.smartExpenseTracker.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "smartspend_users")
@Data                   // Lombok: generates getters/setters, equals, toString
@NoArgsConstructor
@AllArgsConstructor

public class SmartSpendUser {
       @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}
