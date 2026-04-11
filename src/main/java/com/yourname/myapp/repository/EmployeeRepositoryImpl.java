package com.yourname.myapp.repository;

import com.yourname.myapp.entity.Employee;
import com.yourname.myapp.entity.EmploymentStatus;
import com.yourname.myapp.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of EmployeeRepository using Hibernate.
 */
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    @Override
    public Employee save(Employee employee) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(employee);
            session.getTransaction().commit();
            logger.info("Employee saved: {}", employee.getEmployeeId());
            return employee;
        } catch (Exception e) {
            logger.error("Error saving employee", e);
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    @Override
    public Optional<Employee> findById(String employeeId) {
        try (Session session = HibernateUtil.getSession()) {
            Employee employee = session.get(Employee.class, employeeId);
            return Optional.ofNullable(employee);
        } catch (Exception e) {
            logger.error("Error finding employee by ID: {}", employeeId, e);
            throw new RuntimeException("Failed to find employee", e);
        }
    }

    @Override
    public List<Employee> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching all employees", e);
            throw new RuntimeException("Failed to fetch employees", e);
        }
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Employee> query = session.createQuery("FROM Employee WHERE department = :dept", Employee.class);
            query.setParameter("dept", department);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding employees by department: {}", department, e);
            throw new RuntimeException("Failed to find employees by department", e);
        }
    }

    @Override
    public List<Employee> findByEmploymentStatus(EmploymentStatus status) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Employee> query = session.createQuery("FROM Employee WHERE employmentStatus = :status", Employee.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding employees by status: {}", status, e);
            throw new RuntimeException("Failed to find employees by status", e);
        }
    }

    @Override
    public List<Employee> findByDepartmentAndEmploymentStatus(String department, EmploymentStatus status) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Employee> query = session.createQuery(
                    "FROM Employee WHERE department = :dept AND employmentStatus = :status",
                    Employee.class
            );
            query.setParameter("dept", department);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding employees by department and status", e);
            throw new RuntimeException("Failed to find employees by department and status", e);
        }
    }

    @Override
    public List<Employee> findByEmployeeNameContains(String employeeName) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Employee> query = session.createQuery(
                    "FROM Employee WHERE employeeName LIKE :name",
                    Employee.class
            );
            query.setParameter("name", "%" + employeeName + "%");
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error searching employees by name: {}", employeeName, e);
            throw new RuntimeException("Failed to search employees", e);
        }
    }

    @Override
    public boolean deleteById(String employeeId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class, employeeId);
            if (employee != null) {
                session.remove(employee);
                session.getTransaction().commit();
                logger.info("Employee deleted: {}", employeeId);
                return true;
            }
            session.getTransaction().commit();
            return false;
        } catch (Exception e) {
            logger.error("Error deleting employee: {}", employeeId, e);
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    @Override
    public boolean existsById(String employeeId) {
        try (Session session = HibernateUtil.getSession()) {
            Employee employee = session.get(Employee.class, employeeId);
            return employee != null;
        } catch (Exception e) {
            logger.error("Error checking if employee exists: {}", employeeId, e);
            throw new RuntimeException("Failed to check employee existence", e);
        }
    }

    @Override
    public long count() {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Employee", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting employees", e);
            throw new RuntimeException("Failed to count employees", e);
        }
    }

    @Override
    public long countByEmploymentStatus(EmploymentStatus status) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Employee WHERE employmentStatus = :status",
                    Long.class
            );
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting employees by status: {}", status, e);
            throw new RuntimeException("Failed to count employees by status", e);
        }
    }
}
