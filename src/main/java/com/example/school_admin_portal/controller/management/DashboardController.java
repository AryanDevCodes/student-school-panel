package com.example.school_admin_portal.controller.management;

import com.example.school_admin_portal.service.UserService;
import com.example.school_admin_portal.service.management.ManagementService;
import com.example.school_admin_portal.service.student.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class DashboardController {

    private final StudentService studentService;
    private final ManagementService managementService;
    private final UserService userService;

    public DashboardController(StudentService studentService, ManagementService managementService, UserService userService) {
        this.studentService = studentService;
        this.managementService = managementService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String managementDashboard(Model model) {
        long totalStudents = studentService.getAllStudents().size();
        long totalSchools = managementService.findAllSchools().size();
        long totalUsers = userService.getAllUsers().size();

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalSchools", totalSchools);
        model.addAttribute("totalUsers", totalUsers);

        model.addAttribute("recentStudents", studentService.getAllStudents().stream().limit(5).toList());

        return "management/dashboard";
    }
}
