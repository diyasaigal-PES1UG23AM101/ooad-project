package com.yourname.myapp.config;

import com.yourname.myapp.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for Hibernate SessionFactory and session management.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            
            // Database configuration
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/eims_db");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "Guggulop@9");
            
            // Hibernate configuration
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.use_sql_comments", "true");
            configuration.setProperty("hibernate.jdbc.batch_size", "10");
            
            // Add entity classes
            configuration.addAnnotatedClass(Employee.class);
            
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Failed to create SessionFactory: " + e);
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Get a new Hibernate Session
     */
    public static Session getSession() {
        return sessionFactory.openSession();
    }

    /**
     * Get SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Close SessionFactory (call on application shutdown)
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
