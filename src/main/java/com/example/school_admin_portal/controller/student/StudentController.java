package com.example.school_admin_portal.controller.student;

import com.example.school_admin_portal.entity.FeeEntity;
import com.example.school_admin_portal.entity.GradeEntity;
import com.example.school_admin_portal.entity.StudentEntity;
import com.example.school_admin_portal.service.FeeService;
import com.example.school_admin_portal.service.GradeService;
import com.example.school_admin_portal.service.student.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {
    private final StudentService studentService;
    private final GradeService gradeService;
    private final FeeService feeService;

    public StudentController(StudentService studentService, GradeService gradeService, FeeService feeService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.feeService = feeService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping({"/students", "/schools"})
    public String home() {
        return "students";
    }

    private StudentEntity getCurrentStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return studentService.getAllStudents().stream()
                .filter(s -> s.getEmail().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Student Dashboard - for logged-in students to see their own info
    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model) {
        StudentEntity student = getCurrentStudent();

        // Add dashboard summary data
        model.addAttribute("student", student);
        model.addAttribute("currentGPA", gradeService.calculateGPA(student.getId()));
        model.addAttribute("totalCredits", gradeService.getTotalCreditsEarned(student.getId()));
        model.addAttribute("pendingAmount", feeService.getPendingAmount(student.getId()));
        model.addAttribute("totalFeeAmount", feeService.getTotalFeeAmount(student.getId()));

        return "student/dashboard";
    }

    // Student Profile - view own details
    @GetMapping("/student/profile")
    public String studentProfile(Model model) {
        StudentEntity student = getCurrentStudent();
        model.addAttribute("student", student);
        return "student/profile";
    }

    // Student Grades - dynamic grades data
    @GetMapping("/student/grades")
    public String studentGrades(@RequestParam(value = "semester", required = false) String semester, Model model) {
        StudentEntity student = getCurrentStudent();

        List<GradeEntity> grades;
        if (semester != null && !semester.isEmpty()) {
            grades = gradeService.getGradesByStudentIdAndSemester(student.getId(), semester);
        } else {
            grades = gradeService.getGradesByStudentId(student.getId());
        }

        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        model.addAttribute("currentGPA", gradeService.calculateGPA(student.getId()));
        model.addAttribute("totalCredits", gradeService.getTotalCreditsEarned(student.getId()));
        model.addAttribute("availableSemesters", gradeService.getAvailableSemesters(student.getId()));
        model.addAttribute("selectedSemester", semester);

        return "student/grades";
    }

    // Student Fees - dynamic fee data
    @GetMapping("/student/fees")
    public String studentFees(Model model) {
        StudentEntity student = getCurrentStudent();

        List<FeeEntity> allFees = feeService.getFeesByStudentId(student.getId());
        List<FeeEntity> pendingFees = feeService.getPendingFees(student.getId());
        List<FeeEntity> paidFees = feeService.getPaidFees(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("allFees", allFees);
        model.addAttribute("pendingFees", pendingFees);
        model.addAttribute("paidFees", paidFees);
        model.addAttribute("totalAmount", feeService.getTotalFeeAmount(student.getId()));
        model.addAttribute("paidAmount", feeService.getPaidAmount(student.getId()));
        model.addAttribute("pendingAmount", feeService.getPendingAmount(student.getId()));
        model.addAttribute("feeBreakdown", feeService.getFeeBreakdown(student.getId()));

        return "student/fees";
    }

    // View specific student details (for management)
    @GetMapping("/students/view/{id}")
    public String viewStudentById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "student/student_details";
    }

    // MANAGEMENT ENDPOINTS (existing ones)
    @GetMapping("/schools/view_students")
    public String viewStudents(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("students", studentService.searchStudents(keyword));
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        model.addAttribute("keyword", keyword);
        return "management/view_students";
    }

    @GetMapping("/schools/add-student")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new StudentEntity());
        return "management/add-student";
    }

    @PostMapping("/schools/add-student")
    public String addStudent(@ModelAttribute StudentEntity student) {
        studentService.createStudent(student);
        return "redirect:/schools/view_students";
    }

    @GetMapping("/schools/students/edit/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        StudentEntity student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "management/edit_student";
    }

    @PostMapping("/schools/students/edit/{id}")
    public String editStudent(@PathVariable("id") Long id, @ModelAttribute StudentEntity student) {
        studentService.updateStudent(id, student);
        return "redirect:/schools/view_students";
    }

    @GetMapping("/schools/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/schools/view_students";
    }

    @GetMapping("/schools/filter_students")
    public String filterStudents(@RequestParam(value = "feeAfter", required = false) Double feeAfter,
                                 @RequestParam(value = "feeBefore", required = false) Double feeBefore,
                                 Model model) {
        if (feeAfter != null && feeBefore != null) {
            model.addAttribute("students", studentService.findByFeeBetween(feeAfter, feeBefore));
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        return "management/view_students";
    }


}
