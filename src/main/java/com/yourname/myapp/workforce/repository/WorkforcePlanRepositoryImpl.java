package com.yourname.myapp.workforce.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.workforce.entity.WorkforcePlan;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class WorkforcePlanRepositoryImpl implements WorkforcePlanRepository {
    @Override
    public List<WorkforcePlan> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from WorkforcePlan", WorkforcePlan.class).getResultList();
        }
    }

    @Override
    public List<WorkforcePlan> findByQuarter(String quarter) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "from WorkforcePlan w where w.quarter = :quarter",
                            WorkforcePlan.class
                    )
                    .setParameter("quarter", quarter)
                    .getResultList();
        }
    }

    @Override
    public WorkforcePlan findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(WorkforcePlan.class, id);
        }
    }

    @Override
    public void save(WorkforcePlan plan) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(plan);
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void update(WorkforcePlan plan) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.merge(plan);
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                WorkforcePlan existing = session.get(WorkforcePlan.class, id);
                if (existing != null) {
                    session.remove(existing);
                    tx.commit();
                    return true;
                }
                tx.commit();
                return false;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }
}
