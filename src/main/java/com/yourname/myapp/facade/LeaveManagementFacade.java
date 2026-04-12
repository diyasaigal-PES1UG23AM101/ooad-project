package com.yourname.myapp.facade;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.entity.LeaveBalance;
import com.yourname.myapp.entity.LeaveRequest;
import com.yourname.myapp.exception.LeaveBalanceExceededException;
import com.yourname.myapp.repository.LeaveBalanceRepository;
import com.yourname.myapp.repository.LeaveRequestRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.temporal.ChronoUnit;

public class LeaveManagementFacade {

    public void approveLeave(Long leaveRequestId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            LeaveRequest request = session.get(LeaveRequest.class, leaveRequestId);
            if (request == null) throw new RuntimeException("Leave request not found.");
            if (request.getLeaveStatus() != LeaveRequest.LeaveStatus.PENDING)
                throw new RuntimeException("Only PENDING requests can be approved.");

            long daysRequested = ChronoUnit.DAYS.between(request.getLeaveFromDate(), request.getLeaveToDate());

            LeaveBalance balance = session.createQuery("FROM LeaveBalance WHERE employeeId = :eid", LeaveBalance.class)
                    .setParameter("eid", request.getEmployeeId()).uniqueResult();

            if (balance == null) {
                balance = LeaveBalance.createDefault(request.getEmployeeId());
                session.save(balance);
            }

            if (balance.getBalance() < daysRequested)
                throw new LeaveBalanceExceededException("Insufficient balance. Available: " + balance.getBalance() + ", Requested: " + daysRequested);

            balance.setBalance(balance.getBalance() - (int) daysRequested);
            request.setLeaveStatus(LeaveRequest.LeaveStatus.APPROVED);
            session.update(balance);
            session.update(request);
            tx.commit();
        }
    }

    public void rejectLeave(Long leaveRequestId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            LeaveRequest request = session.get(LeaveRequest.class, leaveRequestId);
            if (request == null) throw new RuntimeException("Leave request not found.");
            if (request.getLeaveStatus() != LeaveRequest.LeaveStatus.PENDING)
                throw new RuntimeException("Only PENDING requests can be rejected.");
            request.setLeaveStatus(LeaveRequest.LeaveStatus.REJECTED);
            session.update(request);
            tx.commit();
        }
    }
}