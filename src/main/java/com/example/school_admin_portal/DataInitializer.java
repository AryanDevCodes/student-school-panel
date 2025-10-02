package com.example.school_admin_portal;

import com.example.school_admin_portal.entity.FeeEntity;
import com.example.school_admin_portal.entity.GradeEntity;
import com.example.school_admin_portal.entity.StudentEntity;
import com.example.school_admin_portal.entity.role.Role;
import com.example.school_admin_portal.repository.UserRepository;
import com.example.school_admin_portal.service.FeeService;
import com.example.school_admin_portal.service.GradeService;
import com.example.school_admin_portal.service.UserService;
import com.example.school_admin_portal.service.student.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class DataInitializer {

    private final StudentService studentService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final GradeService gradeService;
    private final FeeService feeService;

    public DataInitializer(StudentService studentService, UserService userService, UserRepository userRepository,
                           GradeService gradeService, FeeService feeService) {
        this.studentService = studentService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.gradeService = gradeService;
        this.feeService = feeService;
    }

    @PostConstruct
    public void init() {
        // Initialize users first (only admin and school users, not student users)
        if (userRepository.count() == 0) {
            userService.createUser("admin", "admin123", Role.MANAGEMENT);
            userService.createUser("school1", "school123", Role.SCHOOL);
            System.out.println("Initial users created: admin/admin123, school1/school123");
        }

        // Update any existing students that might have NULL role values
        List<StudentEntity> allStudents = studentService.getAllStudents();
        for (StudentEntity student : allStudents) {
            if (student.getRole() == null) {
                student.setRole(Role.STUDENT);
                try {
                    studentService.updateStudent(student.getId(), student);
                    System.out.println("Updated role for existing student: " + student.getEmail());
                } catch (Exception e) {
                    System.out.println("Could not update role for student: " + student.getEmail() + " - " + e.getMessage());
                }
            }
        }

        // Initialize students using StudentService (which automatically creates user accounts with email as username)
        if (studentService.getAllStudents().isEmpty()) {
            // Create students with individual passwords
            createStudentWithPassword("Aarav Sharma", "aarav.sharma@example.com", "Computer Science", 20, 1500.0, "aarav123");
            createStudentWithPassword("Vihaan Patel", "vihaan.patel@example.com", "Mechanical Engineering", 21, 1400.0, "vihaan123");
            createStudentWithPassword("Ishaan Singh", "ishaan.singh@example.com", "Electrical Engineering", 22, 1350.0, "ishaan123");
            createStudentWithPassword("Advika Nair", "advika.nair@example.com", "Civil Engineering", 20, 1200.0, "advika123");
            createStudentWithPassword("Anaya Reddy", "anaya.reddy@example.com", "Mathematics", 19, 1100.0, "anaya123");
            createStudentWithPassword("Vivaan Gupta", "vivaan.gupta@example.com", "Physics", 23, 1250.0, "vivaan123");
            createStudentWithPassword("Aadhya Kumar", "aadhya.kumar@example.com", "Chemistry", 21, 1300.0, "aadhya123");
            createStudentWithPassword("Arjun Mehta", "arjun.mehta@example.com", "Biology", 22, 1150.0, "arjun123");
            createStudentWithPassword("Diya Joshi", "diya.joshi@example.com", "Computer Science", 20, 1450.0, "diya123");
            createStudentWithPassword("Krishna Das", "krishna.das@example.com", "Mechanical Engineering", 21, 1380.0, "krishna123");
            createStudentWithPassword("Mira Kapoor", "mira.kapoor@example.com", "Electrical Engineering", 22, 1320.0, "mira123");
            createStudentWithPassword("Reyansh Singh", "reyansh.singh@example.com", "Civil Engineering", 20, 1250.0, "reyansh123");
            createStudentWithPassword("Saanvi Jain", "saanvi.jain@example.com", "Mathematics", 19, 1180.0, "saanvi123");
            createStudentWithPassword("Aarohi Verma", "aarohi.verma@example.com", "Physics", 23, 1270.0, "aarohi123");
            createStudentWithPassword("Kabir Sinha", "kabir.sinha@example.com", "Chemistry", 21, 1330.0, "kabir123");
            createStudentWithPassword("Prisha Choudhary", "prisha.choudhary@example.com", "Biology", 22, 1190.0, "prisha123");
            createStudentWithPassword("Rudra Yadav", "rudra.yadav@example.com", "Computer Science", 20, 1460.0, "rudra123");
            createStudentWithPassword("Myra Bhatt", "myra.bhatt@example.com", "Mechanical Engineering", 21, 1390.0, "myra123");
            createStudentWithPassword("Ayaan Tripathi", "ayaan.tripathi@example.com", "Electrical Engineering", 22, 1340.0, "ayaan123");
            createStudentWithPassword("Anvi Pandey", "anvi.pandey@example.com", "Civil Engineering", 20, 1210.0, "anvi123");

            System.out.println("Initial students created with individual passwords and user accounts automatically assigned");
        }

        // Create sample grades and fee records for the first few students
        createSampleAcademicData();
    }

    private void createStudentWithPassword(String name, String email, String course, Integer age, Double fee, String password) {
        // Create the student entity
        StudentEntity student = new StudentEntity(null, name, email, course, age, fee, null);
        StudentEntity savedStudent = studentService.createStudent(student);

        // Updating the user account with the specific password using savedStudent data
        try {
            userService.getUserByUsername(savedStudent.getEmail()).ifPresent(user -> {
                try {
                    // Update with the specific password
                    userService.updateUser(user.getId(),
                            new com.example.school_admin_portal.entity.UserEntity(user.getId(), savedStudent.getEmail(), password, Role.STUDENT));
                    System.out.println("Password set for student: " + savedStudent.getEmail());
                } catch (Exception e) {
                    System.out.println("Could not set password for: " + savedStudent.getEmail() + " - " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("Could not find user to update password: " + e.getMessage());
        }
    }

    private void createSampleAcademicData() {
        List<StudentEntity> students = studentService.getAllStudents();

        // Create sample data for first 3 students
        for (int i = 0; i < Math.min(3, students.size()); i++) {
            StudentEntity student = students.get(i);
            createSampleGrades(student.getId());
            createSampleFees(student.getId());
        }

        System.out.println("Sample academic data created for students");
    }

    private void createSampleGrades(Long studentId) {
        // Semester 1 grades
        gradeService.saveGrade(new GradeEntity(null, studentId, "Data Structures", 4, "A", 9.2, "Semester 1", "2024-25", "Passed"));
        gradeService.saveGrade(new GradeEntity(null, studentId, "Database Management", 3, "A-", 8.8, "Semester 1", "2024-25", "Passed"));
        gradeService.saveGrade(new GradeEntity(null, studentId, "Web Development", 4, "B+", 8.5, "Semester 1", "2024-25", "Passed"));
        gradeService.saveGrade(new GradeEntity(null, studentId, "Mathematics III", 3, "B", 7.8, "Semester 1", "2024-25", "Passed"));
        gradeService.saveGrade(new GradeEntity(null, studentId, "Software Engineering", 3, "A-", 8.2, "Semester 1", "2024-25", "Passed"));
    }

    private void createSampleFees(Long studentId) {
        // Current semester fees
        feeService.saveFee(new FeeEntity(null, studentId, "Tuition Fee", 10500.0, LocalDate.of(2024, 10, 15), "Paid", LocalDate.of(2024, 10, 12), "RCP001234", "Semester 1", "2024-25"));
        feeService.saveFee(new FeeEntity(null, studentId, "Library Fee", 1500.0, LocalDate.of(2024, 10, 15), "Paid", LocalDate.of(2024, 10, 12), "RCP001235", "Semester 1", "2024-25"));
        feeService.saveFee(new FeeEntity(null, studentId, "Lab Fee", 2250.0, LocalDate.of(2024, 12, 15), "Pending", null, null, "Semester 1", "2024-25"));
        feeService.saveFee(new FeeEntity(null, studentId, "Sports Fee", 750.0, LocalDate.of(2024, 12, 15), "Pending", null, null, "Semester 1", "2024-25"));
        feeService.saveFee(new FeeEntity(null, studentId, "Registration Fee", 500.0, LocalDate.of(2024, 9, 5), "Paid", LocalDate.of(2024, 9, 5), "RCP001200", "Semester 1", "2024-25"));
    }
}
