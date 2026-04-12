package com.yourname.myapp;

import com.yourname.myapp.config.HibernateUtil;
import com.yourname.myapp.service.EmployeeService;
import com.yourname.myapp.ui.*;

import com.yourname.myapp.recruitment.ui.CandidateListView;
import com.yourname.myapp.recruitment.ui.RecruitmentDashboardView;
import com.yourname.myapp.ui.attendance.AttendanceLeaveMainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application class for Employee Information Management System (EIMS) - Swing version.
 * Manages the primary frame and navigation between views.
 */
public class EmployeeManagementApp extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeManagementApp.class);
    
    private JPanel mainPanel;
    private JPanel contentPanel;
    private EmployeeService employeeService;
    private EmployeeListView employeeListView;
    private DashboardView dashboardView;

    // Recruitment module views
    private AttendanceLeaveMainView attendanceLeaveMainView;
    private CandidateListView candidateListView;
    private RecruitmentDashboardView recruitmentDashboardView;

    public EmployeeManagementApp() {
        try {
            this.employeeService = new EmployeeService();
            
            // Frame setup
            setTitle("Employee Information Management System (EIMS)");
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setSize(1200, 700);
            setLocationRelativeTo(null);
            
            // Create main layout
            mainPanel = new JPanel(new BorderLayout());
            
            // Create sidebar
            JPanel sidebar = createSidebar();
            mainPanel.add(sidebar, BorderLayout.WEST);
            
            // Create content panel
            contentPanel = new JPanel(new BorderLayout());
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
            setContentPane(mainPanel);
            

            // Initialize views
            dashboardView = new DashboardView(employeeService);
            employeeListView = new EmployeeListView(employeeService);
            candidateListView = new CandidateListView();
            recruitmentDashboardView = new RecruitmentDashboardView();
            attendanceLeaveMainView = new AttendanceLeaveMainView();

            // Set initial view (Dashboard)
            switchToView(dashboardView.getRootPane(), dashboardView);
            
            // Setup close event handler
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    onApplicationClose();
                }
            });
            
            logger.info("EIMS Application started successfully");
            
        } catch (Exception e) {
            logger.error("Failed to start application", e);
            e.printStackTrace();
        }
    }

    /**
     * Create the sidebar with navigation buttons
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Header
        JLabel headerLabel = new JLabel("EIMS");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(189, 195, 199));
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerBox = new JPanel();
        headerBox.setLayout(new BoxLayout(headerBox, BoxLayout.Y_AXIS));
        headerBox.setBackground(new Color(44, 62, 80));
        headerBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 73, 94)),
                BorderFactory.createEmptyBorder(0, 0, 20, 0)
        ));
        headerBox.setMaximumSize(new Dimension(180, 60));
        headerBox.add(headerLabel);
        headerBox.add(versionLabel);


        // Navigation buttons
        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboardButton.setMaximumSize(new Dimension(180, 40));
        dashboardButton.setFont(new Font("Arial", Font.PLAIN, 12));
        dashboardButton.setBackground(new Color(52, 73, 94));
        dashboardButton.setForeground(Color.WHITE);
        dashboardButton.setBorderPainted(false);
        dashboardButton.setFocusPainted(false);
        dashboardButton.setOpaque(true);
        dashboardButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dashboardButton.addActionListener(e -> switchToView(dashboardView.getRootPane(), dashboardView));

        JButton employeeListButton = new JButton("Employee List");
        employeeListButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        employeeListButton.setMaximumSize(new Dimension(180, 40));
        employeeListButton.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeListButton.setBackground(new Color(52, 73, 94));
        employeeListButton.setForeground(Color.WHITE);
        employeeListButton.setBorderPainted(false);
        employeeListButton.setFocusPainted(false);
        employeeListButton.setOpaque(true);
        employeeListButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeeListButton.addActionListener(e -> switchToView(employeeListView.getRootPane(), employeeListView));

        JButton recruitmentDashboardButton = new JButton("Recruitment Dashboard");
        recruitmentDashboardButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        recruitmentDashboardButton.setMaximumSize(new Dimension(180, 40));
        recruitmentDashboardButton.setFont(new Font("Arial", Font.PLAIN, 12));
        recruitmentDashboardButton.setBackground(new Color(52, 73, 94));
        recruitmentDashboardButton.setForeground(Color.WHITE);
        recruitmentDashboardButton.setBorderPainted(false);
        recruitmentDashboardButton.setFocusPainted(false);
        recruitmentDashboardButton.setOpaque(true);
        recruitmentDashboardButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        recruitmentDashboardButton.addActionListener(e -> switchToView(recruitmentDashboardView.getRootPane(), recruitmentDashboardView));

        JButton candidateListButton = new JButton("Candidate List");
        candidateListButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        candidateListButton.setMaximumSize(new Dimension(180, 40));
        candidateListButton.setFont(new Font("Arial", Font.PLAIN, 12));
        candidateListButton.setBackground(new Color(52, 73, 94));
        candidateListButton.setForeground(Color.WHITE);
        candidateListButton.setBorderPainted(false);
        candidateListButton.setFocusPainted(false);
        candidateListButton.setOpaque(true);
        candidateListButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        candidateListButton.addActionListener(e -> switchToView(candidateListView.getRootPane(), candidateListView));

        JButton attendanceButton = new JButton("Attendance & Leave");
        attendanceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        attendanceButton.setMaximumSize(new Dimension(180, 40));
        attendanceButton.setFont(new Font("Arial", Font.PLAIN, 12));
        attendanceButton.setBackground(new Color(52, 73, 94));
        attendanceButton.setForeground(Color.WHITE);
        attendanceButton.setBorderPainted(false);
        attendanceButton.setFocusPainted(false);
        attendanceButton.setOpaque(true);
        attendanceButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        attendanceButton.addActionListener(e -> switchToView(attendanceLeaveMainView, attendanceLeaveMainView));

        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addEmployeeButton.setMaximumSize(new Dimension(180, 40));
        addEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        addEmployeeButton.setBackground(new Color(39, 174, 96));
        addEmployeeButton.setForeground(Color.WHITE);
        addEmployeeButton.setBorderPainted(false);
        addEmployeeButton.setFocusPainted(false);
        addEmployeeButton.setOpaque(true);
        addEmployeeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addEmployeeButton.addActionListener(e -> openAddEmployeeForm());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshButton.setMaximumSize(new Dimension(180, 40));
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setBackground(new Color(52, 73, 94));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setOpaque(true);
        refreshButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            employeeListView.refresh();
            dashboardView.refresh();
            candidateListView.refresh();
            recruitmentDashboardView.refresh();
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(180, 40));
        exitButton.setFont(new Font("Arial", Font.PLAIN, 12));
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setOpaque(true);
        exitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> dispose());

        // Add components to sidebar
        sidebar.add(headerBox);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(employeeListButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(recruitmentDashboardButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(candidateListButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(attendanceButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(addEmployeeButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(refreshButton);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(new JSeparator());
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(exitButton);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    /**
     * Switch to a different view
     */
    private void switchToView(Container view, Object viewObject) {
        contentPanel.removeAll();
        contentPanel.add((Component) view, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        // Refresh the view if applicable
        if (viewObject instanceof DashboardView) {
            ((DashboardView) viewObject).refresh();
        } else if (viewObject instanceof EmployeeListView) {
            ((EmployeeListView) viewObject).refresh();
        } else if (viewObject instanceof RecruitmentDashboardView) {
            ((RecruitmentDashboardView) viewObject).refresh();
        } else if (viewObject instanceof CandidateListView) {
            ((CandidateListView) viewObject).refresh();
        }
    }

    /**
     * Open the Add Employee form
     */
    private void openAddEmployeeForm() {
        try {
            AddEmployeeForm addForm = new AddEmployeeForm(this, employeeService);
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
            System.exit(0);
        } catch (Exception e) {
            logger.error("Error closing application", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeManagementApp frame = new EmployeeManagementApp();
            frame.setVisible(true);
        });
    }
}
