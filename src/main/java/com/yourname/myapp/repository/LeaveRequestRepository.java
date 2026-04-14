package com.yourname.myapp.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.entity.LeaveRequest;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeaveRequestRepository {

    public void save(LeaveRequest request) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(request);
            session.getTransaction().commit();
        }
    }

    public List<LeaveRequest> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ArrayList<>(
                session.createQuery("FROM LeaveRequest", LeaveRequest.class).list()
            );
        }
    }

    public List<LeaveRequest> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ArrayList<>(
                session.createQuery("FROM LeaveRequest WHERE leaveStatus = :status", LeaveRequest.class)
                    .setParameter("status", LeaveRequest.LeaveStatus.valueOf(status)).list()
            );
        }
    }

    public List<LeaveRequest> findByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ArrayList<>(
                session.createQuery("FROM LeaveRequest WHERE employeeId = :eid", LeaveRequest.class)
                    .setParameter("eid", employeeId).list()
            );
        }
    }

    public Optional<LeaveRequest> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(LeaveRequest.class, id));
        }
    }

    public long countPending() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT COUNT(l) FROM LeaveRequest l WHERE l.leaveStatus = 'PENDING'", Long.class)
                .getSingleResult();
        }
    }
}