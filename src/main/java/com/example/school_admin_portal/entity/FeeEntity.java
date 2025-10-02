package com.example.school_admin_portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "fee_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long studentId;

    @NotNull
    @Column(nullable = false)
    private String feeType; // Tuition, Library, Lab, Sports, etc.

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @NotNull
    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private String status = "Pending"; // Paid, Pending, Overdue

    private LocalDate paidDate;

    private String receiptNumber;

    @NotNull
    @Column(nullable = false)
    private String semester;

    @NotNull
    @Column(nullable = false)
    private String academicYear;
}
