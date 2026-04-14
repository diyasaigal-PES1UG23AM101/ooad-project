package com.yourname.myapp.performance.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.performance.entity.Appraisal;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AppraisalRepositoryImpl implements AppraisalRepository {

    @Override
    public List<Appraisal> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Appraisal", Appraisal.class).getResultList();
        }
    }

    @Override
    public Appraisal findById(String id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Appraisal.class, id);
        }
    }

    @Override
    public void save(Appraisal appraisal) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(appraisal);
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void update(Appraisal appraisal) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.merge(appraisal);
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public int deleteByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                String normalizedEmployeeId = employeeId == null ? "" : employeeId.trim().toUpperCase();
                int deletedCount = session.createMutationQuery(
                                "delete from Appraisal a where a.employeeId = :employeeId or upper(trim(a.employeeId)) = :normalizedEmployeeId"
                        )
                        .setParameter("employeeId", employeeId)
                        .setParameter("normalizedEmployeeId", normalizedEmployeeId)
                        .executeUpdate();
                tx.commit();
                return deletedCount;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public int deleteOrphanedAppraisals() {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                int deletedCount = session.createMutationQuery(
                                "delete from Appraisal a where not exists (select 1 from Employee e where e.employeeId = a.employeeId)"
                        )
                        .executeUpdate();
                tx.commit();
                return deletedCount;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public long countByStatus(Appraisal.AppraisalStatus status) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "select count(a) from Appraisal a where a.appraisalStatus = :status",
                            Long.class
                    )
                    .setParameter("status", status)
                    .getSingleResult();
        }
    }

    @Override
    public long countAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("select count(a) from Appraisal a", Long.class).getSingleResult();
        }
    }
}
