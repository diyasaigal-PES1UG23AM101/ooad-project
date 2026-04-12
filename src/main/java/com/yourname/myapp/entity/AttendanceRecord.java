package com.yourname.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_record")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 20)
    private String employeeId;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "overtime_hours")
    private double overtimeHours;

    public Long getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public LocalTime getCheckInTime() { return checkInTime; }
    public LocalTime getCheckOutTime() { return checkOutTime; }
    public double getOvertimeHours() { return overtimeHours; }

    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }

    public static AttendanceRecordBuilder builder() { return new AttendanceRecordBuilder(); }

    public static class AttendanceRecordBuilder {
        private String employeeId;
        private LocalDate attendanceDate;
        private LocalTime checkInTime;
        private LocalTime checkOutTime;
        private double overtimeHours;

        public AttendanceRecordBuilder employeeId(String employeeId) { this.employeeId = employeeId; return this; }
        public AttendanceRecordBuilder attendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; return this; }
        public AttendanceRecordBuilder checkInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; return this; }
        public AttendanceRecordBuilder checkOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; return this; }
        public AttendanceRecordBuilder overtimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; return this; }

        public AttendanceRecord build() {
            AttendanceRecord r = new AttendanceRecord();
            r.employeeId = this.employeeId;
            r.attendanceDate = this.attendanceDate;
            r.checkInTime = this.checkInTime;
            r.checkOutTime = this.checkOutTime;
            r.overtimeHours = this.overtimeHours;
            return r;
        }
    }
}