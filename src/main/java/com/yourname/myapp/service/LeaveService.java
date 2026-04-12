package com.yourname.myapp.service;

import com.yourname.myapp.entity.LeaveBalance;
import com.yourname.myapp.entity.LeaveRequest;
import com.yourname.myapp.exception.InvalidDateRangeException;
import com.yourname.myapp.repository.LeaveBalanceRepository;
import com.yourname.myapp.repository.LeaveRequestRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LeaveService {

    public static class LeaveResult {
        public final boolean success;
        public final boolean lowBalanceWarning;
        public final int remainingBalance;
        public final String message;

        public LeaveResult(boolean success, boolean lowBalanceWarning, int remainingBalance, String message) {
            this.success = success;
            this.lowBalanceWarning = lowBalanceWarning;
            this.remainingBalance = remainingBalance;
            this.message = message;
        }
    }

    public LeaveResult createLeaveRequest(String employeeId, LocalDate from, LocalDate to) {
        if (!from.isBefore(to)) throw new InvalidDateRangeException("Leave start date must be before end date.");

        LeaveBalanceRepository balRepo = new LeaveBalanceRepository();
        LeaveBalance balance = balRepo.findByEmployeeId(employeeId).orElseGet(() -> {
            LeaveBalance newBalance = LeaveBalance.createDefault(employeeId);
            balRepo.save(newBalance);
            return newBalance;
        });

        LeaveRequest request = new LeaveRequest();
        request.setEmployeeId(employeeId);
        request.setLeaveFromDate(from);
        request.setLeaveToDate(to);
        request.setLeaveStatus(LeaveRequest.LeaveStatus.PENDING);
        new LeaveRequestRepository().save(request);

        boolean lowBalance = balance.getBalance() <= 2;
        return new LeaveResult(true, lowBalance, balance.getBalance(),
                lowBalance ? "Leave request submitted. Warning: low balance (" + balance.getBalance() + " days left)."
                           : "Leave request submitted successfully.");
    }

    public List<LeaveRequest> getLeavesByStatus(String status) {
        return new LeaveRequestRepository().findByStatus(status);
    }

    public List<LeaveRequest> getAllLeaves() {
        return new LeaveRequestRepository().findAll();
    }

    public long getPendingCount() {
        return new LeaveRequestRepository().countPending();
    }

    public Optional<LeaveRequest> findById(Long id) {
        return new LeaveRequestRepository().findById(id);
    }

    public int getBalance(String employeeId) {
        return new LeaveBalanceRepository().findByEmployeeId(employeeId)
                .map(LeaveBalance::getBalance).orElse(20);
    }
}