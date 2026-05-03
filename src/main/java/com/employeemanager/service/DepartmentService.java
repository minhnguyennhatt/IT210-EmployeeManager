package com.employeemanager.service;

import com.employeemanager.entity.Department;
import com.employeemanager.entity.Employee;
import com.employeemanager.repository.DepartmentRepository;
import com.employeemanager.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public int deleteDepartmentAndUnassignEmployees(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);
        for (Employee employee : employees) {
            employee.setDepartment(null);
        }
        if (!employees.isEmpty()) {
            employeeRepository.saveAll(employees);
        }

        departmentRepository.delete(department);
        return employees.size();
    }
}

