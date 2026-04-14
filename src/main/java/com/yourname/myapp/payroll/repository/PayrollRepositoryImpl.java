package com.yourname.myapp.payroll.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.payroll.entity.Payroll;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class PayrollRepositoryImpl implements PayrollRepository {

    @Override
    public void save(Payroll payroll) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.save(payroll); // 🔥 REAL SAVE

            tx.commit();
            System.out.println("Saved to DB: " + payroll.getEmployee().getEmployeeId());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Payroll payroll) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.update(payroll);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Payroll> findAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Payroll", Payroll.class).list();
        }
    }

    @Override
    public Payroll findByEmployeeId(String employeeId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Payroll WHERE employee.employeeId = :empId",
                    Payroll.class
            )
            .setParameter("empId", employeeId)
            .uniqueResult();
        }
    }

    @Override
    public Payroll findByEmployeeAndMonth(String employeeId, String month, int year) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Payroll WHERE employee.employeeId = :empId AND month = :month AND year = :year",
                    Payroll.class
            )
            .setParameter("empId", employeeId)
            .setParameter("month", month)
            .setParameter("year", year)
            .uniqueResult();
        }
    }
}
