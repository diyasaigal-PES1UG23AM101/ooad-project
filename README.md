# Employee Information Management System (EIMS)

A desktop application for managing employee information built with Java, JavaFX, and MySQL.

## Introduction

EIMS is a comprehensive desktop application designed to manage employee records efficiently. It provides a user-friendly interface for managing employees, including creating, updating, deleting, and viewing employee records with advanced filtering capabilities.

## Features

### Core Features
- **Dashboard View**: Display key statistics including total employees, active employees, on-leave employees, and new joiners this month
- **Employee List**: View all employees with the ability to filter by department and employment status
- **Search Functionality**: Search employees by name in real-time
- **Add Employee**: Create new employee records with auto-generated employee IDs
- **Update Employee**: Modify employee information
- **Delete Employee**: Remove employee records with confirmation

### Technical Features
- **Builder Pattern**: Used for constructing Employee objects
- **Repository Pattern**: Abstraction layer for data access
- **Service Layer**: Business logic separation with comprehensive error handling
- **Custom Exceptions**: Specific exception handling for employee-related errors
- **Auto-Generated IDs**: Employees get unique IDs in format EMP-XXXXXXXX
- **JPA/Hibernate ORM**: Object-relational mapping for database operations
- **Centralized Exception Handling**: User-friendly error dialogs throughout the application

## Technology Stack

- **Java 11+**: Programming language
- **JavaFX 21.0.2**: UI framework
- **Hibernate 6.2**: ORM framework
- **MySQL 8.0**: Database
- **Maven**: Build tool
- **Lombok**: Code generation library
- **SLF4J + Logback**: Logging framework

## Project Structure

```
ooad-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourname/myapp/
│   │   │       ├── App.java                 # Application entry point
│   │   │       ├── EmployeeManagementApp.java # Main JavaFX application
│   │   │       ├── builder/
│   │   │       │   └── EmployeeBuilder.java # Builder pattern implementation
│   │   │       ├── config/
│   │   │       │   └── HibernateUtil.java  # Hibernate session factory
│   │   │       ├── dto/
│   │   │       │   ├── EmployeeRequest.java # Request DTO
│   │   │       │   └── DashboardStats.java  # Dashboard statistics DTO
│   │   │       ├── entity/
│   │   │       │   ├── Employee.java        # Employee entity
│   │   │       │   └── EmploymentStatus.java # Enum for employment status
│   │   │       ├── exception/
│   │   │       │   ├── EmployeeNotFoundException.java
│   │   │       │   └── DuplicateEmployeeIdException.java
│   │   │       ├── repository/
│   │   │       │   ├── EmployeeRepository.java  # Repository interface
│   │   │       │   └── EmployeeRepositoryImpl.java # Repository implementation
│   │   │       ├── service/
│   │   │       │   └── EmployeeService.java # Business logic
│   │   │       └── ui/
│   │   │           ├── DashboardView.java       # Dashboard UI
│   │   │           ├── EmployeeListView.java    # Employee list UI
│   │   │           ├── AddEmployeeForm.java     # Add employee form
│   │   │           ├── UpdateEmployeeForm.java  # Update employee form
│   │   │           └── util/
│   │   │               └── DialogUtil.java      # Dialog utilities
│   │   └── resources/
│   │       └── logback.xml # Logging configuration
│   └── test/
│       └── java/...
└── pom.xml # Maven configuration
```

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- MySQL 8.0 or higher
- Git (optional, for cloning)

## Database Setup

### Step 1: Create Database

Open MySQL command line or MySQL Workbench and execute:

```sql
CREATE DATABASE eims_db;
USE eims_db;
```

### Step 2: Update Hibernate Configuration

Edit `src/main/java/com/yourname/myapp/config/HibernateUtil.java` to match your MySQL credentials:

```java
configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/eims_db");
configuration.setProperty("hibernate.connection.username", "your_mysql_username");
configuration.setProperty("hibernate.connection.password", "your_mysql_password");
```

Default values:
- **URL**: jdbc:mysql://localhost:3306/eims_db
- **Username**: root
- **Password**: (empty)

The tables will be created automatically on first run due to `hibernate.hbm2ddl.auto=update`.

## Building and Running

### Option 1: Using Maven

```bash
# Navigate to project directory
cd ooad-project

# Clean and build
mvn clean package

# Run the application
mvn clean compile javafx:run

# Or run the generated JAR
java -jar target/eims-desktop-1.0-SNAPSHOT.jar
```

### Option 2: IDE Execution

If using IntelliJ IDEA or Eclipse:
1. Right-click on `EmployeeManagementApp.java`
2. Select "Run 'EmployeeManagementApp.main()'"

## Usage Guide

### Dashboard View
- Displays four key metrics:
  - **Total Employees**: Total count of all employees in the system
  - **Active Employees**: Count of employees with ACTIVE status
  - **On Leave**: Count of employees on leave
  - **New Joiners**: Count of employees who joined in the current month

