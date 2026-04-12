package com.yourname.myapp.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.entity.AttendanceRecord;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AttendanceRepository {

    public void save(AttendanceRecord record) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(record);
            session.getTransaction().commit();
        }
    }

    public List<AttendanceRecord> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AttendanceRecord", AttendanceRecord.class).list();
        }
    }

    public List<AttendanceRecord> findByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AttendanceRecord WHERE employeeId = :eid", AttendanceRecord.class)
                    .setParameter("eid", employeeId).list();
        }
    }

    public List<AttendanceRecord> findByDateRange(LocalDate from, LocalDate to) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AttendanceRecord WHERE attendanceDate BETWEEN :from AND :to", AttendanceRecord.class)
                    .setParameter("from", from).setParameter("to", to).list();
        }
    }

    public Optional<AttendanceRecord> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(AttendanceRecord.class, id));
        }
    }
}