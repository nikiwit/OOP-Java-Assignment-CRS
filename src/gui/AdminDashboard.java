package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

import services.AuthenticationService;
import services.UserService;
import models.User;
import models.Admin;
import models.Instructor;
import services.ReportGenerator;
import models.AcademicReport;
public class AdminDashboard extends JFrame {

    // Basic UI colors and size
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 700;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);

    private JLabel welcomeLabel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private String userId;

    private JTable userTable;
    private DefaultTableModel userTableModel;

    // Constructor
    public AdminDashboard(String adminName, String userId) {
        this.userId = userId;

        initializeFrame();
        initializeComponents(adminName);

        setVisible(true);
    }

    // ------------------------------------------------------------
    // For User Management Table:
    // Color renderer for "Status" column
    // ACTIVE  = Light Green
    // INACTIVE = Light Red
// ------------------------------------------------------------
    private class StatusColorRenderer extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            // Center align text
            label.setHorizontalAlignment(SwingConstants.CENTER);

            // Convert value to string safely (NO '?')
            String status = "";
            if (value != null) {
                status = value.toString().trim();
            }

            // Apply colors only when row is not selected
            if (!isSelected) {

                if (status.equalsIgnoreCase("Active")) {
                    label.setBackground(new Color(198, 239, 206));  // Light green
                    label.setForeground(Color.BLACK);

                } else if (status.equalsIgnoreCase("Inactive")) {
                    label.setBackground(new Color(255, 199, 206));  // Light red
                    label.setForeground(Color.BLACK);

                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                }
            }

            label.setOpaque(true);
            return label;
        }
    }

    // Basic window setup
    private void initializeFrame() {
        setTitle("CRS - Admin Dashboard");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    // Layout (header, sidebar, main content)
    private void initializeComponents(String adminName) {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel(adminName);
        add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BACKGROUND_COLOR);

        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createUserManagementPanel(), "UserManagement");
        mainContentPanel.add(new LoginHistoryPanel(), "LoginHistory");
        mainContentPanel.add(createEligibilityCheckPanel(), "EligibilityCheck");
        mainContentPanel.add(createReportsPanel(), "Reports");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    // Header (Admin name + title + logout)
    private JPanel createHeaderPanel(String adminName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, 80));

        // Left — Admin Name
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(PRIMARY_COLOR);

        // ** Move admin name slightly right **
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        welcomeLabel = new JLabel(adminName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel);
        panel.add(leftPanel, BorderLayout.WEST);

        // Center — Title
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_COLOR);

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        centerPanel.add(titleLabel);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Right — Logout button
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(PRIMARY_COLOR);

        // ** Move logout button slightly left **
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE); // Red logout button
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        // Force UI rendering for cross-platform compatibility (Mac/Windows)
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(true);
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout();
            }
        });

        rightPanel.add(logoutButton);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    // Sidebar navigation (Dashboard, User Management, etc.)
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(220, FRAME_HEIGHT));

        // ---- Dashboard ----
        panel.add(Box.createVerticalStrut(15));
        panel.add(createSidebarButton("Dashboard", "Dashboard"));

        // ---- User Management ----
        panel.add(Box.createVerticalStrut(15));
        panel.add(createSidebarButton("User Management", "UserManagement"));

        // ---- Login History (NEW BUTTON HERE) ----
        panel.add(Box.createVerticalStrut(15));
        JButton loginHistoryBtn = createSidebarButton("Login History", "LoginHistory");
        panel.add(loginHistoryBtn);

        // ---- Eligibility Check ----
        panel.add(Box.createVerticalStrut(15));
        panel.add(createSidebarButton("Eligibility Check", "EligibilityCheck"));

        // ---- Reports ----
        panel.add(Box.createVerticalStrut(15));
        panel.add(createSidebarButton("Reports", "Reports"));

        return panel;
    }

    private JButton createSidebarButton(String text, final String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 60));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        // Force UI rendering for cross-platform compatibility (Mac/Windows)
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 2));

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardLayout.show(mainContentPanel, cardName);
            }
        });

        return button;
    }

    // Dashboard Overview Panel
    private JPanel createDashboardPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // ----- Title -----
        JLabel title = new JLabel("System Overview");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        dao.InstructorDAO instructorDAO = new dao.InstructorDAO();
        dao.CourseDAO courseDAO = new dao.CourseDAO();
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        int studentCount = studentDAO.loadAllStudents().size();
        int instructorCount = instructorDAO.loadAllInstructors().size();
        int courseCount = courseDAO.loadAllCourses().size();

        java.util.Set<String> uniqueRecoveryPlans = new java.util.HashSet<>();
        for (models.RecoveryCourseEnrollment enrollment : enrollmentDAO.loadAllEnrollments()) {
            String key = enrollment.getStudentId() + "-" + enrollment.getCourseId();
            uniqueRecoveryPlans.add(key);
        }
        int recoveryPlanCount = uniqueRecoveryPlans.size();

        JPanel totalStudents = createStatBox("Total Students", String.valueOf(studentCount), new Color(46, 204, 113));
        JPanel totalInstructors = createStatBox("Total Instructors", String.valueOf(instructorCount), new Color(52, 152, 219));
        JPanel activeCourses = createStatBox("Active Courses", String.valueOf(courseCount), new Color(155, 89, 182));
        JPanel recoveryPlans = createStatBox("Recovery Plans", String.valueOf(recoveryPlanCount), new Color(230, 126, 34));

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(totalStudents, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(totalInstructors, gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(activeCourses, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(recoveryPlans, gbc);

        return panel;
    }

    // Creating each dashboard statistics box
    private JPanel createStatBox(String title, String value, Color borderColor) {

        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(300, 180));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(borderColor, 3));
        box.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(borderColor);

        box.add(titleLabel, BorderLayout.NORTH);
        box.add(valueLabel, BorderLayout.CENTER);

        return box;
    }

    //  User Management Panel
    private JPanel createUserManagementPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // -------------------- TITLE ROW + FILTER --------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);

        topPanel.add(titleLabel, BorderLayout.WEST);

        // ---------------- STATUS FILTER ONLY ----------------
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(BACKGROUND_COLOR);

        JLabel statusFilterLabel = new JLabel("Status:");
        JComboBox<String> statusFilterBox = new JComboBox<String>();
        statusFilterBox.addItem("ALL");
        statusFilterBox.addItem("Active");
        statusFilterBox.addItem("Inactive");

        filterPanel.add(statusFilterLabel);
        filterPanel.add(statusFilterBox);

        topPanel.add(filterPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        // ---------------- TABLE SETUP ------------------
        String[] columnNames = {"User ID", "Name", "Email", "Role", "Status"};
        userTableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(userTableModel);

        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        userTable.setRowHeight(35);

        // Set color renderer for Status column
        userTable.getColumnModel().getColumn(4).setCellRenderer(new StatusColorRenderer());

        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load user data initially
        loadUserData();
        utils.TableUtils.centerAlignTable(userTable);

        // ---------------- FILTER LOGIC -----------------
        statusFilterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                // First load all users
                loadUserData();

                String selectedStatus = (String) statusFilterBox.getSelectedItem();

                // Loop backwards while removing rows
                for (int i = userTableModel.getRowCount() - 1; i >= 0; i--) {

                    String rowStatus = (String) userTableModel.getValueAt(i, 4);

                    boolean remove = false;

                    if (!selectedStatus.equals("ALL") && !rowStatus.equals(selectedStatus)) {
                        remove = true;
                    }

                    if (remove) {
                        userTableModel.removeRow(i);
                    }
                }
            }
        });

        // ---------------- BOTTOM BUTTONS -----------------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Add User Button
        JButton addButton = new JButton("Add User");
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(Color.BLACK);
        addButton.setOpaque(true);
        addButton.setContentAreaFilled(true);
        addButton.setBorderPainted(true);
        addButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new AddUserFrame(AdminDashboard.this);
            }
        });
        buttonPanel.add(addButton);

        // Update User Button
        JButton updateButton = new JButton("Update User");
        updateButton.setBackground(Color.WHITE);
        updateButton.setForeground(Color.BLACK);
        updateButton.setOpaque(true);
        updateButton.setContentAreaFilled(true);
        updateButton.setBorderPainted(true);
        updateButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserAction();
            }
        });
        buttonPanel.add(updateButton);

        // Deactivate User Button
        JButton deactivateButton = new JButton("Deactivate User");
        deactivateButton.setBackground(Color.WHITE);
        deactivateButton.setForeground(Color.BLACK);
        deactivateButton.setOpaque(true);
        deactivateButton.setContentAreaFilled(true);
        deactivateButton.setBorderPainted(true);
        deactivateButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        deactivateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deactivateUserAction();
            }
        });
        buttonPanel.add(deactivateButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Loads all users into table
    private void loadUserData() {

        try {
            // ---------------- CLEAR TABLE BEFORE RELOADING ----------------
            userTableModel.setRowCount(0);

            // ---------------- LOAD USERS FROM SERVICE ----------------
            UserService userService = new UserService();
            java.util.List<User> users = userService.getAllUsers();

            if (users == null) {
                JOptionPane.showMessageDialog(this, "Failed to load users.");
                return;
            }

            // ================================================================
            //     SORTING LOGIC
            //     1. Active users appear FIRST
            //     2. Inside each Active/Inactive group >> sort by UserID
            // ================================================================
            java.util.Collections.sort(users, new java.util.Comparator<User>() {
                public int compare(User u1, User u2) {

                    // ---- RULE 1: Active FIRST ----
                    if (u1.isActive() && !u2.isActive()) {
                        return -1;  // u1 comes first
                    }
                    if (!u1.isActive() && u2.isActive()) {
                        return 1;   // u2 comes first
                    }

                    // ---- RULE 2: Same status >> sort by User ID ----
                    return u1.getUserId().compareTo(u2.getUserId());
                }
            });

            // ================================================================
            //     LOAD SORTED USERS INTO TABLE
            // ================================================================
            for (int i = 0; i < users.size(); i++) {

                User user = users.get(i);
                String name = "";

                // Get name based on role
                if (user instanceof Admin) {
                    name = ((Admin) user).getAdminName();
                } else if (user instanceof Instructor) {
                    name = ((Instructor) user).getInstructorName();
                }

                String status;
                if (user.isActive()) {
                    status = "Active";
                } else {
                    status = "Inactive";
                }

                userTableModel.addRow(new Object[]{
                    user.getUserId(),
                    name,
                    user.getEmail(),
                    user.getRole().toString(),
                    status
                });
            }

        } catch (Exception ex) {

            // ---------------- ERROR HANDLING ----------------
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while loading user data:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // Called after Add/Update/Deactivate
    public void refreshUserTable() {
        loadUserData();
    }

    // ------ Update Button Action ------
    private void updateUserAction() {
        int row = userTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.");
            return;
        }

        String selectedUserId = (String) userTableModel.getValueAt(row, 0);
        new UpdateUserFrame(this, selectedUserId);
    }

    // ------ Deactivate Button Action -------
    private void deactivateUserAction() {
        // Check if a row is selected
        int row = userTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.");
            return;
        }

        // Get the selected User ID
        String id = (String) userTableModel.getValueAt(row, 0);

        UserService userService = new UserService();
        java.util.List<User> users = userService.getAllUsers();

        User selectedUser = null;

        // Find the user by ID
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getUserId().equals(id)) {
                selectedUser = u;
            }
        }

        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "User not found.");
            return;
        }

        // Deactivate the user (set active = false)
        boolean success = userService.updateUser(id, selectedUser.getName(), false);

        if (success) {
            JOptionPane.showMessageDialog(this, "User deactivated!");
            refreshUserTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to deactivate user.");
        }
    }

    // Eligibility Check Panel
    private JTable eligibilityTable;
    private DefaultTableModel eligibilityTableModel;
    private JComboBox<String> eligibilityFilterBox;
    private JComboBox<String> majorFilterBox;
    private JComboBox<String> yearFilterBox;
    private JCheckBox showRecoveryEligibleBox;

    private JPanel createEligibilityCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Eligibility Check & Enrollment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(BACKGROUND_COLOR);

        JLabel eligibilityLabel = new JLabel("Eligibility:");
        eligibilityFilterBox = new JComboBox<>();
        eligibilityFilterBox.addItem("All Students");
        eligibilityFilterBox.addItem("Eligible");
        eligibilityFilterBox.addItem("Ineligible");
        eligibilityFilterBox.addActionListener(e -> filterEligibilityData());

        JLabel majorLabel = new JLabel("Major:");
        majorFilterBox = new JComboBox<>();
        majorFilterBox.addItem("All Majors");
        majorFilterBox.addActionListener(e -> filterEligibilityData());

        JLabel yearLabel = new JLabel("Year:");
        yearFilterBox = new JComboBox<>();
        yearFilterBox.addItem("All Years");
        yearFilterBox.addItem("1");
        yearFilterBox.addItem("2");
        yearFilterBox.addItem("3");
        yearFilterBox.addItem("4");
        yearFilterBox.addActionListener(e -> filterEligibilityData());

        showRecoveryEligibleBox = new JCheckBox("Recovery Eligible Only");
        showRecoveryEligibleBox.setBackground(BACKGROUND_COLOR);
        showRecoveryEligibleBox.setToolTipText("Show only students eligible for recovery (CGPA >= 2.0 and 1-3 failed courses)");
        showRecoveryEligibleBox.addActionListener(e -> filterEligibilityData());

        filterPanel.add(eligibilityLabel);
        filterPanel.add(eligibilityFilterBox);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(majorLabel);
        filterPanel.add(majorFilterBox);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(yearLabel);
        filterPanel.add(yearFilterBox);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(showRecoveryEligibleBox);

        topPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Student ID", "Name", "Major", "Year", "Semester",
            "CGPA", "Failed Courses", "Failed Course Names", "Eligibility"};
        eligibilityTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eligibilityTable = new JTable(eligibilityTableModel);

        eligibilityTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        eligibilityTable.setRowHeight(35);
        eligibilityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        eligibilityTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        eligibilityTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        eligibilityTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        eligibilityTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        eligibilityTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        eligibilityTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        eligibilityTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        eligibilityTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        eligibilityTable.getColumnModel().getColumn(8).setPreferredWidth(80);

        eligibilityTable.getColumnModel().getColumn(8).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    String status = value != null ? value.toString() : "";
                    if (status.contains("✓")) {
                        label.setBackground(new Color(198, 239, 206));
                        label.setForeground(new Color(0, 128, 0));
                    } else {
                        label.setBackground(new Color(255, 199, 206));
                        label.setForeground(new Color(192, 0, 0));
                    }
                }
                label.setOpaque(true);
                return label;
            }
        }
        );

        javax.swing.table.DefaultTableCellRenderer centerHeaderRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.BOLD, 12));
                return c;
            }
        };
        centerHeaderRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        eligibilityTable.getTableHeader().setDefaultRenderer(centerHeaderRenderer);

        JScrollPane scrollPane = new JScrollPane(eligibilityTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ---------------- BUTTON PANEL ------------------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(SECONDARY_COLOR);
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> {
            resetEligibilityFilters();
            loadEligibilityData();
        });

        JButton enrollButton = new JButton("Enroll for Recovery");
        enrollButton.setBackground(new Color(46, 204, 113));
        enrollButton.setForeground(Color.BLACK);
        enrollButton.setFont(new Font("Arial", Font.BOLD, 14));
        enrollButton.setFocusPainted(false);
        enrollButton.addActionListener(e -> enrollSelectedStudent());

        buttonPanel.add(refreshButton);
        buttonPanel.add(enrollButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadEligibilityData();
        utils.TableUtils.centerAlignTable(eligibilityTable);

        return panel;
    }

    private void loadEligibilityData() {
        eligibilityTableModel.setRowCount(0);
        majorFilterBox.removeAllItems();
        majorFilterBox.addItem("All Majors");

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        services.EligibilityChecker eligibilityChecker = new services.EligibilityChecker();

        java.util.List<models.Student> students = studentDAO.loadAllStudents();
        java.util.Set<String> majors = new java.util.HashSet<>();

        for (models.Student student : students) {
            double cgpa = eligibilityChecker.calculateCGPA(student);
            int failedCount = eligibilityChecker.getFailedCoursesCount(student);
            java.util.List<models.Course> failedCourses = eligibilityChecker.getFailedCourses(student);
            boolean eligible = eligibilityChecker.checkEligibility(student);

            if (hasActiveRecoveryEnrollmentForAllCourses(student.getStudentId(), failedCourses)) {
                majors.add(student.getMajor());
                continue;
            }

            StringBuilder failedNames = new StringBuilder();
            for (int i = 0; i < failedCourses.size(); i++) {
                if (i > 0) {
                    failedNames.append(", ");
                }
                failedNames.append(failedCourses.get(i).getCourseId());
            }

            String eligibilityStatus = eligible ? "✓ Eligible" : "✗ Ineligible";

            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getMajor(),
                student.getYear(),
                student.getSemester(),
                String.format("%.2f", cgpa),
                failedCount,
                failedNames.toString(),
                eligibilityStatus
            };

            eligibilityTableModel.addRow(row);
            majors.add(student.getMajor());
        }

        for (String major : majors) {
            majorFilterBox.addItem(major);
        }
    }

    private void filterEligibilityData() {
        dao.StudentDAO studentDAO = new dao.StudentDAO();
        services.EligibilityChecker eligibilityChecker = new services.EligibilityChecker();

        java.util.List<models.Student> students = studentDAO.loadAllStudents();
        eligibilityTableModel.setRowCount(0);

        String selectedEligibility = (String) eligibilityFilterBox.getSelectedItem();
        String selectedMajor = (String) majorFilterBox.getSelectedItem();
        String selectedYear = (String) yearFilterBox.getSelectedItem();

        if (selectedEligibility == null || selectedMajor == null || selectedYear == null) {
            return;
        }

        for (models.Student student : students) {
            if (!selectedMajor.equals("All Majors") && !student.getMajor().equals(selectedMajor)) {
                continue;
            }

            if (!selectedYear.equals("All Years") && student.getYear() != Integer.parseInt(selectedYear)) {
                continue;
            }

            boolean eligible = eligibilityChecker.checkEligibility(student);
            int failedCount = eligibilityChecker.getFailedCoursesCount(student);

            if (selectedEligibility.equals("Eligible") && !eligible) {
                continue;
            }
            if (selectedEligibility.equals("Ineligible") && eligible) {
                continue;
            }

            boolean recoveryEligible = eligible && failedCount >= 1 && failedCount <= 3;
            if (showRecoveryEligibleBox.isSelected() && !recoveryEligible) {
                continue;
            }

            double cgpa = eligibilityChecker.calculateCGPA(student);
            java.util.List<models.Course> failedCourses = eligibilityChecker.getFailedCourses(student);

            if (hasActiveRecoveryEnrollmentForAllCourses(student.getStudentId(), failedCourses)) {
                continue;
            }

            StringBuilder failedNames = new StringBuilder();
            for (int i = 0; i < failedCourses.size(); i++) {
                if (i > 0) {
                    failedNames.append(", ");
                }
                failedNames.append(failedCourses.get(i).getCourseId());
            }

            String eligibilityStatus = eligible ? "✓ Eligible" : "✗ Ineligible";

            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getMajor(),
                student.getYear(),
                student.getSemester(),
                String.format("%.2f", cgpa),
                failedCount,
                failedNames.toString(),
                eligibilityStatus
            };

            eligibilityTableModel.addRow(row);
        }
    }

    /**
     * Resets all eligibility filters to their default values
     */
    private void resetEligibilityFilters() {
        eligibilityFilterBox.setSelectedItem("All Students");
        majorFilterBox.setSelectedItem("All Majors");
        yearFilterBox.setSelectedItem("All Years");
        showRecoveryEligibleBox.setSelected(false);
    }

    /**
     * Checks if student has active recovery enrollments for ALL their failed
     * courses.
     *
     * @param studentId the student ID
     * @param failedCourses list of failed courses needing recovery
     * @return true if all failed courses have active enrollments
     */
    private boolean hasActiveRecoveryEnrollmentForAllCourses(
            String studentId, java.util.List<models.Course> failedCourses) {

        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        java.util.List<models.RecoveryCourseEnrollment> studentEnrollments
                = enrollmentDAO.loadEnrollmentsByStudent(studentId);

        java.util.Set<String> enrolledCourseIds = new java.util.HashSet<>();

        for (models.RecoveryCourseEnrollment enrollment : studentEnrollments) {
            if (enrollment.getStatus() == models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS
                    || enrollment.getStatus() == models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED) {
                enrolledCourseIds.add(enrollment.getCourseId());
            }
        }

        if (!enrolledCourseIds.isEmpty() && (failedCourses == null || failedCourses.isEmpty())) {
            return true;
        }

        if (failedCourses == null || failedCourses.isEmpty()) {
            return false;
        }

        for (models.Course course : failedCourses) {
            if (!enrolledCourseIds.contains(course.getCourseId())) {
                return false;
            }
        }

        return true;
    }

    private void enrollSelectedStudent() {
        int selectedRow = eligibilityTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to enroll for recovery.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) eligibilityTable.getValueAt(selectedRow, 0);
        String studentName = (String) eligibilityTable.getValueAt(selectedRow, 1);
        String eligibilityStatus = (String) eligibilityTable.getValueAt(selectedRow, 8);
        int failedCount = (int) eligibilityTable.getValueAt(selectedRow, 6);
        String cgpaStr = (String) eligibilityTable.getValueAt(selectedRow, 5);
        double cgpa = Double.parseDouble(cgpaStr);

        // Validate enrollment eligibility based on assignment requirements
        // Only students who are ELIGIBLE for progression AND have 1-3 failed courses can enroll for recovery
        if (!eligibilityStatus.contains("✓")) {
            JOptionPane.showMessageDialog(this,
                    "Cannot enroll for recovery.\n\n"
                    + studentName + " is not eligible for progression.\n"
                    + "CGPA: " + cgpaStr + " | Failed Courses: " + failedCount + "\n\n"
                    + "Students who are ineligible must repeat the year.\n"
                    + "Recovery enrollment is only for eligible students with 1-3 failed courses.",
                    "Ineligible for Recovery",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (failedCount == 0) {
            JOptionPane.showMessageDialog(this,
                    "No recovery needed.\n\n"
                    + studentName + " has no failed courses.\n"
                    + "CGPA: " + cgpaStr + " | Failed Courses: 0\n\n"
                    + "This student can progress normally without recovery enrollment.",
                    "No Recovery Needed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        models.Student student = studentDAO.loadStudent(studentId);

        if (student != null) {
            EnrollmentDialog dialog = new EnrollmentDialog(this, student);
            dialog.setVisible(true);

            resetEligibilityFilters();
            loadEligibilityData();
        }
    }

    // Reports (placeholder)
    private JTable reportTable;
    private DefaultTableModel reportTableModel;
    private JComboBox<String> majorFilterBox1;
    private JComboBox<String> yearFilterBox1;
    private JTextField reportSearchField;

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(BACKGROUND_COLOR);

        JLabel searchLabel = new JLabel("Search");
        reportSearchField = new JTextField(20);

// Listen to typing changes
        reportSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterReportData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterReportData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterReportData();
            }
        });

        JLabel majorLabel1 = new JLabel("Major:");
        majorFilterBox1 = new JComboBox<>();
        majorFilterBox1.addItem("All Majors");
        majorFilterBox1.addActionListener(e -> filterReportData());

        JLabel yearLabel1 = new JLabel("Year:");
        yearFilterBox1 = new JComboBox<>();
        yearFilterBox1.addItem("All Years");
        yearFilterBox1.addItem("1");
        yearFilterBox1.addItem("2");
        yearFilterBox1.addItem("3");
        yearFilterBox1.addItem("4");
        yearFilterBox1.addActionListener(e -> filterReportData());

        filterPanel.add(searchLabel);
        filterPanel.add(reportSearchField);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(majorLabel1);
        filterPanel.add(majorFilterBox1);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(yearLabel1);
        filterPanel.add(yearFilterBox1);

        topPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Student ID", "Name", "Major", "Year", "Semester",
            "CGPA"};
        reportTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(reportTableModel);

        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        reportTable.setRowHeight(35);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        reportTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        reportTable.getColumnModel().getColumn(5).setPreferredWidth(120);


        reportTable.getColumnModel().getColumn(5).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    String status = value != null ? value.toString() : "";
                    if (status.contains("✓")) {
                        label.setBackground(new Color(198, 239, 206));
                        label.setForeground(new Color(0, 128, 0));
                    } else {
                        label.setBackground(new Color(255, 199, 206));
                        label.setForeground(new Color(192, 0, 0));
                    }
                }
                label.setOpaque(true);
                return label;
            }
        }
        );

        javax.swing.table.DefaultTableCellRenderer centerHeaderRenderer1 = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.BOLD, 12));
                return c;
            }
        };

        centerHeaderRenderer1.setHorizontalAlignment(SwingConstants.CENTER);
        reportTable.getTableHeader().setDefaultRenderer(centerHeaderRenderer1);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton1 = new JButton("Refresh Data");
        refreshButton1.setBackground(SECONDARY_COLOR);
        refreshButton1.setForeground(Color.BLACK);
        refreshButton1.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton1.setFocusPainted(false);
        refreshButton1.addActionListener(e -> {
            resetReportFilters();
            loadReportData();
        });

        JButton exportButton = new JButton("Export to PDF");
        exportButton.setBackground(new Color(46, 204, 113));
        exportButton.setForeground(Color.BLACK);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportButton.setFocusPainted(false);
        exportButton.addActionListener(e -> exportSelectedStudent());

        buttonPanel.add(refreshButton1);
        buttonPanel.add(exportButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadReportData();
        utils.TableUtils.centerAlignTable(reportTable);

        return panel;
    }

    private void loadReportData() {
        reportTableModel.setRowCount(0);
        majorFilterBox1.removeAllItems();
        majorFilterBox1.addItem("All Majors");

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        java.util.List<models.Student> students = studentDAO.loadAllStudents();
        java.util.Set<String> majors = new java.util.HashSet<>();
        models.AcademicReport academicReport = new models.AcademicReport();


        for (models.Student student : students) {
        academicReport.setStudentId(student.getStudentId());
        double cgpa = academicReport.calculateCGPA();
            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getMajor(),
                student.getYear(),
                student.getSemester(),
                String.format("%.2f", cgpa)
            };
            reportTableModel.addRow(row);
            majors.add(student.getMajor());
        }

        for (String major : majors) {
            majorFilterBox1.addItem(major);
        }
    }

    private void filterReportData() {
        reportTableModel.setRowCount(0);

        String selectedMajor = (String) majorFilterBox1.getSelectedItem();
        if (selectedMajor == null) {
                selectedMajor = "All Majors"; // default value
            
        }   
        String selectedYear = (String) yearFilterBox1.getSelectedItem();
        String keyword = reportSearchField.getText().trim().toLowerCase();

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        java.util.List<models.Student> students = studentDAO.loadAllStudents();
        models.AcademicReport academicReport = new models.AcademicReport();

        for (models.Student student : students) {
            academicReport.setStudentId(student.getStudentId());
            double cgpa = academicReport.calculateCGPA();
            if (!selectedMajor.equals("All Majors") && !student.getMajor().equalsIgnoreCase(selectedMajor)) {
                continue;
            }

            if (!selectedYear.equals("All Years") && student.getYear() != Integer.parseInt(selectedYear)) {
                continue;
            }

            if (!keyword.isEmpty() && !student.getStudentId().toLowerCase().contains(keyword)
                    && !student.getFullName().toLowerCase().contains(keyword)) {
                continue;
            }

            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getMajor(),
                student.getYear(),
                student.getSemester(),
                String.format("%.2f", cgpa)
            };

            reportTableModel.addRow(row);
        }
    }

    private void resetReportFilters() {
        majorFilterBox1.setSelectedItem("All Majors");
        yearFilterBox1.setSelectedItem("All Years");
        reportSearchField.setText("");
        loadReportData();
    }

    private void exportSelectedStudent() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) reportTable.getValueAt(selectedRow, 0);
        dao.StudentDAO studentDAO = new dao.StudentDAO();
        models.Student student = studentDAO.loadStudent(studentId);

        if (student == null) {
            JOptionPane.showMessageDialog(this,
                    "Selected student not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetch all semesters for that student
        java.util.List<String> semesters = new ArrayList<>();
        dao.GradeDAO gradeDAO = new dao.GradeDAO();
        for (var g : gradeDAO.loadGradesByStudent(studentId)) {
            if (!semesters.contains(g.getSemesterId())) {
                semesters.add(g.getSemesterId());
            }
        }

        if (semesters.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No semester records found for this student.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create semester selection combo box
        JComboBox<String> semesterBox = new JComboBox<>();
        for (String sem : semesters) {
            semesterBox.addItem(sem);
        }

        int option = JOptionPane.showConfirmDialog(this, semesterBox,
                "Select Semester to Export", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedSemester = (String) semesterBox.getSelectedItem();
            services.ReportGenerator reportGenerate = services.ReportGenerator.getInstance();
            models.AcademicReport report = reportGenerate.generateAcademicReport(student, selectedSemester);

            String pdfPath = reportGenerate.exportReportToPDF(report);
            if (pdfPath != null) {
                JOptionPane.showMessageDialog(this,
                        "PDF exported successfully:\n" + pdfPath,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error exporting PDF.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            System.out.println(report.generateReport());
        }
        
    }

    // ---------- Logout Button Action -----------
    // ---------------- Logout Confirmation ----------------
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // When user clicks YES
            services.AuthenticationService authService = services.AuthenticationService.getInstance();
            authService.logout(userId);

            dispose();   // close Admin Dashboard window
            new LoginFrame();   // go back to login screen
        }
        // If NO is clicked, then stays on Dashboard 

    }

    // Main method to launch the dashboard
    public static void main(String[] args) {
        AdminDashboard dash = new AdminDashboard("Admin User", "A001");
    }
}