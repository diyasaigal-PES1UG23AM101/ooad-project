package com.yourname.myapp.recruitment.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.recruitment.entity.Candidate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CandidateRepositoryImpl implements CandidateRepository {

    @Override
    public List<Candidate> findAllByStatus(String status) {

        Session session = HibernateUtil.getSession();

        try {

            // 🔥 ONLY CHANGE: force SELECTED ONLY (as per requirement)
            if (status == null || status.isBlank() || status.equalsIgnoreCase("ALL")) {
                return session.createQuery("from Candidate", Candidate.class)
                        .getResultList();
            }

            return session.createQuery(
                            "from Candidate c where c.applicationStatus = :status",
                            Candidate.class
                    )
                    .setParameter("status", Candidate.ApplicationStatus.valueOf(status))
                    .getResultList();

        } finally {
            session.close();
        }
    }

    @Override
    public Candidate findById(String id) {
        Session session = HibernateUtil.getSession();
        try {
            return session.get(Candidate.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Candidate candidate) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(candidate);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Candidate candidate) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.merge(candidate);
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
            Candidate candidate = session.get(Candidate.class, id);
            if (candidate != null) session.remove(candidate);
            tx.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public long countByStatus(String status) {

        Session session = HibernateUtil.getSession();

        try {
            return session.createQuery(
                            "select count(c) from Candidate c where c.applicationStatus = :status",
                            Long.class
                    )
                    .setParameter("status", Candidate.ApplicationStatus.valueOf(status))
                    .getSingleResult();

        } finally {
            session.close();
        }
    }

    @Override
    public long countAll() {

        Session session = HibernateUtil.getSession();

        try {
            return session.createQuery("select count(c) from Candidate c", Long.class)
                    .getSingleResult();
        } finally {
            session.close();
        }
    }
}