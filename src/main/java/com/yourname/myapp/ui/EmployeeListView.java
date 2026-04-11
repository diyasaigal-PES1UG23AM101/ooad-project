package com.yourname.myapp.ui;

import com.yourname.myapp.entity.Employee;
import com.yourname.myapp.entity.EmploymentStatus;
import com.yourname.myapp.exception.EmployeeNotFoundException;
import com.yourname.myapp.service.EmployeeService;
import com.yourname.myapp.ui.util.DialogUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Employee List view with filtering capabilities.
 */
public class EmployeeListView {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeListView.class);
    private final EmployeeService employeeService;
    private VBox rootPane;
    private TableView<Employee> employeeTable;
    private ComboBox<String> departmentFilter;
    private ComboBox<EmploymentStatus> statusFilter;
    private TextField searchField;
    private Runnable onRefreshCallback;
    private Runnable onEditCallback;
    private Runnable onDeleteCallback;

    public EmployeeListView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        initializeUI();
    }

    /**
     * Initialize the employee list UI
     */
    private void initializeUI() {
        rootPane = new VBox(10);
        rootPane.setPadding(new Insets(20));
        rootPane.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("Employee List");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        rootPane.getChildren().add(titleLabel);

        // Filter bar
        HBox filterBar = createFilterBar();
        rootPane.getChildren().add(filterBar);

        // Employee table
        employeeTable = createEmployeeTable();
        ScrollPane scrollPane = new ScrollPane(employeeTable);
        scrollPane.setFitToWidth(true);
        rootPane.getChildren().add(scrollPane);

        // Button bar
        HBox buttonBar = createButtonBar();
        rootPane.getChildren().add(buttonBar);

        // Load initial data
        loadEmployees();
    }

    /**
     * Create the filter bar with department and status filters
     */
    private HBox createFilterBar() {
        HBox filterBar = new HBox(15);
        filterBar.setPadding(new Insets(10));
        filterBar.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");

        // Search field
        Label searchLabel = new Label("Search:");
        searchField = new TextField();
        searchField.setPromptText("Enter employee name...");
        searchField.setPrefWidth(200);
        searchField.setOnKeyReleased(event -> {
            if (searchField.getText().isEmpty()) {
                loadEmployees();
            } else {
                searchEmployees(searchField.getText());
            }
        });

        // Department filter
        Label deptLabel = new Label("Department:");
        departmentFilter = new ComboBox<>();
        departmentFilter.setPrefWidth(150);
        departmentFilter.setPromptText("All Departments");
        departmentFilter.getItems().add("All Departments");
        departmentFilter.getItems().addAll(employeeService.getAllDepartments());
        departmentFilter.setValue("All Departments");
        departmentFilter.setOnAction(event -> applyFilters());

        // Status filter
        Label statusLabel = new Label("Status:");
        statusFilter = new ComboBox<>();
        statusFilter.setPrefWidth(150);
        statusFilter.setPromptText("All Status");
        statusFilter.getItems().add(null);
        statusFilter.getItems().addAll(EmploymentStatus.values());
        statusFilter.setOnAction(event -> applyFilters());

        // Refresh button
        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(100);
        refreshButton.setOnAction(event -> loadEmployees());

        // Clear filters button
        Button clearButton = new Button("Clear Filters");
        clearButton.setPrefWidth(100);
        clearButton.setOnAction(event -> clearFilters());

        filterBar.getChildren().addAll(
                searchLabel, searchField,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                deptLabel, departmentFilter,
                statusLabel, statusFilter,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                refreshButton, clearButton
        );

        return filterBar;
    }

    /**
     * Create the employee table with columns
     */
    private TableView<Employee> createEmployeeTable() {
        TableView<Employee> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Employee, String> idColumn = new TableColumn<>("Employee ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmployeeId()));

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmployeeName()));

        TableColumn<Employee, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartment()));

        TableColumn<Employee, String> jobRoleColumn = new TableColumn<>("Job Role");
        jobRoleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJobRole()));

        TableColumn<Employee, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getEmploymentStatus().toString()
        ));

        table.getColumns().addAll(idColumn, nameColumn, departmentColumn, jobRoleColumn, statusColumn);
        table.setPrefHeight(400);

        return table;
    }

    /**
     * Create the action button bar
     */
    private HBox createButtonBar() {
        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        buttonBar.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");

        Button editButton = new Button("Edit");
        editButton.setPrefWidth(100);
        editButton.setOnAction(event -> {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (onEditCallback != null) {
                    onEditCallback.run();
                }
            } else {
                DialogUtil.showWarning("Selection Required", "", "Please select an employee to edit");
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(100);
        deleteButton.setStyle("-fx-text-fill: white; -fx-background-color: #e74c3c;");
        deleteButton.setOnAction(event -> {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (DialogUtil.showConfirmation("Confirm Delete", "Delete Employee",
                        "Are you sure you want to delete " + selected.getEmployeeName() + "?")) {
                    deleteEmployee(selected.getEmployeeId());
                }
            } else {
                DialogUtil.showWarning("Selection Required", "", "Please select an employee to delete");
            }
        });

        buttonBar.getChildren().addAll(editButton, deleteButton);
        return buttonBar;
    }

    /**
     * Load all employees into the table
     */
    public void loadEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            ObservableList<Employee> observableList = FXCollections.observableArrayList(employees);
            employeeTable.setItems(observableList);
            logger.info("Loaded {} employees", employees.size());
        } catch (Exception e) {
            logger.error("Error loading employees", e);
            DialogUtil.showError("Load Error", "Failed to load employees", e.getMessage());
        }
    }

    /**
     * Apply department and status filters
     */
    private void applyFilters() {
        try {
            String department = departmentFilter.getValue();
            EmploymentStatus status = statusFilter.getValue();

            String deptFilter = (department != null && !department.equals("All Departments")) ? department : null;
            String statusFilter = (status != null) ? status.toString() : null;

            List<Employee> employees = employeeService.getAllEmployees(deptFilter, statusFilter);
            ObservableList<Employee> observableList = FXCollections.observableArrayList(employees);
            employeeTable.setItems(observableList);
            logger.info("Applied filters - Department: {}, Status: {}", deptFilter, statusFilter);
        } catch (Exception e) {
            logger.error("Error applying filters", e);
            DialogUtil.showError("Filter Error", "Failed to apply filters", e.getMessage());
        }
    }

    /**
     * Search for employees by name
     */
    private void searchEmployees(String searchTerm) {
        try {
            List<Employee> employees = employeeService.searchByName(searchTerm);
            ObservableList<Employee> observableList = FXCollections.observableArrayList(employees);
            employeeTable.setItems(observableList);
            logger.info("Search results: {} employees found", employees.size());
        } catch (Exception e) {
            logger.error("Error searching employees", e);
            DialogUtil.showError("Search Error", "Failed to search employees", e.getMessage());
        }
    }

    /**
     * Clear all filters and reload
     */
    private void clearFilters() {
        searchField.clear();
        departmentFilter.setValue("All Departments");
        statusFilter.setValue(null);
        loadEmployees();
    }

    /**
     * Delete an employee
     */
    private void deleteEmployee(String employeeId) {
        try {
            employeeService.deleteEmployee(employeeId);
            DialogUtil.showInfo("Success", "Employee Deleted", "Employee has been deleted successfully");
            loadEmployees();
            if (onDeleteCallback != null) {
                onDeleteCallback.run();
            }
        } catch (EmployeeNotFoundException e) {
            DialogUtil.showError("Not Found", "Error", e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting employee", e);
            DialogUtil.showError("Delete Error", "Failed to delete employee", e.getMessage());
        }
    }

    /**
     * Get selected employee
     */
    public Employee getSelectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Set callback for edit action
     */
    public void setOnEditCallback(Runnable callback) {
        this.onEditCallback = callback;
    }

    /**
     * Set callback for delete action
     */
    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    /**
     * Get the root pane of this view
     */
    public VBox getRootPane() {
        return rootPane;
    }

    /**
     * Refresh the employee list
     */
    public void refresh() {
        loadEmployees();
    }
}
