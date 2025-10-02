package com.example.school_admin_portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long studentId;

    @NotNull
    @Column(nullable = false)
    private String subject;

    @NotNull
    @Column(nullable = false)
    private Integer credits;

    @NotNull
    @Column(nullable = false)
    private String grade; // A, B+, B, C, etc.

    @NotNull
    @Column(nullable = false)
    private Double points; // Grade points (e.g., 9.2, 8.5)

    @NotNull
    @Column(nullable = false)
    private String semester;

    @NotNull
    @Column(nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private String status = "Passed"; // Passed, Failed, In Progress
}
