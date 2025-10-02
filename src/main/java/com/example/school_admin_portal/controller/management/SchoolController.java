package com.example.school_admin_portal.controller.management;

import com.example.school_admin_portal.entity.SchoolEntity;
import com.example.school_admin_portal.service.management.ManagementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SchoolController {

    private final ManagementService managementService;

    public SchoolController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping("/schools")
    public String getAllSchools(Model model) {
        model.addAttribute("schools", managementService.findAllSchools());
        return "management/schools";
    }

    @GetMapping("/schools/{id}")
    public String getSchoolById(@PathVariable Long id, Model model) {
        model.addAttribute("school", managementService.findSchoolById(id));
        return "management/school-details";
    }

    @GetMapping("/schools/new")
    public String showCreateSchoolForm(Model model) {
        model.addAttribute("school", new SchoolEntity());
        return "management/create-school";
    }

    @PostMapping("/schools")
    public String createSchool(@ModelAttribute SchoolEntity school) {
        managementService.saveSchool(school);
        return "redirect:/schools";
    }

    @GetMapping("/schools/edit/{id}")
    public String showEditSchoolForm(@PathVariable Long id, Model model) {
        model.addAttribute("school", managementService.findSchoolById(id));
        return "management/edit-school";
    }

    @PostMapping("/schools/{id}")
    public String updateSchool(@PathVariable Long id, @Valid @ModelAttribute SchoolEntity school) {
        managementService.updateSchool(id, school);
        return "redirect:/schools";
    }

    @GetMapping("/schools/delete/{id}")
    public String deleteSchool(@PathVariable Long id) {
        managementService.deleteSchoolById(id);
        return "redirect:/schools";
    }

}
