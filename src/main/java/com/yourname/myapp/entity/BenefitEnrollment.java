package com.yourname.myapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "benefit_enrollment")
public class BenefitEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 20)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false)
    private EnrollmentStatus enrollmentStatus;

    @Column(name = "health_plan")
    private String healthPlan;

    @Column(name = "insurance_plan")
    private String insurancePlan;

    // Derived: ACTIVE if enrollmentStatus == ENROLLED, otherwise INACTIVE
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_coverage_status")
    private CoverageStatus insuranceCoverageStatus;

    public enum EnrollmentStatus {
        ENROLLED, PENDING, NOT_ENROLLED
    }

    public enum CoverageStatus {
        ACTIVE, INACTIVE
    }

    // Getters
    public Long getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public EnrollmentStatus getEnrollmentStatus() { return enrollmentStatus; }
    public String getHealthPlan() { return healthPlan; }
    public String getInsurancePlan() { return insurancePlan; }
    public CoverageStatus getInsuranceCoverageStatus() { return insuranceCoverageStatus; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
        // Auto-derive coverage status
        this.insuranceCoverageStatus = (enrollmentStatus == EnrollmentStatus.ENROLLED)
                ? CoverageStatus.ACTIVE : CoverageStatus.INACTIVE;
    }
    public void setHealthPlan(String healthPlan) { this.healthPlan = healthPlan; }
    public void setInsurancePlan(String insurancePlan) { this.insurancePlan = insurancePlan; }
    public void setInsuranceCoverageStatus(CoverageStatus insuranceCoverageStatus) {
        this.insuranceCoverageStatus = insuranceCoverageStatus;
    }
}
