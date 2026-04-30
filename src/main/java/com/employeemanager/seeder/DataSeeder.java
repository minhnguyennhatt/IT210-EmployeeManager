package com.employeemanager.seeder;

import com.employeemanager.entity.Department;
import com.employeemanager.entity.Employee;
import com.employeemanager.repository.DepartmentRepository;
import com.employeemanager.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DataSeeder(DepartmentRepository departmentRepository,
                      EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Chỉ seed nếu DB chưa có dữ liệu (tránh trùng lặp khi dùng DB persistent)
        if (departmentRepository.count() == 0 && employeeRepository.count() == 0) {
            // 1. Tạo và lưu phòng ban trước
            Department dev = new Department("Development", "Tòa nhà A");
            Department hr = new Department("Human Resources", "Tòa nhà B");
            Department sales = new Department("Sales", "Tòa nhà C");

            departmentRepository.save(dev);
            departmentRepository.save(hr);
            departmentRepository.save(sales);

            // 2. Tạo nhân viên, gán phòng ban (đã có ID) rồi lưu
            Employee e1 = new Employee("Nguyễn Văn An", 30, "avatar1.png", "Active");
            e1.setDepartment(dev);
            employeeRepository.save(e1);

            Employee e2 = new Employee("Trần Thị Bình", 25, "avatar2.png", "Active");
            e2.setDepartment(dev);
            employeeRepository.save(e2);

            Employee e3 = new Employee("Lê Văn Cường", 45, "avatar3.png", "Inactive");
            e3.setDepartment(hr);
            employeeRepository.save(e3);

            Employee e4 = new Employee("Phạm Thị Dung", 28, "avatar4.png", "Active");
            e4.setDepartment(sales);
            employeeRepository.save(e4);

            System.out.println("Sample data seeded successfully.");
        }
    }
}
