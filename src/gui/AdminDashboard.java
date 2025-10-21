package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Admin dashboard interface for system administration.
 * Provides access to user management, system reports, and administrative functions.
 * Accessible only to users with Admin role.
 */
public class AdminDashboard extends JFrame {

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

    /**
     * Constructor to initialize the admin dashboard.
     *
     * @param adminName name of the logged-in admin
     * @param userId the user ID of the logged-in admin
     */
    public AdminDashboard(String adminName, String userId) {
        this.userId = userId;
        initializeFrame();
        initializeComponents(adminName);
        setVisible(true);
    }

    /**
     * Initializes the main frame settings.
     */
    private void initializeFrame() {
        setTitle("CRS - Admin Dashboard");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Initializes and arranges GUI components.
     */
    private void initializeComponents(String adminName) {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel(adminName);
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar Panel
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // Main Content Panel with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BACKGROUND_COLOR);

        // Add different content panels
        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createUserManagementPanel(), "UserManagement");
        mainContentPanel.add(createEligibilityCheckPanel(), "EligibilityCheck");
        mainContentPanel.add(createReportsPanel(), "Reports");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the header panel with title and user info.
     */
    private JPanel createHeaderPanel(String adminName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, 80));

        // Left side - Admin name
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(PRIMARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 0);
        welcomeLabel = new JLabel(adminName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel, gbc);
        panel.add(leftPanel, BorderLayout.WEST);

        // Center - Title
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Right side - Logout button
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(PRIMARY_COLOR);
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.anchor = GridBagConstraints.EAST;
        rightGbc.insets = new Insets(0, 0, 0, 20);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        rightPanel.add(logoutButton, rightGbc);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Creates the sidebar panel with navigation buttons.
     */
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(220, FRAME_HEIGHT));

        // Add top spacing
        panel.add(Box.createVerticalStrut(20));

        // Navigation Buttons
        String[] menuItems = {"Dashboard", "User Management", "Eligibility Check", "Reports"};
        String[] cardNames = {"Dashboard", "UserManagement", "EligibilityCheck", "Reports"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createSidebarButton(menuItems[i], cardNames[i]);
            panel.add(button);
            panel.add(Box.createVerticalStrut(10));
        }

        // Add spacing at bottom
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Creates a sidebar navigation button.
     */
    private JButton createSidebarButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 60));
        button.setPreferredSize(new Dimension(220, 60));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> cardLayout.show(mainContentPanel, cardName));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        return button;
    }

    /**
     * Creates the dashboard overview panel.
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;

        // Title
        JLabel titleLabel = new JLabel("System Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Statistics Cards
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        panel.add(createStatCard("Total Students", "0", new Color(46, 204, 113)), gbc);

        gbc.gridx = 1;
        panel.add(createStatCard("Total Instructors", "0", new Color(52, 152, 219)), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createStatCard("Active Courses", "0", new Color(155, 89, 182)), gbc);

        gbc.gridx = 1;
        panel.add(createStatCard("Recovery Plans", "0", new Color(230, 126, 34)), gbc);

        return panel;
    }

    /**
     * Creates a statistics card.
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 3));
        card.setPreferredSize(new Dimension(250, 120));

        JLabel titleLabel = new JLabel("  " + title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Creates the user management panel.
     */
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"User ID", "Name", "Email", "Role", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Load user data into table
        loadUserData(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addButton = createActionButton("Add User", PRIMARY_COLOR);
        JButton updateButton = createActionButton("Update User", SECONDARY_COLOR);
        JButton deactivateButton = createActionButton("Deactivate User", new Color(231, 76, 60));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deactivateButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the eligibility check panel.
     */
    private JPanel createEligibilityCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Student Eligibility Check");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Student ID", "Name", "CGPA", "Failed Courses", "Eligible"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton checkAllButton = createActionButton("Check All Students", PRIMARY_COLOR);
        JButton refreshButton = createActionButton("Refresh", SECONDARY_COLOR);

        buttonPanel.add(checkAllButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the reports panel.
     */
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("System Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Report Options
        JButton studentReportButton = createActionButton("Generate Student Report", PRIMARY_COLOR);
        studentReportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(studentReportButton);
        buttonPanel.add(Box.createVerticalStrut(15));

        JButton eligibilityReportButton = createActionButton("Generate Eligibility Report", SECONDARY_COLOR);
        eligibilityReportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(eligibilityReportButton);
        buttonPanel.add(Box.createVerticalStrut(15));

        JButton recoveryReportButton = createActionButton("Generate Recovery Plan Report", new Color(155, 89, 182));
        recoveryReportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(recoveryReportButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates an action button with consistent styling.
     */
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(250, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Loads user data from the database into the table model.
     */
    private void loadUserData(DefaultTableModel tableModel) {
        dao.UserDAO userDAO = new dao.UserDAO();
        java.util.List<models.User> users = userDAO.loadAllUsers();

        // Clear existing rows
        tableModel.setRowCount(0);

        // Add user data to table
        for (models.User user : users) {
            String name = "";
            if (user instanceof models.Admin) {
                name = ((models.Admin) user).getAdminName();
            } else if (user instanceof models.Instructor) {
                name = ((models.Instructor) user).getInstructorName();
            }

            Object[] rowData = {
                user.getUserId(),
                name,
                user.getEmail(),
                user.getRole().toString(),
                user.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Logs out the admin and returns to login screen.
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Update login log with logout timestamp
            services.AuthenticationService authService = services.AuthenticationService.getInstance();
            authService.logout(userId);

            dispose();
            new LoginFrame();
        }
    }

    /**
     * Main method for testing the admin dashboard.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard("Admin User", "A001"));
    }
}
