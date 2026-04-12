package com.yourname.myapp.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.entity.Claim;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class ClaimRepository {

    public void save(Claim claim) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(claim);
            session.getTransaction().commit();
        }
    }

    public List<Claim> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Claim", Claim.class).list();
        }
    }

    public List<Claim> findByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Claim WHERE employeeId = :eid", Claim.class)
                    .setParameter("eid", employeeId).list();
        }
    }

    public Optional<Claim> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Claim.class, id));
        }
    }
}
