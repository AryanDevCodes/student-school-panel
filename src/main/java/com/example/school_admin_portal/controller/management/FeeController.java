package com.example.school_admin_portal.controller.management;

import com.example.school_admin_portal.entity.FeeEntity;
import com.example.school_admin_portal.entity.StudentEntity;
import com.example.school_admin_portal.service.FeeService;
import com.example.school_admin_portal.service.student.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/management")
public class FeeController {

    private final FeeService feeService;
    private final StudentService studentService;

    public FeeController(FeeService feeService, StudentService studentService) {
        this.feeService = feeService;
        this.studentService = studentService;
    }

    @GetMapping("/fees")
    public String listAllFees(Model model) {
        // Get all students and their fee records
        List<StudentEntity> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "management/fees";
    }

    @GetMapping("/fees/student/{studentId}")
    public String viewStudentFees(@PathVariable Long studentId, Model model) {
        StudentEntity student = studentService.getStudentById(studentId);
        List<FeeEntity> fees = feeService.getFeesByStudentId(studentId);

        model.addAttribute("student", student);
        model.addAttribute("fees", fees);
        model.addAttribute("totalAmount", feeService.getTotalFeeAmount(studentId));
        model.addAttribute("paidAmount", feeService.getPaidAmount(studentId));
        model.addAttribute("pendingAmount", feeService.getPendingAmount(studentId));

        return "management/student-fees";
    }

    @GetMapping("/fees/create")
    public String showCreateFeeForm(@RequestParam(required = false) Long studentId, Model model) {
        FeeEntity fee = new FeeEntity();
        if (studentId != null) {
            fee.setStudentId(studentId); // Ensure the form-backing bean has the correct studentId
            model.addAttribute("selectedStudentId", studentId);
            model.addAttribute("selectedStudent", studentService.getStudentById(studentId));
        }
        model.addAttribute("fee", fee);
        model.addAttribute("students", studentService.getAllStudents());
        return "management/create-fee";
    }

    @GetMapping("/fees/edit")
    public String showEditForm(@RequestParam(required = false) Long receiptId, Model model) {
        FeeEntity fee = feeService.getFeeById(receiptId);
        model.addAttribute("fee", fee);
        return "management/edit-fee";
    }


    @PostMapping("/fees/create")
    public String createFee(@ModelAttribute FeeEntity fee, RedirectAttributes redirectAttributes) {
        try {
            fee.setStatus("Pending"); // Default status
            feeService.saveFee(fee);
            redirectAttributes.addFlashAttribute("success", "Fee record created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating fee: " + e.getMessage());
        }
        return "redirect:/management/fees/student/" + fee.getStudentId();
    }

    @GetMapping("/fees/edit/{id}")
    public String showEditFeeForm(@PathVariable Long id, Model model) {
        try {
            FeeEntity fee = feeService.getFeeById(id);
            model.addAttribute("fee", fee);
            model.addAttribute("student", studentService.getStudentById(fee.getStudentId()));
            return "management/edit-fees";
        } catch (Exception e) {
            throw new RuntimeException("Fee record not found with id: " + id);
        }
    }

    @PostMapping("/fees/edit/{id}")
    public String updateFee(@PathVariable Long id, @ModelAttribute FeeEntity fee, RedirectAttributes redirectAttributes) {
        try {
            fee.setId(id);
            feeService.saveFee(fee);
            redirectAttributes.addFlashAttribute("success", "Fee record updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating fee: " + e.getMessage());
        }
        return "redirect:/management/fees/student/" + fee.getStudentId();
    }

    @GetMapping("/fees/delete/{id}")
    public String deleteFee(@PathVariable Long id, @RequestParam Long studentId, RedirectAttributes redirectAttributes) {
        try {
            feeService.deleteFee(id);
            redirectAttributes.addFlashAttribute("success", "Fee record deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting fee: " + e.getMessage());
        }
        return "redirect:/management/fees/student/" + studentId;
    }

    @PostMapping("/fees/pay/{id}")
    public String payFee(@PathVariable Long id, @RequestParam Long studentId, RedirectAttributes redirectAttributes) {
        try {
            feeService.payFee(id);
            redirectAttributes.addFlashAttribute("success", "Fee payment recorded successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error recording payment: " + e.getMessage());
        }
        return "redirect:/management/fees/student/" + studentId;
    }

    @GetMapping("/fees/bulk-create")
    public String showBulkCreateForm(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "management/bulk-create-fees";
    }

    @PostMapping("/fees/bulk-create")
    public String bulkCreateFees(@RequestParam String feeType,
                                 @RequestParam Double amount,
                                 @RequestParam String dueDate,
                                 @RequestParam String semester,
                                 @RequestParam String academicYear,
                                 @RequestParam(required = false) List<Long> studentIds,
                                 RedirectAttributes redirectAttributes) {
        try {
            LocalDate due = LocalDate.parse(dueDate);
            List<Long> targetStudents = studentIds != null ? studentIds :
                    studentService.getAllStudents().stream().map(StudentEntity::getId).toList();

            for (Long studentId : targetStudents) {
                FeeEntity fee = new FeeEntity();
                fee.setStudentId(studentId);
                fee.setFeeType(feeType);
                fee.setAmount(amount);
                fee.setDueDate(due);
                fee.setSemester(semester);
                fee.setAcademicYear(academicYear);
                fee.setStatus("Pending");
                feeService.saveFee(fee);
            }

            redirectAttributes.addFlashAttribute("success",
                    "Fee records created for " + targetStudents.size() + " students");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating bulk fees: " + e.getMessage());
        }
        return "redirect:/management/fees";
    }
}
