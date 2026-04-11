package com.yourname.myapp.recruitment.repository;

import com.yourname.myapp.recruitment.entity.Candidate;
import com.yourname.myapp.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * CandidateRepositoryImpl - Hibernate-based implementation of CandidateRepository
 * 
 * Purpose: Implement data access operations for Candidate entities using Hibernate ORM
 * 
 * Key Features:
 * - Uses Hibernate Session for database operations
 * - Implements transaction management with rollback on errors
 * - Auto-resource management with try-with-resources
 * - HQL queries for type-safe database access
 * - Proper error handling and transaction cleanup
 * 
 * Transaction Management:
 * All write operations (save, update, delete) are wrapped in transactions
 * Transactions are committed on success, rolled back on error
 * Read operations use read-only implicit transactions
 * 
 * @author OOAD Project
 * @version 1.0
 * @since 2024
 */
public class CandidateRepositoryImpl implements CandidateRepository {
    /**
     * Find all candidates, optionally filtered by status
     * 
     * Implementation Details:
     * - If status is null/"ALL": Returns all candidates
     * - If status provided: Returns candidates with matching ApplicationStatus enum value
     * - Uses HQL with parameter binding for safety
     * 
     * @param status Status filter (null for all, or specific ApplicationStatus)
     * @return List of matching candidates
     */
    @Override
    public List<Candidate> findAllByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Candidate> query;
            if (status == null) {
                // Get ALL candidates when status is null
                query = session.createQuery("FROM Candidate", Candidate.class);
            } else {
                // Get candidates with specific status
                query = session.createQuery("FROM Candidate WHERE applicationStatus = :status", Candidate.class);
                query.setParameter("status", Candidate.ApplicationStatus.valueOf(status));
            }
            return query.list();
        }
    }

    /**
     * Find a single candidate by ID
     * 
     * @param id Candidate ID (CND-XXX format)
     * @return Candidate if found, null if not found
     */
    @Override
    public Candidate findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Candidate.class, id);
        }
    }

    /**
     * Save new candidate to database
     * 
     * Transaction Management:
     * - Begin transaction before save
     * - Use session.persist() for new entity
     * - Commit on success, rollback on error
     * 
     * @param candidate Candidate to save (should not have ID yet)
     * @throws RuntimeException if database operation fails
     */
    @Override
    public void save(Candidate candidate) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(candidate);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Update existing candidate in database
     * 
     * Transaction Management:
     * - Begin transaction before update
     * - Use session.merge() to update entity
     * - Commit on success, rollback on error
     * 
     * @param candidate Candidate to update (must have ID)
     * @throws RuntimeException if database operation fails
     */
    @Override
    public void update(Candidate candidate) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(candidate);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Count candidates with specific status
     * 
     * Used by dashboard to display recruitment metrics
     * 
     * @param status ApplicationStatus to count
     * @return Number of candidates with that status
     */
    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Candidate WHERE applicationStatus = :status", Long.class);
            query.setParameter("status", Candidate.ApplicationStatus.valueOf(status));
            return query.uniqueResult();
        }
    }

    /**
     * Delete candidate from database
     * 
     * Transaction Management:
     * - Begin transaction before delete
     * - Fetch candidate and remove it
     * - Commit on success, rollback on error
     * 
     * @param id Candidate ID to delete
     * @throws RuntimeException if database operation fails
     */
    @Override
    public void delete(String id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Candidate candidate = session.get(Candidate.class, id);
            if (candidate != null) {
                session.remove(candidate);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Count total candidates in database
     * 
     * Used by system to:
     * - Generate next auto-increment candidate ID
     * - Display total applications count in dashboard
     * 
     * @return Total number of candidates
     */
    @Override
    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Candidate", Long.class);
            return query.uniqueResult();
        }
    }
}
