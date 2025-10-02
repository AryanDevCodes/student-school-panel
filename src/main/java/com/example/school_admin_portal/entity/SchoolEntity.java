package com.example.school_admin_portal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "School name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9\\-+]{9,15}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Principal name is required")
    private String principalName;

    @NotNull(message = "Number of students is required")
    @Min(value = 0, message = "Number of students cannot be negative")
    private Integer numberOfStudents;

    @NotNull(message = "Number of teachers is required")
    @Min(value = 0, message = "Number of teachers cannot be negative")
    private Integer numberOfTeachers;

    @NotNull(message = "Established year is required")
    @Min(value = 1800, message = "Established year must be after 1800")
    @Max(value = 2100, message = "Established year must be before 2100")
    private Integer establishedYear;

    @NotBlank(message = "Website is required")
    private String website;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Affiliation is required")
    private String affiliation; // e.g., CBSE, ICSE

    @NotBlank(message = "Medium of instruction is required")
    private String mediumOfInstruction;
}
