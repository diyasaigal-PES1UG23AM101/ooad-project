package com.yourname.myapp.repository;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.entity.LeaveBalance;
import org.hibernate.Session;
import java.util.Optional;

public class LeaveBalanceRepository {

    public void save(LeaveBalance balance) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(balance);
            session.getTransaction().commit();
        }
    }

    public Optional<LeaveBalance> findByEmployeeId(String employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM LeaveBalance WHERE employeeId = :eid", LeaveBalance.class)
                    .setParameter("eid", employeeId).uniqueResultOptional();
        }
    }
}