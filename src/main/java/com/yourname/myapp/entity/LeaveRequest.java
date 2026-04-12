package com.yourname.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 20)
    private String employeeId;

    @Column(name = "leave_from_date", nullable = false)
    private LocalDate leaveFromDate;

    @Column(name = "leave_to_date", nullable = false)
    private LocalDate leaveToDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", nullable = false)
    private LeaveStatus leaveStatus;

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED
    }

    public Long getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public LocalDate getLeaveFromDate() { return leaveFromDate; }
    public LocalDate getLeaveToDate() { return leaveToDate; }
    public LeaveStatus getLeaveStatus() { return leaveStatus; }

    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setLeaveFromDate(LocalDate leaveFromDate) { this.leaveFromDate = leaveFromDate; }
    public void setLeaveToDate(LocalDate leaveToDate) { this.leaveToDate = leaveToDate; }
    public void setLeaveStatus(LeaveStatus leaveStatus) { this.leaveStatus = leaveStatus; }

    public static LeaveRequestBuilder builder() { return new LeaveRequestBuilder(); }

    public static class LeaveRequestBuilder {
        private String employeeId;
        private LocalDate leaveFromDate;
        private LocalDate leaveToDate;
        private LeaveStatus leaveStatus;

        public LeaveRequestBuilder employeeId(String e) { this.employeeId = e; return this; }
        public LeaveRequestBuilder leaveFromDate(LocalDate d) { this.leaveFromDate = d; return this; }
        public LeaveRequestBuilder leaveToDate(LocalDate d) { this.leaveToDate = d; return this; }
        public LeaveRequestBuilder leaveStatus(LeaveStatus s) { this.leaveStatus = s; return this; }

        public LeaveRequest build() {
            LeaveRequest r = new LeaveRequest();
            r.employeeId = this.employeeId;
            r.leaveFromDate = this.leaveFromDate;
            r.leaveToDate = this.leaveToDate;
            r.leaveStatus = this.leaveStatus;
            return r;
        }
    }
}