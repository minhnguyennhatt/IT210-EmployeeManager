package com.employeemanager.controller;

import com.employeemanager.entity.Employee;
import com.employeemanager.repository.DepartmentRepository;
import com.employeemanager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public EmployeeController(EmployeeRepository employeeRepository,
                              DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // Redirect trang chủ về danh sách nhân viên
    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employees";
    }

    // Hiển thị form thêm mới
    @GetMapping("/employees/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "add-employee";
    }

    // Xử lý submit form thêm mới
    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute("employee") Employee employee,
                              @RequestParam("file") MultipartFile file) throws IOException {
        // 1. Xử lý upload file nếu có
        if (!file.isEmpty()) {
            // Tạo tên file duy nhất
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;

            // Lưu file vào thư mục uploads (tạo nếu chưa có)
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Lưu tên file vào employee
            employee.setAvatar(newFileName);
        }

        // 2. Xử lý quan hệ Department
        // Lúc này employee.getDepartment() chỉ có id (do binding từ form), cần load đầy đủ
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Long deptId = employee.getDepartment().getId();
            departmentRepository.findById(deptId).ifPresent(employee::setDepartment);
        } else {
            // nếu không chọn phòng ban, set null để tránh lỗi
            employee.setDepartment(null);
        }

        // 3. Lưu employee
        employeeRepository.save(employee);

        return "redirect:/employees";
    }
}
