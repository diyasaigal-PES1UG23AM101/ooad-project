package com.yourname.myapp.performance.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.performance.entity.Promotion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PromotionRepositoryImpl implements PromotionRepository {
    @Override
    public List<Promotion> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Promotion", Promotion.class).getResultList();
        }
    }

    @Override
    public void save(Promotion promotion) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(promotion);
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
                                "delete from Promotion p where p.employeeId = :employeeId or upper(trim(p.employeeId)) = :normalizedEmployeeId"
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
    public int deleteOrphanedPromotions() {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                int deletedCount = session.createMutationQuery(
                                "delete from Promotion p where not exists (select 1 from Employee e where e.employeeId = p.employeeId)"
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
    public long countAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("select count(p) from Promotion p", Long.class).getSingleResult();
        }
    }
}
