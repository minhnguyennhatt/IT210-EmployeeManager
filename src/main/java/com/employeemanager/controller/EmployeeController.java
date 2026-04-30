package com.employeemanager.controller;

import com.employeemanager.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public String redirectToEmployees() {
        return "redirect:/employees";
    }

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        // findAll() trả về tất cả nhân viên (Fetch LAZY -> department chỉ load khi cần)
        model.addAttribute("employees", employeeRepository.findAll());
        return "employees";  // Trả về view employees.html
    }
}
