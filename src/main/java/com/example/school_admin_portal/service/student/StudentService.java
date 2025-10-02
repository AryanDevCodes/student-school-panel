package com.example.school_admin_portal.service.student;

import com.example.school_admin_portal.entity.StudentEntity;
import com.example.school_admin_portal.entity.role.Role;
import com.example.school_admin_portal.repository.student.StudentRepo;
import com.example.school_admin_portal.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService {
    private final StudentRepo studentRepo;
    private final UserService userService;

    public StudentService(StudentRepo studentRepo, UserService userService) {
        this.studentRepo = studentRepo;
        this.userService = userService;
    }

    @Override
    public List<StudentEntity> getAllStudents() {
        return studentRepo.findAll();
    }

    @Override
    public StudentEntity getStudentById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
    }

    @Override
    public StudentEntity createStudent(StudentEntity student) {
        student.setRole(Role.STUDENT);

        // Save the student first
        StudentEntity savedStudent = studentRepo.save(student);

        // Create user account with email as username and default password
        try {
            String defaultPassword = "student123"; // Default password for all students
            userService.createUser(savedStudent.getEmail(), defaultPassword, Role.STUDENT);
            System.out.println("User account created for student: " + savedStudent.getEmail());
        } catch (RuntimeException e) {
            // If user already exists, just log it (don't fail student creation)
            System.out.println("User account already exists for email: " + savedStudent.getEmail());
        }

        return savedStudent;
    }

    @Override
    public StudentEntity updateStudent(Long id, StudentEntity student) {
        StudentEntity existing = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        String oldEmail = existing.getEmail();

        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setCourse(student.getCourse());
        existing.setAge(student.getAge());
        existing.setFee(student.getFee());
        // Role is never updated - always remains STUDENT
        // existing.setRole() is intentionally omitted

        StudentEntity updatedStudent = studentRepo.save(existing);

        // If email changed, update the username in user account
        if (!oldEmail.equals(updatedStudent.getEmail())) {
            try {
                // Find the user with old email and update username
                userService.getUserByUsername(oldEmail).ifPresent(user -> {
                    try {
                        userService.updateUser(user.getId(),
                                new com.example.school_admin_portal.entity.UserEntity(user.getId(), updatedStudent.getEmail(), "", Role.STUDENT));
                        System.out.println("Updated username from " + oldEmail + " to " + updatedStudent.getEmail());
                    } catch (Exception e) {
                        System.out.println("Could not update username: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                System.out.println("Could not find user to update: " + e.getMessage());
            }
        }

        return updatedStudent;
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepo.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepo.deleteById(id);
    }

    @Override
    public List<StudentEntity> getStudentsByName(String name) {
        return studentRepo.findByNameOrderByNameAsc(name);
    }

    @Override
    public List<StudentEntity> findByFeeBetween(Double feeAfter, Double feeBefore) {
        return studentRepo.findByFeeBetween(feeAfter, feeBefore);
    }

    @Override
    public List<StudentEntity> searchStudents(String keyword) {
        return studentRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
                keyword, keyword, keyword);
    }
}
