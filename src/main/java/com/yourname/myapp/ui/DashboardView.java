package com.yourname.myapp.ui;

import com.yourname.myapp.dto.DashboardStats;
import com.yourname.myapp.service.EmployeeService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dashboard view showing employee statistics and metrics.
 */
public class DashboardView {
    private static final Logger logger = LoggerFactory.getLogger(DashboardView.class);
    private final EmployeeService employeeService;
    private VBox rootPane;

    public DashboardView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        initializeUI();
    }

    /**
     * Initialize the dashboard UI
     */
    private void initializeUI() {
        rootPane = new VBox(20);
        rootPane.setPadding(new Insets(20));
        rootPane.setStyle("-fx-background-color: #f5f5f5;");
        rootPane.setPrefWidth(Double.MAX_VALUE);
        rootPane.setPrefHeight(Double.MAX_VALUE);

        // Title
        Label titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        rootPane.getChildren().add(titleLabel);

        // Statistics cards
        GridPane statsGrid = createStatisticsGrid();
        rootPane.getChildren().add(statsGrid);
    }

    /**
     * Create the statistics grid with employee metrics
     */
    private GridPane createStatisticsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10));
        grid.setPrefWidth(Double.MAX_VALUE);
        grid.setStyle("-fx-background-color: #f5f5f5;");

        try {
            DashboardStats stats = employeeService.getDashboardStats();

            // Total Employees Card
            VBox totalCard = createStatCard("Total Employees", String.valueOf(stats.getTotalEmployeeCount()), "#3498db");
            grid.add(totalCard, 0, 0);

            // Active Employees Card
            VBox activeCard = createStatCard("Active Employees", String.valueOf(stats.getActiveEmployeeCount()), "#2ecc71");
            grid.add(activeCard, 1, 0);

            // On Leave Card
            VBox onLeaveCard = createStatCard("On Leave", String.valueOf(stats.getOnLeaveCount()), "#f39c12");
            grid.add(onLeaveCard, 2, 0);

            // New Joiners Card
            VBox newJoinersCard = createStatCard("New Joiners (This Month)", String.valueOf(stats.getNewJoinersCount()), "#9b59b6");
            grid.add(newJoinersCard, 3, 0);

            logger.info("Dashboard statistics loaded successfully");
        } catch (Exception e) {
            logger.error("Error loading dashboard statistics", e);
            Label errorLabel = new Label("Error loading statistics: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            grid.add(errorLabel, 0, 0);
        }

        return grid;
    }

    /**
     * Create a statistics card with count and title
     */
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;");
        card.setPrefWidth(200);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 14));
        titleLabel.setStyle("-fx-text-fill: #333;");

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    /**
     * Get the root pane of this view
     */
    public VBox getRootPane() {
        return rootPane;
    }

    /**
     * Refresh the dashboard data
     */
    public void refresh() {
        // Remove the old grid (it's the second child, after the title)
        if (rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(1);
        }
        // Add a fresh grid with updated statistics
        GridPane statsGrid = createStatisticsGrid();
        rootPane.getChildren().add(statsGrid);
    }
}
