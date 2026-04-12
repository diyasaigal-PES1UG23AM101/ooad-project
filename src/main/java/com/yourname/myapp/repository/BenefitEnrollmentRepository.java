package com.yourname.myapp.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.entity.BenefitEnrollment;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class BenefitEnrollmentRepository {

    public void save(BenefitEnrollment enrollment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(enrollment);
            session.getTransaction().commit();
        }
    }

    public List<BenefitEnrollment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM BenefitEnrollment", BenefitEnrollment.class).list();
        }
    }

    public List<BenefitEnrollment> findByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM BenefitEnrollment WHERE employeeId = :eid", BenefitEnrollment.class)
                    .setParameter("eid", employeeId).list();
        }
    }

    public Optional<BenefitEnrollment> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(BenefitEnrollment.class, id));
        }
    }
}
