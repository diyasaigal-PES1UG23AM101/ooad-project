package com.yourname.myapp.onboarding.service;

import com.yourname.myapp.recruitment.entity.Candidate;
import com.yourname.myapp.recruitment.repository.CandidateRepository;
import com.yourname.myapp.recruitment.repository.CandidateRepositoryImpl;

import com.yourname.myapp.entity.Employee;
import com.yourname.myapp.service.EmployeeService;

import com.yourname.myapp.onboarding.command.*;
import com.yourname.myapp.onboarding.entity.OnboardingRecord;
import com.yourname.myapp.onboarding.repository.OnboardingRepository;
import com.yourname.myapp.onboarding.repository.OnboardingRepositoryImpl;

// 🔥 ADDED
import com.yourname.myapp.onboarding.exception.*;

import java.util.*;

public class OnboardingServiceImpl implements OnboardingService {

    private final OnboardingRepository repository = new OnboardingRepositoryImpl();
    private final CommandInvoker invoker = new CommandInvoker();

    private final CandidateRepository candidateRepository = new CandidateRepositoryImpl();
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    public List<OnboardingRecord> getAllRecords() {
        return repository.findAll();
    }

    @Override
    public OnboardingRecord getById(String id) {
        OnboardingRecord record = repository.findById(id);
        if (record == null) {
            throw new NoSuchElementException("Onboarding record not found: " + id);
        }
        return record;
    }

    @Override
    public OnboardingRecord createRecord(String candidateId) {

        if (candidateId == null || candidateId.isBlank()) {
            throw new IllegalArgumentException("Candidate ID cannot be empty");
        }

        Candidate candidate = candidateRepository.findById(candidateId);

        if (candidate == null) {
            throw new RuntimeException("Candidate does not exist with ID: " + candidateId);
        }

        // 🔥 existing rule (UNCHANGED)
        if (candidate.getApplicationStatus() != Candidate.ApplicationStatus.SELECTED) {
            throw new IllegalStateException(
                "Only SELECTED candidates can be moved to onboarding"
            );
        }

        // 🆕 ADDED: duplicate onboarding check
        boolean exists = repository.findAll().stream()
                .anyMatch(r -> r.getAssignedEmployeeId().equals(candidateId));

        if (exists) {
            throw new DuplicateCandidateOnboardingException(candidateId);
        }

        OnboardingRecord record = new OnboardingRecord();

        long count = repository.countAll() + 1;
        record.setOnboardingId(String.format("ONB-%03d", count));

        record.setAssignedEmployeeId(candidate.getCandidateId());
        record.setEmployeeName(candidate.getCandidateName());

        record.setPipelineStatus(OnboardingRecord.PipelineStatus.EMPLOYEE_ASSIGNED);
        record.setBackgroundCheckStatus(OnboardingRecord.BackgroundCheckStatus.PENDING);
        record.setDocumentVerificationStatus(OnboardingRecord.DocumentVerificationStatus.PENDING);
        record.setVerifiedRecord(false);

        record.addActivity("Onboarding started from candidate: " + candidate.getCandidateName());

        repository.save(record);

        return record;
    }

    @Override
    public void updateBackgroundCheck(String id, String status) {

        OnboardingRecord record = getById(id);

        if (record.isVerifiedRecord()) {
            throw new InvalidCandidateOnboardingStateException(
                "Cannot update background check after verification"
            );
        }

        OnboardingRecord.BackgroundCheckStatus newStatus =
                OnboardingRecord.BackgroundCheckStatus.valueOf(status);

        invoker.executeCommand(
                new UpdateBackgroundCheckCommand(record, newStatus, repository)
        );
    }

    @Override
    public void updateDocumentVerification(String id, String status) {

        OnboardingRecord record = getById(id);

        if (record.isVerifiedRecord()) {
            throw new InvalidCandidateOnboardingStateException(
                "Cannot update document verification after verification"
            );
        }

        OnboardingRecord.DocumentVerificationStatus newStatus =
                OnboardingRecord.DocumentVerificationStatus.valueOf(status);

        invoker.executeCommand(
                new UpdateDocumentVerificationCommand(record, newStatus, repository)
        );
    }

    @Override
    public void approveOnboarding(String id) {

        OnboardingRecord record = getById(id);

        // 🆕 ADDED: prevent re-approval
        if (record.isVerifiedRecord()) {
            throw new OnboardingAlreadyVerifiedException(id);
        }

        invoker.executeCommand(
                new ApproveOnboardingCommand(record, repository)
        );

        Candidate candidate = candidateRepository.findById(record.getAssignedEmployeeId());

        if (candidate != null) {
            com.yourname.myapp.dto.EmployeeRequest req =
                    new com.yourname.myapp.dto.EmployeeRequest();

            req.setEmployeeName(candidate.getCandidateName());
            req.setDepartment("DEFAULT");
            req.setJobRole("NEW_JOINER");

            employeeService.createEmployee(req);
        }
    }

    @Override
    public void deleteRecord(String id) {
        getById(id);
        repository.delete(id);
    }

    @Override
    public Map<String, Object> getOnboardingStats() {

        List<OnboardingRecord> records = repository.findAll();

        long total = records.size();

        long active = records.stream()
                .filter(r -> r.getPipelineStatus() == OnboardingRecord.PipelineStatus.ACTIVE_ONBOARDING)
                .count();

        long verified = records.stream()
                .filter(r -> r.getPipelineStatus() == OnboardingRecord.PipelineStatus.VERIFIED)
                .count();

        long pending = records.stream()
                .filter(r -> r.getPipelineStatus() == OnboardingRecord.PipelineStatus.EMPLOYEE_ASSIGNED)
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", total);
        stats.put("activeOnboarding", active);
        stats.put("verified", verified);
        stats.put("pending", pending);

        return stats;
    }
}