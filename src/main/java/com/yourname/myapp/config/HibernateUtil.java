package com.yourname.myapp.config;

import com.yourname.myapp.entity.Employee;
import com.yourname.myapp.recruitment.entity.Candidate; // Entity field in recruitments Dhrithi 
import com.yourname.myapp.onboarding.entity.OnboardingRecord; // Dhrithi 
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.yourname.myapp.entity.AttendanceRecord;
import com.yourname.myapp.entity.LeaveRequest;
import com.yourname.myapp.entity.LeaveBalance;
import com.yourname.myapp.entity.BenefitEnrollment;
import com.yourname.myapp.entity.Claim;
import com.yourname.myapp.payroll.entity.Payroll;
import com.yourname.myapp.performance.entity.Appraisal;
import com.yourname.myapp.performance.entity.Promotion;
import com.yourname.myapp.workforce.entity.WorkforcePlan;

/**
 * Utility class for Hibernate SessionFactory and session management.
 * 
 * Purpose:
 * - Initialize Hibernate session factory on application startup
 * - Provide centralized access to Hibernate sessions
 * - Configure database connection and ORM settings
 * - Register JPA entities (Employee and Candidate)
 * 
 * Database Configuration:
 * This class supports two configuration methods:
 * 
 * 1. ENVIRONMENT VARIABLES (RECOMMENDED for production):
 *    Set these environment variables before running:
 *    - DB_URL: JDBC connection URL (default: jdbc:mysql://localhost:3306/eims_db)
 *    - DB_USERNAME: MySQL username (default: root)
 *    - DB_PASSWORD: MySQL password (default: empty)
 *    
 *    Example (Linux/Mac):
 *    export DB_URL="jdbc:mysql://localhost:3306/eims_db"
 *    export DB_USERNAME="root"
 *    export DB_PASSWORD="your_password"
 *    
 *    Example (Windows):
 *    set DB_URL=jdbc:mysql://localhost:3306/eims_db
 *    set DB_USERNAME=root
 *    set DB_PASSWORD=your_password
 * 
 * 2. DEFAULT CREDENTIALS (development):
 *    If environment variables not set, uses:
 *    - URL: jdbc:mysql://localhost:3306/eims_db
 *    - Username: root
 *    - Password: (empty)
 * 
 * Auto-Schema Creation:
 * - Configured with hibernate.hbm2ddl.auto=update
 * - Tables automatically created on first run if they don't exist
 * - Existing tables updated with new columns if schema changes
 * - No manual SQL setup needed
 * 
 * Entities Registered:
 * - Employee.java: Employee management module
 * - Candidate.java: Recruitment & ATS module
 * 
 * Security Note:
 * - NEVER hardcode database credentials in source code
 * - Always use environment variables for sensitive data
 * - Credentials should never be committed to version control
 * 
 * @author OOAD Project
 * @version 1.0
 * @since 2024
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            
            // ========================================================================
            // DATABASE CONNECTION CONFIGURATION
            // ========================================================================
            // Credentials are loaded from environment variables for security
            // If not set, defaults are: root / (empty password) / localhost
            // Environment Variables (recommended):
            //   - DB_URL: JDBC connection URL
            //   - DB_USERNAME: MySQL username
            //   - DB_PASSWORD: MySQL password
            // ========================================================================
            String dbUrl = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:mysql://localhost:3306/eims_db";
            String dbUsername = System.getenv("DB_USERNAME") != null ? System.getenv("DB_USERNAME") : "root";
            String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "YourPasswordPlease";
            
            // JDBC Driver Configuration
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbUsername);
            configuration.setProperty("hibernate.connection.password", dbPassword);
            
            // ========================================================================
            // HIBERNATE ORM CONFIGURATION
            // ========================================================================
            // dialect: MySQL8Dialect for MySQL 8.0+ compatibility
            // hbm2ddl.auto=update: Auto-create/update tables on startup
            // show_sql: Log all SQL queries to console (for debugging)
            // format_sql: Pretty-print SQL queries
            // jdbc.batch_size: Batch size for bulk operations (performance optimization)
            // ========================================================================
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");  // Auto-create tables
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.use_sql_comments", "true");
            configuration.setProperty("hibernate.jdbc.batch_size", "10");
            
            // ========================================================================
            // REGISTER JPA ENTITIES
            // ========================================================================
            // Add entity classes for Hibernate to manage
            // Tables will be auto-created based on entity annotations
            // ========================================================================
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(Candidate.class);
             configuration.addAnnotatedClass(OnboardingRecord.class);
            configuration.addAnnotatedClass(AttendanceRecord.class);
            configuration.addAnnotatedClass(LeaveRequest.class);
            configuration.addAnnotatedClass(LeaveBalance.class);
            configuration.addAnnotatedClass(BenefitEnrollment.class);
            configuration.addAnnotatedClass(Claim.class);
            configuration.addAnnotatedClass(Payroll.class);
            configuration.addAnnotatedClass(Appraisal.class);
            configuration.addAnnotatedClass(Promotion.class);
            configuration.addAnnotatedClass(WorkforcePlan.class);
            
            // Build SessionFactory from configuration
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
