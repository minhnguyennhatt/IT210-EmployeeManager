package com.employeemanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String avatar;    // đường dẫn hoặc tên file ảnh đại diện
    private String status;    // "Active", "Inactive",...

    // Quan hệ n-1: nhiều nhân viên thuộc một phòng ban
    // FetchType.LAZY để tránh load dữ liệu không cần thiết ngay lập tức
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")  // tên khoá ngoại trong bảng employees
    private Department department;

    // Constructors
    public Employee() {}

    public Employee(String name, int age, String avatar, String status) {
        this.name = name;
        this.age = age;
        this.avatar = avatar;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
