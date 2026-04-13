package com.yourname.myapp.onboarding.command;

import com.yourname.myapp.onboarding.entity.OnboardingRecord;
import com.yourname.myapp.onboarding.repository.OnboardingRepository;

// 🆕 ADDED
import com.yourname.myapp.onboarding.exception.OnboardingVerificationPendingException;

public class ApproveOnboardingCommand implements Command {
    private final OnboardingRecord record;
    private final OnboardingRepository repository;

    public ApproveOnboardingCommand(OnboardingRecord record, OnboardingRepository repository) {
        this.record = record;
        this.repository = repository;
    }

    @Override
    public void execute() {

        // 🆕 ADDED SAFETY CHECK (no logic changed, only guard)
        boolean bgCleared = record.getBackgroundCheckStatus()
                == OnboardingRecord.BackgroundCheckStatus.CLEARED;

        boolean docVerified = record.getDocumentVerificationStatus()
                == OnboardingRecord.DocumentVerificationStatus.VERIFIED;

        if (!bgCleared || !docVerified) {
            throw new OnboardingVerificationPendingException();
        }

        // Step 1 → VERIFIED
        record.setVerifiedRecord(true);
        record.setPipelineStatus(OnboardingRecord.PipelineStatus.VERIFIED);
        record.addActivity("Onboarding verified successfully.");

        // Step 2 → ACTIVE
        record.setPipelineStatus(OnboardingRecord.PipelineStatus.ACTIVE_ONBOARDING);
        record.addActivity("Employee moved to ACTIVE onboarding.");

        repository.update(record);
    }
}