package com.yourname.myapp.performance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @Column(name = "promotion_id")
    private String promotionId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "new_role", nullable = false)
    private String newRole;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
