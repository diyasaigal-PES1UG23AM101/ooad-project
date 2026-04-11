package com.yourname.myapp;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.service.EmployeeService;
import com.yourname.myapp.ui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for Employee Information Management System (EIMS).
 * Manages the primary stage and navigation between views.
 */
public class EmployeeManagementApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeManagementApp.class);
    
    private Stage primaryStage;
    private BorderPane mainLayout;
    private EmployeeService employeeService;
    private EmployeeListView employeeListView;
    private DashboardView dashboardView;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.employeeService = new EmployeeService();

            // Create main layout
            mainLayout = new BorderPane();

            // Create sidebar
            VBox sidebar = createSidebar();
            mainLayout.setLeft(sidebar);

            // Initialize views
            dashboardView = new DashboardView(employeeService);
            employeeListView = new EmployeeListView(employeeService);

            // Set initial view (Dashboard)
            mainLayout.setCenter(dashboardView.getRootPane());

            // Create and show scene
            Scene scene = new Scene(mainLayout, 1200, 700);
            primaryStage.setTitle("Employee Information Management System (EIMS)");
            primaryStage.setScene(scene);
            primaryStage.show();

            logger.info("EIMS Application started successfully");

            // Setup close event handler
            primaryStage.setOnCloseRequest(event -> {
                onApplicationClose();
            });

        } catch (Exception e) {
            logger.error("Failed to start application", e);
            e.printStackTrace();
        }
    }

    /**
     * Create the sidebar with navigation buttons
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(200);

        // Header
        Label headerLabel = new Label("EIMS");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        headerLabel.setStyle("-fx-text-fill: white;");

        Label versionLabel = new Label("v1.0");
        versionLabel.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 10;");

        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(headerLabel, versionLabel);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        headerBox.setStyle("-fx-border-color: #34495e; -fx-border-width: 0 0 2 0;");

        // Navigation buttons
        Button dashboardButton = new Button("Dashboard");
        dashboardButton.setPrefWidth(170);
        dashboardButton.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        dashboardButton.setOnAction(event -> switchToView(dashboardView.getRootPane(), dashboardView));

        Button employeeListButton = new Button("Employee List");
        employeeListButton.setPrefWidth(170);
        employeeListButton.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        employeeListButton.setOnAction(event -> switchToView(employeeListView.getRootPane(), employeeListView));

        Button addEmployeeButton = new Button("Add Employee");
        addEmployeeButton.setPrefWidth(170);
        addEmployeeButton.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #27ae60;");
        addEmployeeButton.setOnAction(event -> openAddEmployeeForm());

        // Refresh button
        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(170);
        refreshButton.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        refreshButton.setOnAction(event -> {
            employeeListView.refresh();
            dashboardView.refresh();
        });

        // Exit button
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(170);
        exitButton.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #e74c3c;");
        exitButton.setOnAction(event -> primaryStage.close());

        sidebar.getChildren().addAll(
                headerBox,
                dashboardButton,
                employeeListButton,
                addEmployeeButton,
                refreshButton,
                new Separator(javafx.geometry.Orientation.HORIZONTAL),
                exitButton
        );

        return sidebar;
    }

    /**
     * Switch to a different view
     */
    private void switchToView(VBox view, Object viewObject) {
        mainLayout.setCenter(view);
        
        // Refresh the view if applicable
        if (viewObject instanceof DashboardView) {
            ((DashboardView) viewObject).refresh();
        } else if (viewObject instanceof EmployeeListView) {
            ((EmployeeListView) viewObject).refresh();
        }
    }

    /**
     * Open the Add Employee form
     */
    private void openAddEmployeeForm() {
        try {
            AddEmployeeForm addForm = new AddEmployeeForm(employeeService);
            addForm.setOnSuccessCallback(() -> {
                employeeListView.refresh();
                dashboardView.refresh();
            });
            addForm.show();
        } catch (Exception e) {
            logger.error("Error opening add employee form", e);
        }
    }

    /**
     * Handle application close event
     */
    private void onApplicationClose() {
        try {
            // Close Hibernate session factory
            HibernateUtil.closeSessionFactory();
            logger.info("EIMS Application closed successfully");
        } catch (Exception e) {
            logger.error("Error closing application", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
