# EIMS Desktop Application - Quick Setup Guide

## 1. Database Setup (One-time)

### Create MySQL Database
```sql
mysql -u root
CREATE DATABASE eims_db;
EXIT;
```

### Configure Database Connection
Edit: `src/main/java/com/yourname/myapp/config/HibernateUtil.java`

Update these lines if needed:
```java
configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/eims_db");
configuration.setProperty("hibernate.connection.username", "root");
configuration.setProperty("hibernate.connection.password", "");  // Your password
```

## 2. Build the Application

From project root:
```bash
cd ooad-project
mvn clean package
```

## 3. Run the Application

### Option A: Using Maven
```bash
mvn clean compile javafx:run
```

### Option B: Using JAR
```bash
java -jar target/eims-desktop-1.0-SNAPSHOT.jar
```

### Option C: IDE
- Right-click `EmployeeManagementApp.java` → Run

## 4. First Use

1. Dashboard will load with 0 employees on first run
2. Click "Add Employee" to create first employee
3. Fill form: Name, Department, Job Role, Status
4. Auto-generated Employee ID will be assigned (EMP-XXXXXXXX)
5. Navigate between Dashboard and Employee List using sidebar

## Common Issues

| Issue | Solution |
|-------|----------|
| MySQL connection refused | Check MySQL is running, verify credentials in HibernateUtil |
| Database not found | Execute `CREATE DATABASE eims_db;` in MySQL |
| JavaFX not found | Ensure Java 11+, Maven will download JFX automatically |
| Class not found | Run `mvn clean install` to rebuild |
| Port already in use | Change MySQL port or close conflicting app |

## Key Locations

- **Main App**: `src/main/java/com/yourname/myapp/EmployeeManagementApp.java`
- **Database Config**: `src/main/java/com/yourname/myapp/config/HibernateUtil.java`
- **Logs**: `logs/eims.log` (created after first run)
- **Service Logic**: `src/main/java/com/yourname/myapp/service/EmployeeService.java`

## Architecture Overview

```
JavaFX UI Layer (Views & Forms)
          ↓
    EmployeeService (Business Logic)
          ↓
    EmployeeRepository (Data Access)
          ↓
    Hibernate ORM
          ↓
    MySQL Database
```

## File Summary

| Component | File | Purpose |
|-----------|------|---------|
| Entity | Employee.java | Database table structure |
| Service | EmployeeService.java | Business logic |
| Repository | EmployeeRepositoryImpl.java | Database queries |
| UI - Dashboard | DashboardView.java | Statistics display |
| UI - List | EmployeeListView.java | Employee table with filters |
| UI - Add | AddEmployeeForm.java | Create new employee |
| UI - Edit | UpdateEmployeeForm.java | Modify employee |
| Config | HibernateUtil.java | Database connection |
| Main | EmployeeManagementApp.java | Application window |

---

**For detailed documentation see: README.md**
