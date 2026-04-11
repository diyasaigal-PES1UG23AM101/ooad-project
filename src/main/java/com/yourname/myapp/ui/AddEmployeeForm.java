package com.yourname.myapp.ui;

import com.yourname.myapp.dto.EmployeeRequest;
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
 * Form for adding a new employee.
 */
public class AddEmployeeForm {
    private static final Logger logger = LoggerFactory.getLogger(AddEmployeeForm.class);
    private final EmployeeService employeeService;
    private final Stage stage;
    private Runnable onSuccessCallback;

    public AddEmployeeForm(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.stage = new Stage();
        stage.setTitle("Add New Employee");
        stage.setWidth(500);
        stage.setHeight(400);
        initializeUI();
    }

    /**
     * Initialize the form UI
     */
    private void initializeUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("Add New Employee");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        GridPane.setColumnSpan(titleLabel, 2);
        gridPane.add(titleLabel, 0, 0);

        // Employee Name
        Label nameLabel = new Label("Employee Name:");
        nameLabel.setFont(Font.font("Arial", 12));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter full name");
        nameField.setPrefWidth(300);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);

        // Department
        Label deptLabel = new Label("Department:");
        deptLabel.setFont(Font.font("Arial", 12));
        ComboBox<String> departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll(
                "IT", "HR", "Finance", "Operations", "Sales", "Marketing", "Development"
        );
        departmentCombo.setPrefWidth(300);
        departmentCombo.setPromptText("Select department");
        gridPane.add(deptLabel, 0, 2);
        gridPane.add(departmentCombo, 1, 2);

        // Job Role
        Label roleLabel = new Label("Job Role:");
        roleLabel.setFont(Font.font("Arial", 12));
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(
                "Manager", "Developer", "Analyst", "Consultant", "Coordinator", "Executive", "Other"
        );
        roleCombo.setPrefWidth(300);
        roleCombo.setPromptText("Select job role");
        gridPane.add(roleLabel, 0, 3);
        gridPane.add(roleCombo, 1, 3);

        // Employment Status
        Label statusLabel = new Label("Employment Status:");
        statusLabel.setFont(Font.font("Arial", 12));
        ComboBox<EmploymentStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(EmploymentStatus.values());
        statusCombo.setValue(EmploymentStatus.ACTIVE);
        statusCombo.setPrefWidth(300);
        gridPane.add(statusLabel, 0, 4);
        gridPane.add(statusCombo, 1, 4);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setStyle("-fx-font-size: 12;");
        saveButton.setOnAction(event -> {
            handleSave(nameField, departmentCombo, roleCombo, statusCombo);
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setStyle("-fx-font-size: 12;");
        cancelButton.setOnAction(event -> stage.close());

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        GridPane.setColumnSpan(buttonBox, 2);
        gridPane.add(buttonBox, 0, 5);

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
    }

    /**
     * Handle save button action
     */
    private void handleSave(TextField nameField, ComboBox<String> departmentCombo,
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

            // Create employee request
            EmployeeRequest request = new EmployeeRequest();
            request.setEmployeeName(name);
            request.setDepartment(department);
            request.setJobRole(role);
            request.setEmploymentStatus(status != null ? status : EmploymentStatus.ACTIVE);

            // Save employee
            employeeService.createEmployee(request);
            DialogUtil.showInfo("Success", "Employee Added", "Employee has been added successfully");
            
            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }
            
            stage.close();
            logger.info("Employee created successfully");
        } catch (Exception e) {
            logger.error("Error creating employee", e);
            DialogUtil.showError("Error", "Failed to add employee", e.getMessage());
        }
    }

    /**
     * Set callback for successful save
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
