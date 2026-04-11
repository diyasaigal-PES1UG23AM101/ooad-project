package com.yourname.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Employee entity class representing an employee in the system.
 * Uses auto-generated ID with format EMP-XXXXXXXX
 */
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(length = 20, nullable = false, updatable = false)
    private String employeeId;

    @Column(nullable = false, length = 100)
    private String employeeName;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 100)
    private String jobRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    // Constructors
    public Employee() {}

    public Employee(String employeeId, String employeeName, String department, String jobRole, EmploymentStatus employmentStatus) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.jobRole = jobRole;
        this.employmentStatus = employmentStatus;
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public String getJobRole() {
        return jobRole;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * PrePersist hook to set default values before saving
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        if (this.employmentStatus == null) {
            this.employmentStatus = EmploymentStatus.ACTIVE;
        }
        if (this.joiningDate == null) {
            this.joiningDate = LocalDate.now();
        }
    }

    /**
     * PreUpdate hook to update the lastModified timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", department='" + department + '\'' +
                ", jobRole='" + jobRole + '\'' +
                ", employmentStatus=" + employmentStatus +
                '}';
    }
}
