package com.yourname.myapp;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point for the EIMS Desktop Application.
 * This class is kept simple and delegates to EmployeeManagementApp.
 */
public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        EmployeeManagementApp app = new EmployeeManagementApp();
        app.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
