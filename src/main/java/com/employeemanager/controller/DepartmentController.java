package com.employeemanager.controller;

import com.employeemanager.repository.DepartmentRepository;
import com.employeemanager.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentRepository departmentRepository,
                                DepartmentService departmentService) {
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
    }

    @GetMapping("/departments")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "departments";
    }

    @PostMapping("/departments/{id}/delete")
    public String deleteDepartment(@PathVariable("id") Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            int updatedEmployees = departmentService.deleteDepartmentAndUnassignEmployees(id);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Đã xóa phòng ban và cập nhật trạng thái cho " + updatedEmployees + " nhân viên."
            );
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng ban cần xóa.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa phòng ban. Vui lòng thử lại.");
        }
        return "redirect:/departments";
    }
}

