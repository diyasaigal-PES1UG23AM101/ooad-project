package com.yourname.myapp.performance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "appraisal")
public class Appraisal {

    @Id
    @Column(name = "appraise_id")
    private String appraiseId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "feedback")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "appraisal_status", nullable = false)
    private AppraisalStatus appraisalStatus;

    @Column(name = "deadline_date", nullable = false)
    private LocalDate deadlineDate;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    public enum AppraisalStatus {
        PENDING,
        COMPLETED
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public AppraisalStatus getAppraisalStatus() {
        return appraisalStatus;
    }

    public void setAppraisalStatus(AppraisalStatus appraisalStatus) {
        this.appraisalStatus = appraisalStatus;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