### Employee List View
- **Search**: Enter employee name to search in real-time
- **Filter by Department**: Select a specific department to filter
- **Filter by Status**: Filter employees by their employment status
- **Clear Filters**: Reset all filters to view all employees
- **Refresh**: Manually refresh the employee list
- **Edit**: Select an employee and click Edit to modify details
- **Delete**: Select an employee and click Delete to remove (requires confirmation)

### Add Employee
1. Click the "Add Employee" button in the sidebar
2. Fill in all required fields:
   - Employee Name
   - Department (dropdown selector)
   - Job Role (dropdown selector)
   - Employment Status (defaults to ACTIVE)
3. Click "Save" to create the employee
4. An auto-generated Employee ID in format EMP-XXXXXXXX will be assigned

### Update Employee
1. In Employee List, select an employee
2. Click "Edit" button
3. Modify the desired fields
4. Click "Update" to save changes

### Delete Employee
1. In Employee List, select an employee
2. Click "Delete" button
3. Confirm deletion in the dialog
4. Employee will be removed from the system

## Class Documentation

### Entity Classes

#### Employee
- Auto-generated ID (EMP-XXXXXXXX format)
- Fields: employeeId, employeeName, department, jobRole, employmentStatus, joiningDate
- JPA annotations for persistence
- Timestamps: createdAt, updatedAt

#### EmploymentStatus
- Enum with values: ACTIVE, INACTIVE, ON_LEAVE

### Service Layer

#### EmployeeService
Key methods:
- `getAllEmployees()`: Retrieve all employees
- `getAllEmployees(String dept, String status)`: Get employees with filters
- `getEmployeeById(String id)`: Fetch specific employee
- `createEmployee(EmployeeRequest dto)`: Create new employee using Builder pattern
- `updateEmployee(String id, EmployeeRequest dto)`: Update employee
- `deleteEmployee(String id)`: Delete employee
- `getDashboardStats()`: Get dashboard statistics
- `searchByName(String name)`: Search employees by name
- `getAllDepartments()`: Get list of all departments

### Repository Layer

#### EmployeeRepository & EmployeeRepositoryImpl
Custom methods:
- `save(Employee)`: Save/update employee
- `findById(String id)`: Find by ID
- `findAll()`: Get all employees
- `findByDepartment(String)`: Filter by department
- `findByEmploymentStatus(EmploymentStatus)`: Filter by status
- `findByDepartmentAndEmploymentStatus()`: Combined filter
- `findByEmployeeNameContains()`: Search by name
- `deleteById(String)`: Delete employee
- `existsById(String)`: Check existence
- `count()`: Total count
- `countByEmploymentStatus()`: Count by status

### Builder Pattern

#### EmployeeBuilder
Fluent API for creating Employee objects:
```java
Employee employee = new EmployeeBuilder()
    .withEmployeeName("John Doe")
    .withDepartment("IT")
    .withJobRole("Developer")
    .withEmploymentStatus(EmploymentStatus.ACTIVE)
    .build();
```

### Exception Handling

#### Custom Exceptions
- `EmployeeNotFoundException`: Thrown when employee is not found
- `DuplicateEmployeeIdException`: Thrown when duplicate ID detected

All exceptions are caught at the service layer and displayed as user-friendly dialogs in the UI.

## Error Handling

The application implements comprehensive error handling:
- **Validation Errors**: Validated at form level before submission
- **Business Logic Errors**: Caught in service layer with specific exception types
- **Database Errors**: Caught and logged with user-friendly messages
- **UI Dialogs**: All errors displayed as information/warning/error dialogs

## Logging

Logs are configured using Logback:
- **Console**: DEBUG messages displayed in console
- **File**: INFO level messages saved to `logs/eims.log`
- **Rolling**: Log files rotate daily or at 10MB

Access logs at: `logs/eims.log`

## Configuration

### Database Configuration
Located in: `src/main/java/com/yourname/myapp/config/HibernateUtil.java`

Key settings:
- Driver: com.mysql.cj.jdbc.Driver
- Dialect: MySQL8Dialect
- Auto DDL: Update (auto-creates/updates schema)
- Show SQL: true (for debugging)
- Batch Size: 10 (for performance)

### Logging Configuration
Located in: `src/main/resources/logback.xml`

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Check database credentials in HibernateUtil.java
- Verify database exists (eims_db)
- Check MySQL port (default: 3306)

### JavaFX Not Starting
- Ensure Java 11+ is installed
- Verify JavaFX libraries are in classpath (Maven handles this)
- Check for display server (on Linux, may need DISPLAY variable)

### Employee Not Found Error
- Verify employee ID exists
- Check if employee was deleted
- Reload the employee list

## Future Enhancements

- Export employee data to Excel/PDF
- Email notifications for employee updates
- Authentication and user roles
- Attendance tracking
- Salary management
- Performance reviews
- Advanced reporting and analytics
- Data backup and restore functionality

## License

This project is created for educational purposes.

## Support

For issues or questions, check the error logs in `logs/eims.log` for detailed error messages.

---

**Version**: 1.0  
**Last Updated**: April 2026