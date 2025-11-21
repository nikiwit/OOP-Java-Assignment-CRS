package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import services.AuthenticationService;
import services.UserService;
import models.User;
import models.Admin;
import models.Instructor;

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
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);

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

        gbc.gridwidth = 1; // Reset

        // ----- Stats Panels -----
        JPanel totalStudents = createStatBox("Total Students", "0", new Color(46, 204, 113));
        JPanel totalInstructors = createStatBox("Total Instructors", "0", new Color(52, 152, 219));
        JPanel activeCourses = createStatBox("Active Courses", "0", new Color(155, 89, 182));
        JPanel recoveryPlans = createStatBox("Recovery Plans", "0", new Color(230, 126, 34));

        // Row 1
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

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);

        panel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = { "User ID", "Name", "Email", "Role", "Status" };
        userTableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(userTableModel);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        userTable.setRowHeight(35);
        // Apply color renderer to the "Status" column (the 5th column index = 4)
        userTable.getColumnModel().getColumn(4).setCellRenderer(new StatusColorRenderer());



        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadUserData();
        utils.TableUtils.centerAlignTable(userTable);

        

        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // ---------- Add User button ----------
        JButton addButton = new JButton("Add User");
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new AddUserFrame(AdminDashboard.this);
            }
        });
        buttonPanel.add(addButton);

        // ---------- Update User button ----------
        JButton updateButton = new JButton("Update User");
        updateButton.setBackground(SECONDARY_COLOR);
        updateButton.setForeground(Color.WHITE);

        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserAction();
            }
        });
        buttonPanel.add(updateButton);

        // ---------- Deactivate User button ----------
        JButton deactivateButton = new JButton("Deactivate User");
        deactivateButton.setBackground(new Color(231, 76, 60));
        deactivateButton.setForeground(Color.WHITE);
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
            } 
            else if (user instanceof Instructor) {
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


    // Eligibility section (placeholder)
    private JPanel createEligibilityCheckPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    // Reports (placeholder)
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
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

