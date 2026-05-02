package com.employeemanager.repository;

import com.employeemanager.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Method Query: tìm kiếm theo tên (chứa từ khóa, không phân biệt hoa thường) + phân trang
    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // @Query JPQL: tìm kiếm theo tên + phân trang (cách thay thế)
    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchByName(@Param("keyword") String keyword, Pageable pageable);
}
