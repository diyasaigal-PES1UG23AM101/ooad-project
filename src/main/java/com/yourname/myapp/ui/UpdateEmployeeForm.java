package com.yourname.myapp.ui;

import com.yourname.myapp.dto.EmployeeRequest;
import com.yourname.myapp.entity.Employee;
import com.yourname.myapp.entity.EmploymentStatus;
import com.yourname.myapp.service.EmployeeService;
import com.yourname.myapp.ui.util.DialogUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Form for updating an existing employee.
 */
public class UpdateEmployeeForm {
    private static final Logger logger = LoggerFactory.getLogger(UpdateEmployeeForm.class);
    private final EmployeeService employeeService;
    private final Employee employee;
    private final Stage stage;
    private Runnable onSuccessCallback;

    public UpdateEmployeeForm(EmployeeService employeeService, Employee employee) {
        this.employeeService = employeeService;
        this.employee = employee;
        this.stage = new Stage();
        stage.setTitle("Update Employee");
        stage.setWidth(500);
        stage.setHeight(400);
        initializeUI();
    }

    /**
     * Initialize the form UI with existing employee data
     */
    private void initializeUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("Update Employee");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        GridPane.setColumnSpan(titleLabel, 2);
        gridPane.add(titleLabel, 0, 0);

        // Employee ID (read-only)
        Label idLabel = new Label("Employee ID:");
        idLabel.setFont(Font.font("Arial", 12));
        Label idValue = new Label(employee.getEmployeeId());
        idValue.setPrefWidth(300);
        idValue.setStyle("-fx-border-color: #ddd; -fx-padding: 5;");
        gridPane.add(idLabel, 0, 1);
        gridPane.add(idValue, 1, 1);

        // Employee Name
        Label nameLabel = new Label("Employee Name:");
        nameLabel.setFont(Font.font("Arial", 12));
        TextField nameField = new TextField();
        nameField.setText(employee.getEmployeeName());
        nameField.setPrefWidth(300);
        gridPane.add(nameLabel, 0, 2);
        gridPane.add(nameField, 1, 2);

        // Department
        Label deptLabel = new Label("Department:");
        deptLabel.setFont(Font.font("Arial", 12));
        ComboBox<String> departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll(
                "IT", "HR", "Finance", "Operations", "Sales", "Marketing", "Development"
        );
        departmentCombo.setValue(employee.getDepartment());
        departmentCombo.setPrefWidth(300);
        gridPane.add(deptLabel, 0, 3);
        gridPane.add(departmentCombo, 1, 3);

        // Job Role
        Label roleLabel = new Label("Job Role:");
        roleLabel.setFont(Font.font("Arial", 12));
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(
                "Manager", "Developer", "Analyst", "Consultant", "Coordinator", "Executive", "Other"
        );
        roleCombo.setValue(employee.getJobRole());
        roleCombo.setPrefWidth(300);
        gridPane.add(roleLabel, 0, 4);
        gridPane.add(roleCombo, 1, 4);

        // Employment Status
        Label statusLabel = new Label("Employment Status:");
        statusLabel.setFont(Font.font("Arial", 12));
        ComboBox<EmploymentStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(EmploymentStatus.values());
        statusCombo.setValue(employee.getEmploymentStatus());
        statusCombo.setPrefWidth(300);
        gridPane.add(statusLabel, 0, 5);
        gridPane.add(statusCombo, 1, 5);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button updateButton = new Button("Update");
        updateButton.setPrefWidth(100);
        updateButton.setStyle("-fx-font-size: 12;");
        updateButton.setOnAction(event -> {
            handleUpdate(nameField, departmentCombo, roleCombo, statusCombo);
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setStyle("-fx-font-size: 12;");
        cancelButton.setOnAction(event -> stage.close());

        buttonBox.getChildren().addAll(updateButton, cancelButton);
        GridPane.setColumnSpan(buttonBox, 2);
        gridPane.add(buttonBox, 0, 6);

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
    }

    /**
     * Handle update button action
     */
    private void handleUpdate(TextField nameField, ComboBox<String> departmentCombo,
                             ComboBox<String> roleCombo, ComboBox<EmploymentStatus> statusCombo) {
        try {
            // Validate inputs
            String name = nameField.getText().trim();
            String department = departmentCombo.getValue();
            String role = roleCombo.getValue();
            EmploymentStatus status = statusCombo.getValue();

            if (name.isEmpty()) {
                DialogUtil.showWarning("Validation Error", "Empty Field", "Please enter employee name");
                return;
            }
            if (department == null) {
                DialogUtil.showWarning("Validation Error", "Empty Field", "Please select department");
                return;
            }
            if (role == null) {
                DialogUtil.showWarning("Validation Error", "Empty Field", "Please select job role");
                return;
            }

            // Create update request
            EmployeeRequest request = new EmployeeRequest();
            request.setEmployeeName(name);
            request.setDepartment(department);
            request.setJobRole(role);
            request.setEmploymentStatus(status != null ? status : EmploymentStatus.ACTIVE);

            // Update employee
            employeeService.updateEmployee(employee.getEmployeeId(), request);
            DialogUtil.showInfo("Success", "Employee Updated", "Employee has been updated successfully");
            
            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }
            
            stage.close();
            logger.info("Employee updated successfully: {}", employee.getEmployeeId());
        } catch (Exception e) {
            logger.error("Error updating employee", e);
            DialogUtil.showError("Error", "Failed to update employee", e.getMessage());
        }
    }

    /**
     * Set callback for successful update
     */
    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    /**
     * Show the form
     */
    public void show() {
        stage.show();
    }
}
