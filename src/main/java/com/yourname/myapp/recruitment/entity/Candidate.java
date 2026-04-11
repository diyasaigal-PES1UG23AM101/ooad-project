package com.yourname.myapp.recruitment.entity;

import jakarta.persistence.*;

/**
 * Candidate Entity - Represents a job candidate in the Recruitment & ATS system
 * 
 * This JPA entity models candidates who apply for positions.
 * Each candidate progresses through application statuses: APPLIED → SHORTLISTED → INTERVIEW → SELECTED/REJECTED
 * 
 * Auto-generated IDs: CND-001, CND-002, etc.
 * Database Table: candidate
 * 
 * @author OOAD Project
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "candidate")
public class Candidate {
    public Candidate() {}
    
    /**
     * Unique identifier for the candidate
     * Format: CND-XXX (auto-generated)
     * Primary Key in database
     */
    @Id
    @Column(name = "candidate_id")
    private String candidateId;

    /** Full name of the candidate (required) */
    @Column(name = "candidate_name", nullable = false)
    private String candidateName;

    /** Contact information - email or phone number (required) */
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    /** Resume data - URL or embedded resume content (required) */
    @Column(name = "resume_data", nullable = false)
    private String resumeData;

    /** Interview performance score (0-100 scale, optional) */
    @Column(name = "interview_score")
    private double interviewScore;

    /** Current application status in the recruitment workflow (required) */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ApplicationStatus applicationStatus;

    /**
     * Application Status Enum - Represents the candidate's current position in recruitment workflow
     * 
     * Valid Status Transitions:
     * - APPLIED: Initial status when candidate applies
     * - SHORTLISTED: After passing initial screening
     * - INTERVIEW: Candidate is being interviewed
     * - SELECTED: Final stage - candidate selected for hire
     * - REJECTED: Candidate not selected (terminal state)
     * 
     * Transition Rules (enforced by CandidateServiceImpl):
     * APPLIED -> [SHORTLISTED, REJECTED]
     * SHORTLISTED -> [INTERVIEW, SELECTED, REJECTED]
     * INTERVIEW -> [SELECTED, REJECTED]
     * SELECTED -> [REJECTED]
     * REJECTED -> []  (no transitions from rejected)
     */
    public enum ApplicationStatus {
        APPLIED,        // Initial application state
        SHORTLISTED,    // Passed screening
        INTERVIEW,      // In interview process
        SELECTED,       // Selected for hire
        REJECTED        // Application rejected (terminal state)
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getResumeData() {
        return resumeData;
    }

    public void setResumeData(String resumeData) {
        this.resumeData = resumeData;
    }

    public double getInterviewScore() {
        return interviewScore;
    }

    public void setInterviewScore(double interviewScore) {
        this.interviewScore = interviewScore;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId='" + candidateId + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", resumeData='" + resumeData + '\'' +
                ", interviewScore=" + interviewScore +
                ", applicationStatus=" + applicationStatus +
                '}';
    }
}
