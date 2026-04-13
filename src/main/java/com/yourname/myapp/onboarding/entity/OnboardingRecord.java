package com.yourname.myapp.onboarding.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "onboarding_record")
public class OnboardingRecord {

    public OnboardingRecord() {}

    @Id
    @Column(name = "onboarding_id")
    private String onboardingId;

    @Column(name = "assigned_employee_id", nullable = false)
    private String assignedEmployeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "background_check_status", nullable = false)
    private BackgroundCheckStatus backgroundCheckStatus = BackgroundCheckStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_verification_status", nullable = false)
    private DocumentVerificationStatus documentVerificationStatus = DocumentVerificationStatus.PENDING;

    @Column(name = "verified_record", nullable = false)
    private boolean verifiedRecord = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "onboarding_activity_log", joinColumns = @JoinColumn(name = "onboarding_id"))
    @Column(name = "activity")
    // private List<String> recentActivityLog = new ArrayList<>();
    
    private List<String> recentActivityLog = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "pipeline_status", nullable = false)
    private PipelineStatus pipelineStatus = PipelineStatus.EMPLOYEE_ASSIGNED;

    public enum BackgroundCheckStatus {
        PENDING,
        CLEARED,
        FAILED
    }

    public enum DocumentVerificationStatus {
        PENDING,
        VERIFIED,
        REJECTED
    }

    public enum PipelineStatus {
        EMPLOYEE_ASSIGNED,
        BACKGROUND_CHECK,
        DOCUMENT_VERIFICATION,
        VERIFIED,
        ACTIVE_ONBOARDING
    }

    // Getters and Setters

    public String getOnboardingId() { return onboardingId; }
    public void setOnboardingId(String onboardingId) { this.onboardingId = onboardingId; }

    public String getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(String assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public BackgroundCheckStatus getBackgroundCheckStatus() { return backgroundCheckStatus; }
    public void setBackgroundCheckStatus(BackgroundCheckStatus backgroundCheckStatus) { this.backgroundCheckStatus = backgroundCheckStatus; }

    public DocumentVerificationStatus getDocumentVerificationStatus() { return documentVerificationStatus; }
    public void setDocumentVerificationStatus(DocumentVerificationStatus documentVerificationStatus) { this.documentVerificationStatus = documentVerificationStatus; }

    public boolean isVerifiedRecord() { return verifiedRecord; }
    public void setVerifiedRecord(boolean verifiedRecord) { this.verifiedRecord = verifiedRecord; }

    public List<String> getRecentActivityLog() { return recentActivityLog; }
    public void setRecentActivityLog(List<String> recentActivityLog) { this.recentActivityLog = recentActivityLog; }

    public PipelineStatus getPipelineStatus() { return pipelineStatus; }
    public void setPipelineStatus(PipelineStatus pipelineStatus) { this.pipelineStatus = pipelineStatus; }

    public void addActivity(String activity) {
        this.recentActivityLog.add(java.time.LocalDateTime.now() + " — " + activity);
    }

    @Override
    public String toString() {
        return "OnboardingRecord{" +
                "onboardingId='" + onboardingId + '\'' +
                ", assignedEmployeeId='" + assignedEmployeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", backgroundCheckStatus=" + backgroundCheckStatus +
                ", documentVerificationStatus=" + documentVerificationStatus +
                ", verifiedRecord=" + verifiedRecord +
                ", pipelineStatus=" + pipelineStatus +
                '}';
    }
}