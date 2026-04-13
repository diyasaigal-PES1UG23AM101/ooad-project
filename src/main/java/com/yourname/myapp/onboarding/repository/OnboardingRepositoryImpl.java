package com.yourname.myapp.onboarding.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.onboarding.entity.OnboardingRecord;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OnboardingRepositoryImpl implements OnboardingRepository {

    @Override
    public List<OnboardingRecord> findAll() {
        Session session = HibernateUtil.getSession();
        try {

            // ✅ FIX: ensure no stale persistence cache
            session.clear();

            return session.createQuery("from OnboardingRecord", OnboardingRecord.class)
                    .setHint("org.hibernate.cacheMode", "REFRESH")
                    .getResultList();

        } finally {
            session.close();
        }
    }

    @Override
    public OnboardingRecord findById(String id) {
        Session session = HibernateUtil.getSession();
        try {

            // ✅ FIX: force fresh fetch
            session.clear();

            return session.get(OnboardingRecord.class, id);

        } finally {
            session.close();
        }
    }

    @Override
    public void save(OnboardingRecord record) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(record);

            // ✅ FIX: ensure DB flush before commit
            session.flush();

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void update(OnboardingRecord record) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.merge(record);

            // ✅ FIX: force write immediately
            session.flush();
            session.clear();

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(String id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        try {
            OnboardingRecord record = session.get(OnboardingRecord.class, id);
            if (record != null) session.remove(record);

            session.flush();

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public long countAll() {
        Session session = HibernateUtil.getSession();
        try {
            session.clear(); // ✅ FIX
            return session.createQuery("select count(r) from OnboardingRecord r", Long.class)
                    .getSingleResult();
        } finally {
            session.close();
        }
    }

    @Override
    public long countByPipelineStatus(String status) {
        Session session = HibernateUtil.getSession();
        try {
            session.clear(); // ✅ FIX

            return session.createQuery(
                    "select count(r) from OnboardingRecord r where r.pipelineStatus = :status",
                    Long.class
            )
            .setParameter("status", OnboardingRecord.PipelineStatus.valueOf(status))
            .getSingleResult();

        } finally {
            session.close();
        }
    }
}