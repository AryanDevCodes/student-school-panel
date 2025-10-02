package com.example.school_admin_portal.entity;

import com.example.school_admin_portal.entity.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String course;
    private Integer age;
    @NotNull
    private Double fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role = Role.STUDENT; // Default role for students
}
