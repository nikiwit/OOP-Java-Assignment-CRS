package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import dao.CourseDAO;
import models.Course;
import static gui.UIConstants.*;

/**
 * Instructor dashboard interface for managing recovery plans.
 * Provides access to course management, recovery plan creation,
 * student grading, and progress tracking.
 * Accessible only to users with Instructor role.
 */
public class InstructorDashboard extends JFrame {

    // Using UIConstants for consistent styling
    private static final Color PRIMARY_COLOR = PRIMARY;
    private static final Color SECONDARY_COLOR = PRIMARY_DARK;
    private static final Color BACKGROUND_COLOR = BACKGROUND;
    private static final Color TEXT_COLOR = TEXT_PRIMARY;

    private JLabel welcomeLabel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private String userId;
    private List<Course> instructorCourses;

    /**
     * Constructor to initialize the instructor dashboard.
     *
     * @param instructorName name of the logged-in instructor
     * @param userId the user ID of the logged-in instructor
     */
    public InstructorDashboard(String instructorName, String userId) {
        this.userId = userId;

        // Load courses for this instructor
        CourseDAO courseDAO = new CourseDAO();
        this.instructorCourses = courseDAO.loadCoursesByInstructor(userId);

        System.out.println("Loaded " + instructorCourses.size() + " courses for instructor " + userId);

        initializeFrame();
        initializeComponents(instructorName);
        setVisible(true);
    }

    /**
     * Initializes the main frame settings.
     */
    private void initializeFrame() {
        setTitle("CRS - Academic Officer Dashboard");
        setMinimumSize(WINDOW_MIN_SIZE);
        setSize(WINDOW_MIN_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Initializes and arranges GUI components.
     */
    private void initializeComponents(String instructorName) {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel(instructorName);
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar Panel
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // Main Content Panel with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BACKGROUND_COLOR);

        // Add different content panels (removed Recovery Templates)
        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createCoursesPanel(), "Courses");
        mainContentPanel.add(createRecoveryPlansPanel(), "RecoveryPlans");
        mainContentPanel.add(createFailedComponentsPanel(), "Grading");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the header panel with title and user info.
     */
    private JPanel createHeaderPanel(String instructorName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));

        // Left side - Instructor name
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(PRIMARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, SPACING_MD, 0, 0);
        welcomeLabel = new JLabel(instructorName);
        welcomeLabel.setFont(FONT_BODY);
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel, gbc);
        panel.add(leftPanel, BorderLayout.WEST);

        // Center - Title
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Academic Officer Dashboard");
        titleLabel.setFont(FONT_H1);
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Right side - Logout button
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(PRIMARY_COLOR);
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.anchor = GridBagConstraints.EAST;
        rightGbc.insets = new Insets(0, 0, 0, SPACING_LG);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(FONT_BODY);
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
        panel.setBackground(SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));

        // Add top spacing
        panel.add(Box.createVerticalStrut(SPACING_LG));

        // Navigation Buttons with keyboard shortcuts
        String[] menuItems = {"Dashboard", "My Courses", "Recovery Plans", "Grading"};
        String[] cardNames = {"Dashboard", "Courses", "RecoveryPlans", "Grading"};
        int[] mnemonics = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4};

        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createSidebarButton(menuItems[i], cardNames[i], mnemonics[i]);
            panel.add(button);
            panel.add(Box.createVerticalStrut(SPACING_SM));
        }

        // Add spacing at bottom
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Creates a sidebar navigation button with keyboard shortcut.
     * @param text button text
     * @param cardName card to navigate to
     * @param mnemonic keyboard shortcut (KeyEvent.VK_*)
     */
    private JButton createSidebarButton(String text, String cardName, int mnemonic) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(SIDEBAR_WIDTH, SIDEBAR_BUTTON_HEIGHT));
        button.setPreferredSize(new Dimension(SIDEBAR_WIDTH, SIDEBAR_BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(SIDEBAR_WIDTH, SIDEBAR_BUTTON_HEIGHT));
        button.setBackground(SIDEBAR_BG);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BODY_BOLD);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, SPACING_LG, 0, 0));

        // Set keyboard shortcut (Ctrl+1, Ctrl+2, etc.)
        button.setMnemonic(mnemonic);
        button.setToolTipText("Press Ctrl+" + KeyEvent.getKeyText(mnemonic) + " to navigate");

        button.addActionListener(e -> {
            cardLayout.show(mainContentPanel, cardName);
            updateActiveButton(button);
        });

        // Hover effect with smooth color transition
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(SIDEBAR_BG)) {
                    button.setBackground(SIDEBAR_HOVER);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(SIDEBAR_HOVER)) {
                    button.setBackground(SIDEBAR_BG);
                }
            }
        });

        return button;
    }

    /**
     * Updates the active button state in sidebar.
     */
    private void updateActiveButton(JButton activeButton) {
        // Reset all sidebar buttons to default state
        Component[] components = ((JPanel) activeButton.getParent()).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(SIDEBAR_BG);
            }
        }
        // Highlight the active button
        activeButton.setBackground(SIDEBAR_ACTIVE);
    }

    /**
     * Creates the dashboard overview panel.
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Academic Officer Dashboard - Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(BACKGROUND_COLOR);

        // Statistics Panel (Top)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Calculate statistics
        int totalCourses = instructorCourses.size();
        int studentsInRecovery = getStudentsInRecoveryCount();
        int totalStudents = getTotalStudentsCount();
        int failedStudents = getFailedStudentsCount();

        statsPanel.add(createStatCard("Total Courses", String.valueOf(totalCourses), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Students in Recovery", String.valueOf(studentsInRecovery), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Students", String.valueOf(totalStudents), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Failed Students", String.valueOf(failedStudents), new Color(231, 76, 60)));

        mainContent.add(statsPanel, BorderLayout.NORTH);

        // Courses List Panel (Bottom)
        JPanel coursesPanel = new JPanel(new BorderLayout(10, 10));
        coursesPanel.setBackground(BACKGROUND_COLOR);

        JLabel coursesLabel = new JLabel("My Courses");
        coursesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        coursesLabel.setForeground(TEXT_COLOR);
        coursesPanel.add(coursesLabel, BorderLayout.NORTH);

        // Create courses list area
        JPanel coursesListPanel = new JPanel();
        coursesListPanel.setLayout(new BoxLayout(coursesListPanel, BoxLayout.Y_AXIS));
        coursesListPanel.setBackground(Color.WHITE);
        coursesListPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Add each course to the list
        for (Course course : instructorCourses) {
            JPanel courseItem = createCourseListItem(course);
            coursesListPanel.add(courseItem);
            coursesListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Make it scrollable
        JScrollPane coursesScrollPane = new JScrollPane(coursesListPanel);
        coursesScrollPane.setPreferredSize(new Dimension(700, 300));
        coursesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        coursesScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        coursesPanel.add(coursesScrollPane, BorderLayout.CENTER);

        mainContent.add(coursesPanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a course list item panel.
     */
    private JPanel createCourseListItem(Course course) {
        JPanel item = new JPanel(new BorderLayout(10, 5));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Course ID and Name
        JLabel courseLabel = new JLabel(course.getCourseId() + " - " + course.getCourseName());
        courseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        courseLabel.setForeground(TEXT_COLOR);

        // Course details (credits, capacity)
        JLabel detailsLabel = new JLabel("Credits: " + course.getCredits() + " | Capacity: " + course.getCapacity());
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsLabel.setForeground(new Color(100, 100, 100));

        JPanel textPanel = new JPanel(new BorderLayout(5, 2));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(courseLabel, BorderLayout.NORTH);
        textPanel.add(detailsLabel, BorderLayout.CENTER);

        item.add(textPanel, BorderLayout.CENTER);

        // Add hover effect
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(240, 248, 255));
                textPanel.setBackground(new Color(240, 248, 255));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
                textPanel.setBackground(Color.WHITE);
            }
        });

        return item;
    }

    /**
     * Gets the count of students currently in recovery enrollments.
     */
    private int getStudentsInRecoveryCount() {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        java.util.Set<String> uniqueStudents = new java.util.HashSet<>();

        for (Course course : instructorCourses) {
            List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByCourse(course.getCourseId());
            for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                uniqueStudents.add(enrollment.getStudentId());
            }
        }

        return uniqueStudents.size();
    }

    /**
     * Gets the total count of students across all instructor's courses.
     */
    private int getTotalStudentsCount() {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        java.util.Set<String> uniqueStudents = new java.util.HashSet<>();

        for (Course course : instructorCourses) {
            List<models.Result> results = resultDAO.loadResultsByCourse(course.getCourseId());
            for (models.Result result : results) {
                uniqueStudents.add(result.getStudentId());
            }
        }

        return uniqueStudents.size();
    }

    /**
     * Gets the count of students who failed in instructor's courses.
     */
    private int getFailedStudentsCount() {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        java.util.Set<String> failedStudents = new java.util.HashSet<>();

        for (Course course : instructorCourses) {
            List<models.Result> results = resultDAO.loadResultsByCourse(course.getCourseId());
            for (models.Result result : results) {
                if (result.getStatus() == enums.GradeStatus.FAILED) {
                    failedStudents.add(result.getStudentId() + "_" + result.getCourseId());
                }
            }
        }

        return failedStudents.size();
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
     * Creates the courses panel.
     */
    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("My Courses (" + instructorCourses.size() + " total)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Course ID", "Course Name", "Credits", "Capacity", "Instructor ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Populate table with actual course data
        for (Course course : instructorCourses) {
            Object[] rowData = {
                course.getCourseId(),
                course.getCourseName(),
                course.getCredits(),
                course.getCapacity(),
                course.getInstructorId()
            };
            tableModel.addRow(rowData);
        }

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

        JButton recoveryActionsButton = createActionButton("Recovery Actions", PRIMARY_COLOR);
        JButton refreshButton = createActionButton("Refresh", SECONDARY_COLOR);

        // Recovery Actions button action
        recoveryActionsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String courseId = (String) tableModel.getValueAt(selectedRow, 0);
                String courseName = (String) tableModel.getValueAt(selectedRow, 1);
                showCourseRecoveryActionsDialog(courseId, courseName);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a course to manage recovery actions.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Refresh button action
        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            for (Course course : instructorCourses) {
                Object[] rowData = {
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getCapacity(),
                    course.getInstructorId()
                };
                tableModel.addRow(rowData);
            }
            JOptionPane.showMessageDialog(panel, "Course list refreshed!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        buttonPanel.add(recoveryActionsButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the recovery templates management panel.
     */
    private JPanel createRecoveryTemplatesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Recovery Plan Templates - Course-Level Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table with template data (removed Template ID column as requested)
        String[] columnNames = {"Course ID", "Plan Title", "Actions", "Status", "Created", "Modified"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Load template data for this instructor
        loadTemplateData(tableModel);

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton createButton = createActionButton("Create Template", new Color(46, 204, 113));
        JButton editButton = createActionButton("Edit Metadata", PRIMARY_COLOR);
        JButton manageActionsButton = createActionButton("Manage Actions", SECONDARY_COLOR);
        JButton deactivateButton = createActionButton("Deactivate", new Color(231, 76, 60));
        JButton refreshButton = createActionButton("Refresh", new Color(52, 152, 219));

        // Create Template button action
        createButton.addActionListener(e -> showCreateTemplateDialog(tableModel));

        // Edit Metadata button action
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Column indices changed: Course ID (0), Plan Title (1)
                String courseId = (String) tableModel.getValueAt(selectedRow, 0);
                // Get template ID by looking it up using courseId and instructorId
                String templateId = getTemplateIdByCourse(courseId);
                showEditTemplateDialog(templateId, tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a template to edit.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Manage Actions button action
        manageActionsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Column indices: Course ID (0), Plan Title (1)
                String courseId = (String) tableModel.getValueAt(selectedRow, 0);
                String planTitle = (String) tableModel.getValueAt(selectedRow, 1);
                String templateId = getTemplateIdByCourse(courseId);
                showManageActionsDialog(templateId, courseId, planTitle, tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a template to manage actions.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Deactivate button action
        deactivateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String courseId = (String) tableModel.getValueAt(selectedRow, 0);
                String templateId = getTemplateIdByCourse(courseId);
                int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to deactivate this template?\nThis will prevent new enrollments but preserve existing data.",
                    "Confirm Deactivation",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    models.Instructor instructor = getInstructorObject();
                    boolean success = instructor.deleteRecoveryPlanTemplate(templateId);

                    if (success) {
                        JOptionPane.showMessageDialog(panel, "Template deactivated successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                        tableModel.setRowCount(0);
                        loadTemplateData(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to deactivate template.", "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a template to deactivate.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Refresh button action
        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadTemplateData(tableModel);
            JOptionPane.showMessageDialog(panel, "Data refreshed successfully!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        buttonPanel.add(createButton);
        buttonPanel.add(editButton);
        buttonPanel.add(manageActionsButton);
        buttonPanel.add(deactivateButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Helper method to get template ID by course ID.
     * Since we removed the Template ID column from the display, we need to look it up.
     */
    private String getTemplateIdByCourse(String courseId) {
        models.Instructor instructor = getInstructorObject();
        List<models.CourseRecoveryPlanTemplate> templates = instructor.getRecoveryPlanTemplates();

        for (models.CourseRecoveryPlanTemplate template : templates) {
            if (template.getCourseId().equals(courseId)) {
                return template.getTemplateId();
            }
        }
        return null;
    }

    /**
     * Loads template data for the instructor's courses into the table.
     */
    private void loadTemplateData(DefaultTableModel tableModel) {
        models.Instructor instructor = getInstructorObject();
        List<models.CourseRecoveryPlanTemplate> templates = instructor.getRecoveryPlanTemplates();

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

        for (models.CourseRecoveryPlanTemplate template : templates) {
            String createdDate = template.getCreatedDate() != null ? dateFormat.format(template.getCreatedDate()) : "-";
            String modifiedDate = template.getLastModifiedDate() != null ? dateFormat.format(template.getLastModifiedDate()) : "-";
            String status = template.isActive() ? "Active" : "Inactive";

            // Removed Template ID from row data as requested
            Object[] rowData = {
                template.getCourseId(),
                template.getPlanTitle(),
                template.getActionCount(),
                status,
                createdDate,
                modifiedDate
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Shows dialog to create a new recovery plan template.
     */
    private void showCreateTemplateDialog(DefaultTableModel tableModel) {
        JPanel dialogPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Course selection
        JLabel courseLabel = new JLabel("Course:");
        JComboBox<String> courseCombo = new JComboBox<>();
        for (Course course : instructorCourses) {
            courseCombo.addItem(course.getCourseId() + " - " + course.getCourseName());
        }

        JLabel titleLabel = new JLabel("Plan Title:");
        JTextField titleField = new JTextField();

        JLabel descLabel = new JLabel("Description:");
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        dialogPanel.add(courseLabel);
        dialogPanel.add(courseCombo);
        dialogPanel.add(titleLabel);
        dialogPanel.add(titleField);
        dialogPanel.add(descLabel);
        dialogPanel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Create Recovery Plan Template",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedCourse = (String) courseCombo.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            String courseId = selectedCourse.split(" - ")[0];
            String title = titleField.getText().trim();
            String description = descArea.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a title.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            models.Instructor instructor = getInstructorObject();
            models.CourseRecoveryPlanTemplate template = instructor.createRecoveryPlanTemplate(courseId, title, description);

            if (template != null) {
                JOptionPane.showMessageDialog(this, "Template created successfully!\nTemplate ID: " + template.getTemplateId(), "Success", JOptionPane.PLAIN_MESSAGE);
                tableModel.setRowCount(0);
                loadTemplateData(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create template.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Shows dialog to edit template metadata.
     */
    private void showEditTemplateDialog(String templateId, DefaultTableModel tableModel) {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();
        models.CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);

        if (template == null) {
            JOptionPane.showMessageDialog(this, "Template not found.", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel titleLabel = new JLabel("New Title:");
        JTextField titleField = new JTextField(template.getPlanTitle());

        JLabel descLabel = new JLabel("New Description:");
        JTextArea descArea = new JTextArea(template.getPlanDescription(), 3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        dialogPanel.add(titleLabel);
        dialogPanel.add(titleField);
        dialogPanel.add(descLabel);
        dialogPanel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Edit Template Metadata",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newTitle = titleField.getText().trim();
            String newDescription = descArea.getText().trim();

            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            models.Instructor instructor = getInstructorObject();
            boolean success = instructor.updateRecoveryPlanTemplateMetadata(templateId, newTitle, newDescription);

            if (success) {
                JOptionPane.showMessageDialog(this, "Template updated successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                tableModel.setRowCount(0);
                loadTemplateData(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update template.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Shows dialog to manage actions for a template.
     */
    private void showManageActionsDialog(String templateId, String courseId, String planTitle, DefaultTableModel parentTableModel) {
        JDialog actionsDialog = new JDialog(this, "Manage Actions - " + planTitle, true);
        actionsDialog.setSize(900, 600);
        actionsDialog.setLocationRelativeTo(this);
        actionsDialog.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Actions for: " + planTitle + " (" + courseId + ")");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        actionsDialog.add(titlePanel, BorderLayout.NORTH);

        // Actions table
        String[] columnNames = {"Action #", "Title", "Description", "Has Grade", "Status"};
        DefaultTableModel actionsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadActionsData(templateId, actionsTableModel);

        JTable actionsTable = new JTable(actionsTableModel);
        actionsTable.setRowHeight(35);
        actionsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        actionsTable.getTableHeader().setBackground(SECONDARY_COLOR);
        actionsTable.getTableHeader().setForeground(Color.WHITE);
        actionsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        actionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(actionsTable);
        actionsDialog.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addActionButton = createActionButton("Add Action", new Color(46, 204, 113));
        JButton editActionButton = createActionButton("Edit Action", PRIMARY_COLOR);
        JButton deleteActionButton = createActionButton("Delete Action", new Color(231, 76, 60));
        JButton refreshButton = createActionButton("Refresh", SECONDARY_COLOR);
        JButton closeButton = createActionButton("Close", new Color(149, 165, 166));

        // Add Action button
        addActionButton.addActionListener(e -> {
            showAddActionDialog(templateId, courseId, actionsTableModel, parentTableModel);
        });

        // Edit Action button
        editActionButton.addActionListener(e -> {
            int selectedRow = actionsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String actionIdFromTable = getActionIdFromTable(templateId, selectedRow, actionsTableModel);
                showEditActionDialog(actionIdFromTable, actionsTableModel);
            } else {
                JOptionPane.showMessageDialog(actionsDialog, "Please select an action to edit.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Delete Action button
        deleteActionButton.addActionListener(e -> {
            int selectedRow = actionsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String actionIdFromTable = getActionIdFromTable(templateId, selectedRow, actionsTableModel);
                int confirm = JOptionPane.showConfirmDialog(actionsDialog,
                    "Are you sure you want to delete this action?\nIt will be deactivated but preserved in the system.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    models.Instructor instructor = getInstructorObject();
                    boolean success = instructor.deleteActionFromTemplate(templateId, actionIdFromTable);

                    if (success) {
                        JOptionPane.showMessageDialog(actionsDialog, "Action deleted successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                        actionsTableModel.setRowCount(0);
                        loadActionsData(templateId, actionsTableModel);
                        parentTableModel.setRowCount(0);
                        loadTemplateData(parentTableModel);
                    } else {
                        JOptionPane.showMessageDialog(actionsDialog, "Failed to delete action.", "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(actionsDialog, "Please select an action to delete.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Refresh button
        refreshButton.addActionListener(e -> {
            actionsTableModel.setRowCount(0);
            loadActionsData(templateId, actionsTableModel);
            JOptionPane.showMessageDialog(actionsDialog, "Actions refreshed!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        // Close button
        closeButton.addActionListener(e -> actionsDialog.dispose());

        buttonPanel.add(addActionButton);
        buttonPanel.add(editActionButton);
        buttonPanel.add(deleteActionButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        actionsDialog.add(buttonPanel, BorderLayout.SOUTH);

        actionsDialog.setVisible(true);
    }

    /**
     * Loads actions data for a template into the table.
     */
    private void loadActionsData(String templateId, DefaultTableModel tableModel) {
        models.Instructor instructor = getInstructorObject();
        List<models.RecoveryCourseAction> actions = instructor.getTemplateActions(templateId);

        for (models.RecoveryCourseAction action : actions) {
            String description = action.getDescription();
            if (description != null && description.length() > 50) {
                description = description.substring(0, 50) + "...";
            }

            Object[] rowData = {
                action.getActionNumber(),
                action.getTitle(),
                description,
                action.isHasGrade() ? "Yes" : "No",
                action.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Gets the action ID from a table row.
     */
    private String getActionIdFromTable(String templateId, int rowIndex, DefaultTableModel tableModel) {
        models.Instructor instructor = getInstructorObject();
        List<models.RecoveryCourseAction> actions = instructor.getTemplateActions(templateId);

        if (rowIndex >= 0 && rowIndex < actions.size()) {
            return actions.get(rowIndex).getId();
        }
        return null;
    }

    /**
     * Shows dialog to add a new action to a template.
     */
    private void showAddActionDialog(String templateId, String courseId, DefaultTableModel actionsTableModel, DefaultTableModel parentTableModel) {
        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel numberLabel = new JLabel("Action Number:");
        JTextField numberField = new JTextField();

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();

        JLabel descLabel = new JLabel("Description:");
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        JLabel gradeLabel = new JLabel("Has Grade:");
        JCheckBox gradeCheckBox = new JCheckBox();

        dialogPanel.add(numberLabel);
        dialogPanel.add(numberField);
        dialogPanel.add(titleLabel);
        dialogPanel.add(titleField);
        dialogPanel.add(descLabel);
        dialogPanel.add(descScroll);
        dialogPanel.add(gradeLabel);
        dialogPanel.add(gradeCheckBox);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add Action to Template",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int actionNumber = Integer.parseInt(numberField.getText().trim());
                String title = titleField.getText().trim();
                String description = descArea.getText().trim();
                boolean hasGrade = gradeCheckBox.isSelected();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a title.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                models.Instructor instructor = getInstructorObject();
                boolean success = instructor.addActionToTemplate(templateId, actionNumber, title, description, hasGrade);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Action added successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                    actionsTableModel.setRowCount(0);
                    loadActionsData(templateId, actionsTableModel);
                    parentTableModel.setRowCount(0);
                    loadTemplateData(parentTableModel);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add action.", "Error", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Action Number.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Shows dialog to edit an existing action.
     */
    private void showEditActionDialog(String actionId, DefaultTableModel actionsTableModel) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        models.RecoveryCourseAction action = actionDAO.loadAction(actionId);

        if (action == null) {
            JOptionPane.showMessageDialog(this, "Action not found.", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel titleLabel = new JLabel("New Title:");
        JTextField titleField = new JTextField(action.getTitle());

        JLabel descLabel = new JLabel("New Description:");
        JTextArea descArea = new JTextArea(action.getDescription(), 3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        dialogPanel.add(titleLabel);
        dialogPanel.add(titleField);
        dialogPanel.add(descLabel);
        dialogPanel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Edit Action",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newTitle = titleField.getText().trim();
            String newDescription = descArea.getText().trim();

            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            models.Instructor instructor = getInstructorObject();
            boolean success = instructor.updateRecoveryAction(actionId, newTitle, newDescription);

            if (success) {
                JOptionPane.showMessageDialog(this, "Action updated successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                actionsTableModel.setRowCount(0);
                // Need to reload with template ID - get it from the action
                String templateId = action.getCourseId(); // This is a simplification
                loadActionsData(templateId, actionsTableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update action.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Helper method to create an Instructor object with proper initialization.
     */
    private models.Instructor getInstructorObject() {
        models.Instructor instructor = new models.Instructor(userId, "", "", "");
        instructor.setAssignedCourseIds(new ArrayList<>());
        for (Course c : instructorCourses) {
            instructor.assignCourse(c.getCourseId());
        }
        return instructor;
    }

    /**
     * Creates the recovery plans management panel.
     */
    private JPanel createRecoveryPlansPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Recovery Plans - Student Progress Tracking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table with recovery data (added Passed Exam and Passed Assignment columns)
        String[] columnNames = {"Student ID", "Course ID", "Action #", "Task Title", "Status", "Grade", "Progress", "Passed Exam", "Passed Assignment"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Load recovery data for this instructor
        loadRecoveryData(tableModel);

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // Make table wider to accommodate new columns
        table.setPreferredScrollableViewportSize(new Dimension(1200, 400));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton switchActionButton = createActionButton("Switch Action", new Color(243, 156, 18));
        JButton gradeButton = createActionButton("Grade Task", PRIMARY_COLOR);
        JButton submitButton = createActionButton("Submit Student", new Color(155, 89, 182));
        JButton addNotesButton = createActionButton("Add Notes", SECONDARY_COLOR);
        JButton refreshButton = createActionButton("Refresh", new Color(46, 204, 113));

        // Initially hide submit button - will be shown when valid row is selected
        submitButton.setVisible(false);

        // Add table selection listener to control button visibility
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String courseId = (String) tableModel.getValueAt(selectedRow, 1);
                    int actionNumber = (Integer) tableModel.getValueAt(selectedRow, 2);
                    String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

                    // Check if this is the last active action
                    dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
                    List<models.RecoveryCourseAction> courseActions = actionDAO.loadActionsByCourse(courseId);

                    int maxActiveActionNumber = -1;
                    for (models.RecoveryCourseAction action : courseActions) {
                        if (action.isActive() && action.getActionNumber() > maxActiveActionNumber) {
                            maxActiveActionNumber = action.getActionNumber();
                        }
                    }

                    boolean isLastAction = (actionNumber == maxActiveActionNumber);
                    boolean isNotSubmitted = !currentStatus.equals("SUBMITTED");

                    // Show submit button only if it's the last action and not already submitted
                    submitButton.setVisible(isLastAction && isNotSubmitted);

                    // Disable switch action if already submitted
                    switchActionButton.setEnabled(isNotSubmitted);
                } else {
                    submitButton.setVisible(false);
                    switchActionButton.setEnabled(true);
                }
            }
        });

        // Switch Action button action
        switchActionButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentId = (String) tableModel.getValueAt(selectedRow, 0);
                String courseId = (String) tableModel.getValueAt(selectedRow, 1);
                int currentActionNumber = (Integer) tableModel.getValueAt(selectedRow, 2);
                String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

                // Prevent switching if already submitted
                if (currentStatus.equals("SUBMITTED")) {
                    JOptionPane.showMessageDialog(panel,
                        "Cannot switch action for a submitted task.\nYou can only grade submitted tasks.",
                        "Task Already Submitted", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                showSwitchActionDialog(studentId, courseId, currentActionNumber, tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a task to switch action.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Grade button action
        gradeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentId = (String) tableModel.getValueAt(selectedRow, 0);
                String courseId = (String) tableModel.getValueAt(selectedRow, 1);
                int actionNumber = (Integer) tableModel.getValueAt(selectedRow, 2);

                // Validate that the action has grading enabled
                dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
                List<models.RecoveryCourseAction> courseActions = actionDAO.loadActionsByCourse(courseId);

                models.RecoveryCourseAction selectedAction = null;
                for (models.RecoveryCourseAction action : courseActions) {
                    if (action.getActionNumber() == actionNumber) {
                        selectedAction = action;
                        break;
                    }
                }

                if (selectedAction == null) {
                    JOptionPane.showMessageDialog(panel,
                        "Error: Action not found.",
                        "Action Not Found",
                        JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                if (!selectedAction.isHasGrade()) {
                    JOptionPane.showMessageDialog(panel,
                        "This action cannot be graded.\n\n" +
                        "Action #" + actionNumber + ": " + selectedAction.getTitle() + "\n\n" +
                        "This is a non-graded activity (e.g., lecture review, attendance, reading).\n" +
                        "Only actions with grading enabled can receive grades.",
                        "Cannot Grade Action",
                        JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                showGradeDialog(studentId, courseId, actionNumber, tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a task to grade.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Submit Student button action
        submitButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentId = (String) tableModel.getValueAt(selectedRow, 0);
                String courseId = (String) tableModel.getValueAt(selectedRow, 1);
                int actionNumber = (Integer) tableModel.getValueAt(selectedRow, 2);

                int confirm = JOptionPane.showConfirmDialog(panel,
                    "Submit this student's recovery task?\n\n" +
                    "Student ID: " + studentId + "\n" +
                    "Course ID: " + courseId + "\n" +
                    "Action #: " + actionNumber + "\n\n" +
                    "This will change the status to SUBMITTED.\n" +
                    "After submission, you cannot switch actions but can still grade.",
                    "Confirm Submission",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
                    List<models.RecoveryCourseEnrollment> enrollments =
                        enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

                    models.RecoveryCourseEnrollment targetEnrollment = null;
                    for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                        if (enrollment.getActionNumber() == actionNumber) {
                            targetEnrollment = enrollment;
                            break;
                        }
                    }

                    if (targetEnrollment != null) {
                        targetEnrollment.setStatus(models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED);
                        boolean success = enrollmentDAO.updateEnrollment(targetEnrollment);

                        if (success) {
                            JOptionPane.showMessageDialog(panel,
                                "Student successfully submitted!",
                                "Success",
                                JOptionPane.PLAIN_MESSAGE);

                            // Refresh table
                            tableModel.setRowCount(0);
                            loadRecoveryData(tableModel);
                        } else {
                            JOptionPane.showMessageDialog(panel,
                                "Failed to submit student.",
                                "Error",
                                JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            }
        });

        // Add notes button action
        addNotesButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentId = (String) tableModel.getValueAt(selectedRow, 0);
                String courseId = (String) tableModel.getValueAt(selectedRow, 1);
                int actionNumber = (Integer) tableModel.getValueAt(selectedRow, 2);
                showNotesDialog(studentId, courseId, actionNumber, tableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a task to add notes.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Refresh button action
        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0); // Clear table
            loadRecoveryData(tableModel); // Reload data
            JOptionPane.showMessageDialog(panel, "Data refreshed successfully!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        buttonPanel.add(switchActionButton);
        buttonPanel.add(gradeButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(addNotesButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Loads recovery enrollment data for the instructor's courses into the table.
     * Validates that enrollments match existing active recovery course actions.
     */
    private void loadRecoveryData(DefaultTableModel tableModel) {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        dao.ResultDAO resultDAO = new dao.ResultDAO();

        int invalidEnrollmentsCount = 0;
        StringBuilder invalidEnrollments = new StringBuilder();

        // Get all enrollments for instructor's courses
        for (Course course : instructorCourses) {
            List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByCourse(course.getCourseId());

            // Load active actions for this course
            List<models.RecoveryCourseAction> courseActions = actionDAO.loadActionsByCourse(course.getCourseId());
            java.util.Map<Integer, models.RecoveryCourseAction> activeActionsMap = new java.util.HashMap<>();
            for (models.RecoveryCourseAction action : courseActions) {
                if (action.isActive()) {
                    activeActionsMap.put(action.getActionNumber(), action);
                }
            }

            for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                // Validate that enrollment's action exists and is active
                String validationWarning = "";

                models.RecoveryCourseAction correspondingAction = activeActionsMap.get(enrollment.getActionNumber());
                if (correspondingAction == null) {
                    validationWarning = " ⚠️ INVALID: Action not found or inactive";
                    invalidEnrollmentsCount++;
                    invalidEnrollments.append(String.format("- Student %s, Course %s, Action #%d: Action does not exist or is inactive\n",
                        enrollment.getStudentId(), enrollment.getCourseId(), enrollment.getActionNumber()));
                }

                // Calculate progress
                List<models.RecoveryCourseEnrollment> studentEnrollments =
                    enrollmentDAO.loadEnrollmentsByStudentAndCourse(enrollment.getStudentId(), enrollment.getCourseId());

                int completed = 0;
                int total = studentEnrollments.size();
                for (models.RecoveryCourseEnrollment e : studentEnrollments) {
                    if (e.isCompleted()) completed++;
                }

                String progress = completed + "/" + total;
                String gradeStr = enrollment.getGrade() != null ? enrollment.getGrade().toString() : "-";

                // Add warning to title if invalid
                String titleDisplay = enrollment.getTitle() + validationWarning;

                // Fetch Result data to get passed exam/assignment info
                models.Result studentResult = resultDAO.getMostRecentResult(enrollment.getStudentId(), enrollment.getCourseId());
                String passedExamDisplay = "-";
                String passedAssignmentDisplay = "-";

                if (studentResult != null) {
                    // Use checkmark (✓) for passed, cross (✗) for failed
                    passedExamDisplay = studentResult.isPassedExam() ? "✓" : "✗";
                    passedAssignmentDisplay = studentResult.isPassedAssignment() ? "✓" : "✗";
                }

                // Added Passed Exam and Passed Assignment columns
                Object[] rowData = {
                    enrollment.getStudentId(),
                    enrollment.getCourseId(),
                    enrollment.getActionNumber(),
                    titleDisplay,
                    enrollment.getStatus().toString(),
                    gradeStr,
                    progress,
                    passedExamDisplay,
                    passedAssignmentDisplay
                };
                tableModel.addRow(rowData);
            }
        }

        // Show warning if there are invalid enrollments
        if (invalidEnrollmentsCount > 0) {
            JOptionPane.showMessageDialog(this,
                "Warning: " + invalidEnrollmentsCount + " enrollment(s) have invalid action references!\n\n" +
                "These enrollments reference actions that don't exist or are inactive:\n\n" +
                invalidEnrollments.toString() + "\n" +
                "Please use 'Switch Action' to assign valid actions to these students.",
                "Invalid Enrollments Detected",
                JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Shows dialog to grade a recovery task and update status.
     * SUBMITTED status can only be set if this is the last active action for the course.
     */
    private void showGradeDialog(String studentId, String courseId, int actionNumber, DefaultTableModel tableModel) {
        // Check if this is the last active action for the course
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<models.RecoveryCourseAction> courseActions = actionDAO.loadActionsByCourse(courseId);

        // Find the highest action number among active actions
        int maxActiveActionNumber = -1;
        for (models.RecoveryCourseAction action : courseActions) {
            if (action.isActive() && action.getActionNumber() > maxActiveActionNumber) {
                maxActiveActionNumber = action.getActionNumber();
            }
        }

        boolean isLastAction = (actionNumber == maxActiveActionNumber);

        JPanel dialogPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel studentLabel = new JLabel("Student ID:");
        JLabel studentValue = new JLabel(studentId);
        JLabel courseLabel = new JLabel("Course ID:");
        JLabel courseValue = new JLabel(courseId);
        JLabel actionLabel = new JLabel("Action Number:");
        JLabel actionValue = new JLabel(String.valueOf(actionNumber));

        JLabel gradeLabel = new JLabel("Grade (0-100):");
        JTextField gradeField = new JTextField();

        JLabel statusLabel = new JLabel("Status:");
        String[] statusOptions;
        if (isLastAction) {
            statusOptions = new String[]{"IN-PROGRESS", "SUBMITTED", "FAILED"};
        } else {
            statusOptions = new String[]{"IN-PROGRESS", "FAILED"};
        }
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);

        dialogPanel.add(studentLabel);
        dialogPanel.add(studentValue);
        dialogPanel.add(courseLabel);
        dialogPanel.add(courseValue);
        dialogPanel.add(actionLabel);
        dialogPanel.add(actionValue);
        dialogPanel.add(gradeLabel);
        dialogPanel.add(gradeField);
        dialogPanel.add(statusLabel);
        dialogPanel.add(statusComboBox);

        if (!isLastAction) {
            JLabel warningLabel = new JLabel("⚠️ SUBMITTED not available");
            JLabel warningMsg = new JLabel("<html>Only last action can be SUBMITTED</html>");
            warningLabel.setForeground(new Color(231, 76, 60));
            warningMsg.setForeground(new Color(231, 76, 60));
            dialogPanel.add(warningLabel);
            dialogPanel.add(warningMsg);
        }

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Grade Recovery Task",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String gradeText = gradeField.getText().trim();
                Integer grade = null;
                if (!gradeText.isEmpty()) {
                    grade = Integer.parseInt(gradeText);
                    if (grade < 0 || grade > 100) {
                        JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.", "Invalid Grade", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                }

                String selectedStatus = (String) statusComboBox.getSelectedItem();

                // Find and update the enrollment
                dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
                List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

                models.RecoveryCourseEnrollment targetEnrollment = null;
                for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                    if (enrollment.getActionNumber() == actionNumber) {
                        targetEnrollment = enrollment;
                        break;
                    }
                }

                if (targetEnrollment == null) {
                    JOptionPane.showMessageDialog(this, "Enrollment not found.", "Error", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                // Update grade and status
                if (grade != null) {
                    targetEnrollment.setGrade(grade);
                }
                targetEnrollment.setStatus(models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus.fromString(selectedStatus));

                // Add note about grading
                String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
                String gradeNote = "[Graded by instructor on " + timestamp + "] Status: " + selectedStatus;
                if (grade != null) {
                    gradeNote += ", Grade: " + grade;
                }

                String existingNotes = targetEnrollment.getNotes();
                String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
                                    ? existingNotes + "\n" + gradeNote
                                    : gradeNote;
                targetEnrollment.setNotes(updatedNotes);

                boolean success = enrollmentDAO.updateEnrollment(targetEnrollment);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Task graded successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                    tableModel.setRowCount(0);
                    loadRecoveryData(tableModel);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to grade task.", "Error", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for grade.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Shows dialog to add notes to a recovery task.
     */
    private void showNotesDialog(String studentId, String courseId, int actionNumber, DefaultTableModel tableModel) {
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.add(new JLabel("Student ID:"));
        infoPanel.add(new JLabel(studentId));
        infoPanel.add(new JLabel("Course ID:"));
        infoPanel.add(new JLabel(courseId));
        infoPanel.add(new JLabel("Action Number:"));
        infoPanel.add(new JLabel(String.valueOf(actionNumber)));

        // Load existing notes
        String existingNotes = "";
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        java.util.List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);
        for (models.RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == actionNumber) {
                existingNotes = enrollment.getNotes() != null ? enrollment.getNotes() : "";
                break;
            }
        }

        JLabel notesLabel = new JLabel("Notes (existing notes shown below):");
        JTextArea notesArea = new JTextArea(8, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setText(existingNotes);
        notesArea.setCaretPosition(notesArea.getText().length()); // Move cursor to end
        JScrollPane notesScroll = new JScrollPane(notesArea);

        dialogPanel.add(infoPanel, BorderLayout.NORTH);
        dialogPanel.add(notesLabel, BorderLayout.CENTER);
        dialogPanel.add(notesScroll, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add Notes to Recovery Task",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String notes = notesArea.getText().trim();
            if (notes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter some notes.", "Empty Notes", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // Create instructor object and add notes
            models.Instructor instructor = new models.Instructor(userId, "", "", "");
            instructor.setAssignedCourseIds(new ArrayList<>());
            for (Course c : instructorCourses) {
                instructor.assignCourse(c.getCourseId());
            }

            boolean success = instructor.updateRecoveryNotes(studentId, courseId, actionNumber, notes);

            if (success) {
                JOptionPane.showMessageDialog(this, "Notes added successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                tableModel.setRowCount(0);
                loadRecoveryData(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add notes.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Creates the grading panel.
     */
    private JPanel createFailedComponentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title and filter panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Student Grading - Course Results Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Filter panel with course dropdown and status dropdown
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        // Course filter
        JLabel courseFilterLabel = new JLabel("Filter by Course:");
        courseFilterLabel.setFont(new Font("Arial", Font.BOLD, 14));

        List<String> courseOptions = new ArrayList<>();
        courseOptions.add("All Courses");
        for (Course course : instructorCourses) {
            courseOptions.add(course.getCourseId() + " - " + course.getCourseName());
        }

        JComboBox<String> courseFilter = new JComboBox<>(courseOptions.toArray(new String[0]));
        courseFilter.setPreferredSize(new Dimension(300, 35));
        courseFilter.setFont(new Font("Arial", Font.PLAIN, 13));

        // Status filter dropdown
        JLabel statusFilterLabel = new JLabel("Filter by Status:");
        statusFilterLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] statusOptions = {"All Students", "PASSED", "FAILED", "TRANSIT", "INCOMPLETE"};
        JComboBox<String> statusFilter = new JComboBox<>(statusOptions);
        statusFilter.setPreferredSize(new Dimension(180, 35));
        statusFilter.setFont(new Font("Arial", Font.PLAIN, 13));

        // Add components to filter panel in a single row
        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(courseFilterLabel, gbc);

        gbc.gridx = 1;
        filterPanel.add(courseFilter, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(5, 30, 5, 5); // Extra left margin for spacing
        filterPanel.add(statusFilterLabel, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        filterPanel.add(statusFilter, gbc);

        // Add horizontal glue to push everything to the left
        gbc.gridx = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        filterPanel.add(Box.createHorizontalGlue(), gbc);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table showing student results with status
        String[] columnNames = {"Student ID", "Student Name", "Course ID", "Grade", "Status", "Failed Components"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Load all student data initially
        loadFailedComponentsData(tableModel, null, "All Students");

        JTable table = new JTable(tableModel) {
            @Override
            public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                java.awt.Component c = super.prepareRenderer(renderer, row, column);

                // Color-code rows based on status for better UX
                if (!isRowSelected(row)) {
                    String status = (String) getValueAt(row, 4); // Status column
                    if (status != null) {
                        if (status.equals("FAILED")) {
                            c.setBackground(new Color(255, 230, 230)); // Light red for failed
                        } else if (status.equals("PASSED")) {
                            c.setBackground(new Color(230, 255, 230)); // Light green for passed
                        } else if (status.equals("TRANSIT")) {
                            c.setBackground(new Color(255, 250, 205)); // Light yellow for transit
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(this.getSelectionBackground());
                }
                return c;
            }
        };

        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setPreferredScrollableViewportSize(new Dimension(1100, 400));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add filter listeners that work together
        Runnable applyFilters = () -> {
            String selectedCourse = (String) courseFilter.getSelectedItem();
            String courseIdFilter = null;

            if (selectedCourse != null && !selectedCourse.equals("All Courses")) {
                courseIdFilter = selectedCourse.split(" - ")[0];
            }

            String selectedStatus = (String) statusFilter.getSelectedItem();

            tableModel.setRowCount(0);
            loadFailedComponentsData(tableModel, courseIdFilter, selectedStatus);
        };

        courseFilter.addActionListener(e -> applyFilters.run());
        statusFilter.addActionListener(e -> applyFilters.run());

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = createActionButton("Refresh", new Color(46, 204, 113));
        JButton viewDetailsButton = createActionButton("View Student Details", PRIMARY_COLOR);

        refreshButton.addActionListener(e -> {
            applyFilters.run();
            JOptionPane.showMessageDialog(panel, "Data refreshed successfully!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        viewDetailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentId = (String) tableModel.getValueAt(selectedRow, 0);
                String studentName = (String) tableModel.getValueAt(selectedRow, 1);
                String courseId = (String) tableModel.getValueAt(selectedRow, 2);
                String grade = (String) tableModel.getValueAt(selectedRow, 3);
                String status = (String) tableModel.getValueAt(selectedRow, 4);
                String failedComponents = (String) tableModel.getValueAt(selectedRow, 5);

                // Show custom dialog with student image
                showStudentDetailsDialog(studentId, studentName, courseId, grade, status, failedComponents);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a student to view details.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(viewDetailsButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Shows a custom dialog with student details including profile picture.
     *
     * @param studentId the student ID
     * @param studentName the student name
     * @param courseId the course ID
     * @param grade the grade
     * @param status the status
     * @param failedComponents the failed components
     */
    private void showStudentDetailsDialog(String studentId, String studentName, String courseId,
                                         String grade, String status, String failedComponents) {
        // Create custom dialog with proper sizing
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Student Details", true);
        dialog.setLayout(new BorderLayout(SPACING_MD, SPACING_MD));
        dialog.setSize(500, 550); // Increased height to fit all content
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(SPACING_MD, SPACING_MD));
        mainPanel.setBackground(BACKGROUND_WHITE);
        mainPanel.setBorder(BORDER_EMPTY_LG);

        // Top panel with image and name
        JPanel topPanel = new JPanel(new BorderLayout(SPACING_SM, SPACING_SM));
        topPanel.setBackground(BACKGROUND_WHITE);

        // Load and display user image (cropped to square)
        JLabel imageLabel = createStudentImageLabel();
        topPanel.add(imageLabel, BorderLayout.CENTER);

        // Student name label with better styling
        JLabel nameLabel = new JLabel(studentName);
        nameLabel.setFont(FONT_H3);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setForeground(TEXT_PRIMARY);
        topPanel.add(nameLabel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Details panel with proper layout
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, SPACING_SM, SPACING_MD));
        detailsPanel.setBackground(BACKGROUND_WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BORDER_LINE,
                "Student Information",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                FONT_BODY_BOLD,
                TEXT_PRIMARY
            ),
            BorderFactory.createEmptyBorder(SPACING_MD, SPACING_MD, SPACING_MD, SPACING_MD)
        ));

        // Add detail rows
        addDetailRow(detailsPanel, "Student ID:", studentId);
        addDetailRow(detailsPanel, "Course ID:", courseId);
        addDetailRow(detailsPanel, "Grade:", grade);
        addDetailRow(detailsPanel, "Status:", status);
        addDetailRow(detailsPanel, "Failed Components:", failedComponents);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Close button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, SPACING_MD, SPACING_MD));
        buttonPanel.setBackground(BACKGROUND_WHITE);

        JButton closeButton = createPrimaryButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        // Add keyboard shortcut for ESC key to close
        dialog.getRootPane().registerKeyboardAction(
            e -> dialog.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Creates the student image label with proper styling.
     * @return formatted image label
     */
    private JLabel createStudentImageLabel() {
        try {
            ImageIcon originalIcon = new ImageIcon("data/images/def_user.png");
            Image originalImage = originalIcon.getImage();

            // Get original dimensions
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            // Calculate square crop dimensions (use the smaller dimension)
            int cropSize = Math.min(originalWidth, originalHeight);

            // Calculate crop position (center crop)
            int cropX = (originalWidth - cropSize) / 2;
            int cropY = (originalHeight - cropSize) / 2;

            // Create a buffered image for cropping
            java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage(
                originalWidth, originalHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            // Enable anti-aliasing for better quality
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Crop to square
            java.awt.image.BufferedImage croppedImage = bufferedImage.getSubimage(cropX, cropY, cropSize, cropSize);

            // Scale the cropped square image to display size (120x120 for better visibility)
            Image scaledImage = croppedImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon userIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(userIcon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, BORDER_CARD),
                BorderFactory.createEmptyBorder(SPACING_XS, SPACING_XS, SPACING_XS, SPACING_XS)
            ));

            return imageLabel;
        } catch (Exception ex) {
            System.err.println("Error loading user image: " + ex.getMessage());

            // Create placeholder with better styling
            JLabel placeholderLabel = new JLabel("👤", JLabel.CENTER);
            placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 60));
            placeholderLabel.setPreferredSize(new Dimension(120, 120));
            placeholderLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_MEDIUM, BORDER_THICK),
                BorderFactory.createEmptyBorder(SPACING_SM, SPACING_SM, SPACING_SM, SPACING_SM)
            ));
            placeholderLabel.setBackground(BACKGROUND);
            placeholderLabel.setOpaque(true);

            return placeholderLabel;
        }
    }

    /**
     * Helper method to add a detail row to the details panel.
     *
     * @param panel the panel to add to
     * @param label the label text
     * @param value the value text
     */
    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(FONT_BODY_BOLD);
        labelComponent.setForeground(TEXT_SECONDARY);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(FONT_BODY);
        valueComponent.setForeground(TEXT_PRIMARY);

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    /**
     * Loads student results data for all students in the instructor's courses.
     * Displays grades, status, and failed components for each student.
     *
     * @param tableModel the table model to populate
     * @param courseIdFilter optional course ID filter (null for all courses)
     * @param statusFilter status filter ("All Students", "PASSED", "FAILED", "TRANSIT", "INCOMPLETE")
     */
    private void loadFailedComponentsData(DefaultTableModel tableModel, String courseIdFilter, String statusFilter) {
        dao.StudentDAO studentDAO = new dao.StudentDAO();
        dao.ResultDAO resultDAO = new dao.ResultDAO();

        // Track students already added per course (to avoid duplicates)
        java.util.Set<String> addedStudentCourse = new java.util.HashSet<>();

        // Get all results for instructor's courses
        for (Course course : instructorCourses) {
            // Apply course filter if specified
            if (courseIdFilter != null && !course.getCourseId().equals(courseIdFilter)) {
                continue;
            }

            // Load all results for this course
            List<models.Result> allResults = resultDAO.loadResultsByCourse(course.getCourseId());

            for (models.Result result : allResults) {
                String studentId = result.getStudentId();
                String courseId = result.getCourseId();
                String studentCourseKey = studentId + "_" + courseId;

                // Skip if already added (avoid duplicate rows for same student in same course)
                if (addedStudentCourse.contains(studentCourseKey)) {
                    continue;
                }
                addedStudentCourse.add(studentCourseKey);

                // Apply status filter
                if (statusFilter != null && !statusFilter.equals("All Students")) {
                    String resultStatus = result.getStatus() != null ? result.getStatus().toString() : "";
                    if (!resultStatus.equals(statusFilter)) {
                        continue;
                    }
                }

                // Get student details
                models.Student student = studentDAO.loadStudent(studentId);
                String studentName = student != null ? student.getFullName() : "Unknown";

                // Get grade information
                String gradeDisplay = result.getGrade() != null ?
                    result.getGrade() + " (" + result.getGradePoint() + ")" : "N/A";

                // Get status
                String statusDisplay = result.getStatus() != null ? result.getStatus().toString() : "N/A";

                // Build failed components string
                StringBuilder failedComponentsStr = new StringBuilder();

                if (result.getStatus() == enums.GradeStatus.FAILED || result.needsRecovery()) {
                    // Student failed - show which components
                    if (!result.isPassedExam()) {
                        failedComponentsStr.append("EXAM");
                    }

                    if (!result.isPassedAssignment()) {
                        if (failedComponentsStr.length() > 0) {
                            failedComponentsStr.append(", ");
                        }
                        failedComponentsStr.append("ASSIGNMENT");
                    }

                    // If both passed but status is FAILED, show "None"
                    if (failedComponentsStr.length() == 0) {
                        failedComponentsStr.append("None");
                    }
                } else {
                    // Student passed - show "-"
                    failedComponentsStr.append("-");
                }

                // Add row to table with new column order: Student ID, Name, Course ID, Grade, Status, Failed Components
                Object[] rowData = {
                    studentId,
                    studentName,
                    courseId,
                    gradeDisplay,
                    statusDisplay,
                    failedComponentsStr.toString()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Creates an action button with consistent styling and hover effects.
     * @param text button text
     * @param bgColor background color
     * @return styled button
     */
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setMinimumSize(new Dimension(BUTTON_MIN_WIDTH, BUTTON_HEIGHT));
        button.setPreferredSize(new Dimension(BUTTON_MIN_WIDTH + 50, BUTTON_HEIGHT));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(SPACING_SM, SPACING_LG, SPACING_SM, SPACING_LG));

        // Add hover effect
        Color darkerColor = UIConstants.darker(bgColor, 0.15f);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(darkerColor);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                }
            }
        });

        return button;
    }

    /**
     * Creates a primary action button (for main actions).
     */
    private JButton createPrimaryButton(String text) {
        return createActionButton(text, PRIMARY);
    }

    /**
     * Creates a success action button (for positive actions like create, add).
     */
    private JButton createSuccessButton(String text) {
        return createActionButton(text, SUCCESS);
    }

    /**
     * Creates a danger action button (for destructive actions like delete).
     */
    private JButton createDangerButton(String text) {
        return createActionButton(text, DANGER);
    }

    /**
     * Creates a warning action button (for actions needing attention).
     */
    private JButton createWarningButton(String text) {
        return createActionButton(text, WARNING);
    }

    /**
     * Creates a secondary action button (for less important actions).
     */
    private JButton createSecondaryButton(String text) {
        return createActionButton(text, PRIMARY_DARK);
    }

    /**
     * Shows dialog to manage recovery actions directly for a course.
     * This allows instructors to manage actions for any course without needing a template first.
     */
    private void showCourseRecoveryActionsDialog(String courseId, String courseName) {
        JDialog actionsDialog = new JDialog(this, "Recovery Actions - " + courseName, true);
        actionsDialog.setSize(1000, 650);
        actionsDialog.setLocationRelativeTo(this);
        actionsDialog.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Recovery Actions for: " + courseName + " (" + courseId + ")");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        actionsDialog.add(titlePanel, BorderLayout.NORTH);

        // Actions table
        String[] columnNames = {"Action #", "Title", "Description", "Has Grade", "Status"};
        DefaultTableModel actionsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadCourseActionsData(courseId, actionsTableModel);

        JTable actionsTable = new JTable(actionsTableModel);
        actionsTable.setRowHeight(35);
        actionsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        actionsTable.getTableHeader().setBackground(SECONDARY_COLOR);
        actionsTable.getTableHeader().setForeground(Color.WHITE);
        actionsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        actionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(actionsTable);
        actionsDialog.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addActionButton = createActionButton("Add Action", new Color(46, 204, 113));
        JButton editActionButton = createActionButton("Edit Action", PRIMARY_COLOR);
        JButton toggleStatusButton = createActionButton("Toggle Active/Inactive", new Color(243, 156, 18));
        JButton deleteActionButton = createActionButton("Delete Action", new Color(231, 76, 60));
        JButton refreshButton = createActionButton("Refresh", SECONDARY_COLOR);
        JButton closeButton = createActionButton("Close", new Color(149, 165, 166));

        // Add Action button
        addActionButton.addActionListener(e -> {
            showAddCourseActionDialog(courseId, actionsTableModel);
        });

        // Edit Action button
        editActionButton.addActionListener(e -> {
            int selectedRow = actionsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String actionIdFromTable = getCourseActionIdFromTable(courseId, selectedRow, actionsTableModel);
                showEditCourseActionDialog(actionIdFromTable, actionsTableModel, courseId);
            } else {
                JOptionPane.showMessageDialog(actionsDialog, "Please select an action to edit.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Toggle Status button
        toggleStatusButton.addActionListener(e -> {
            int selectedRow = actionsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String actionIdFromTable = getCourseActionIdFromTable(courseId, selectedRow, actionsTableModel);
                toggleActionStatus(actionIdFromTable, actionsTableModel, courseId);
            } else {
                JOptionPane.showMessageDialog(actionsDialog, "Please select an action to toggle status.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Delete Action button
        deleteActionButton.addActionListener(e -> {
            int selectedRow = actionsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String actionIdFromTable = getCourseActionIdFromTable(courseId, selectedRow, actionsTableModel);
                int confirm = JOptionPane.showConfirmDialog(actionsDialog,
                    "Are you sure you want to delete this action?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
                    try {
                        actionDAO.deleteAction(actionIdFromTable);
                        JOptionPane.showMessageDialog(actionsDialog, "Action deleted successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                        actionsTableModel.setRowCount(0);
                        loadCourseActionsData(courseId, actionsTableModel);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(actionsDialog, "Failed to delete action: " + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(actionsDialog, "Please select an action to delete.", "No Selection", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Refresh button
        refreshButton.addActionListener(e -> {
            actionsTableModel.setRowCount(0);
            loadCourseActionsData(courseId, actionsTableModel);
            JOptionPane.showMessageDialog(actionsDialog, "Actions refreshed!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        // Close button
        closeButton.addActionListener(e -> actionsDialog.dispose());

        buttonPanel.add(addActionButton);
        buttonPanel.add(editActionButton);
        buttonPanel.add(toggleStatusButton);
        buttonPanel.add(deleteActionButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        actionsDialog.add(buttonPanel, BorderLayout.SOUTH);

        actionsDialog.setVisible(true);
    }

    /**
     * Loads actions data for a course into the table.
     */
    private void loadCourseActionsData(String courseId, DefaultTableModel tableModel) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<models.RecoveryCourseAction> actions = actionDAO.loadActionsByCourse(courseId);

        for (models.RecoveryCourseAction action : actions) {
            String description = action.getDescription();
            if (description != null && description.length() > 50) {
                description = description.substring(0, 50) + "...";
            }

            Object[] rowData = {
                action.getActionNumber(),
                action.getTitle(),
                description,
                action.isHasGrade() ? "Yes" : "No",
                action.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Gets the action ID from a table row for a specific course.
     */
    private String getCourseActionIdFromTable(String courseId, int rowIndex, DefaultTableModel tableModel) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<models.RecoveryCourseAction> actions = actionDAO.loadActionsByCourse(courseId);

        if (rowIndex >= 0 && rowIndex < actions.size()) {
            return actions.get(rowIndex).getId();
        }
        return null;
    }

    /**
     * Shows dialog to add a new action to a course.
     */
    private void showAddCourseActionDialog(String courseId, DefaultTableModel actionsTableModel) {
        JPanel dialogPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel numberLabel = new JLabel("Action Number:");
        JTextField numberField = new JTextField();

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();

        JLabel descLabel = new JLabel("Description:");
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        JLabel gradeLabel = new JLabel("Has Grade:");
        JCheckBox gradeCheckBox = new JCheckBox();

        JLabel activeLabel = new JLabel("Is Active:");
        JCheckBox activeCheckBox = new JCheckBox();
        activeCheckBox.setSelected(true); // Default to active

        dialogPanel.add(numberLabel);
        dialogPanel.add(numberField);
        dialogPanel.add(titleLabel);
        dialogPanel.add(titleField);
        dialogPanel.add(descLabel);
        dialogPanel.add(descScroll);
        dialogPanel.add(gradeLabel);
        dialogPanel.add(gradeCheckBox);
        dialogPanel.add(activeLabel);
        dialogPanel.add(activeCheckBox);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add Action to Course",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int actionNumber = Integer.parseInt(numberField.getText().trim());
                String title = titleField.getText().trim();
                String description = descArea.getText().trim();
                boolean hasGrade = gradeCheckBox.isSelected();
                boolean isActive = activeCheckBox.isSelected();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a title.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                // Validation: Check if action number already exists as ACTIVE
                if (isActive) {
                    dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
                    List<models.RecoveryCourseAction> existingActions = actionDAO.loadActionsByCourse(courseId);

                    for (models.RecoveryCourseAction existingAction : existingActions) {
                        if (existingAction.getActionNumber() == actionNumber && existingAction.isActive()) {
                            JOptionPane.showMessageDialog(this,
                                "Action number " + actionNumber + " already exists as ACTIVE for this course.\n" +
                                "Action numbers cannot repeat unless the existing action is inactive.\n" +
                                "Please choose a different number or set this action as inactive.",
                                "Duplicate Action Number",
                                JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                    }
                }

                // Generate unique ID
                String actionId = String.valueOf(System.currentTimeMillis());

                // Create action
                models.RecoveryCourseAction action = new models.RecoveryCourseAction(
                    actionId, courseId, userId, actionNumber, title, description, isActive, hasGrade
                );

                dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
                actionDAO.saveAction(action);

                JOptionPane.showMessageDialog(this, "Action added successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
                actionsTableModel.setRowCount(0);
                loadCourseActionsData(courseId, actionsTableModel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Action Number.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Shows dialog to edit an existing course action.
     */
    private void showEditCourseActionDialog(String actionId, DefaultTableModel actionsTableModel, String courseId) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        models.RecoveryCourseAction action = actionDAO.loadAction(actionId);

        if (action == null) {
            JOptionPane.showMessageDialog(this, "Action not found.", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        JPanel dialogPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(action.getTitle());

        JLabel descLabel = new JLabel("Description:");
        JTextArea descArea = new JTextArea(action.getDescription(), 3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        JLabel gradeLabel = new JLabel("Has Grade:");
        JCheckBox gradeCheckBox = new JCheckBox();
        gradeCheckBox.setSelected(action.isHasGrade());

        dialogPanel.add(titleLabel);
        dialogPanel.add(titleField);
        dialogPanel.add(descLabel);
        dialogPanel.add(descScroll);
        dialogPanel.add(gradeLabel);
        dialogPanel.add(gradeCheckBox);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Edit Action",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newTitle = titleField.getText().trim();
            String newDescription = descArea.getText().trim();
            boolean newHasGrade = gradeCheckBox.isSelected();

            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            action.setTitle(newTitle);
            action.setDescription(newDescription);
            action.setHasGrade(newHasGrade);

            actionDAO.updateAction(action);
            JOptionPane.showMessageDialog(this, "Action updated successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
            actionsTableModel.setRowCount(0);
            loadCourseActionsData(courseId, actionsTableModel);
        }
    }

    /**
     * Shows dialog to switch action for a student's recovery enrollment.
     * Allows instructor to manually change which action a student is working on.
     */
    private void showSwitchActionDialog(String studentId, String courseId, int currentActionNumber, DefaultTableModel tableModel) {
        // Load all ACTIVE actions for this course
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<models.RecoveryCourseAction> allActions = actionDAO.loadActionsByCourse(courseId);

        // Filter to only active actions
        List<models.RecoveryCourseAction> activeActions = new ArrayList<>();
        for (models.RecoveryCourseAction action : allActions) {
            if (action.isActive()) {
                activeActions.add(action);
            }
        }

        if (activeActions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No active actions available for this course.\nPlease create and activate recovery actions first.",
                "No Active Actions",
                JOptionPane.PLAIN_MESSAGE);
            return;
        }

        // Sort by action number
        activeActions.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.add(new JLabel("Student ID:"));
        infoPanel.add(new JLabel(studentId));
        infoPanel.add(new JLabel("Course ID:"));
        infoPanel.add(new JLabel(courseId));
        infoPanel.add(new JLabel("Current Action #:"));
        infoPanel.add(new JLabel(String.valueOf(currentActionNumber)));

        // Action selection
        JLabel selectLabel = new JLabel("Select New Action:");
        String[] actionOptions = new String[activeActions.size()];
        int currentActionIndex = -1;
        for (int i = 0; i < activeActions.size(); i++) {
            models.RecoveryCourseAction action = activeActions.get(i);
            String label = "Action #" + action.getActionNumber() + ": " + action.getTitle();

            // Mark the current action in the dropdown
            if (action.getActionNumber() == currentActionNumber) {
                label += " (CURRENT)";
                currentActionIndex = i;
            }

            actionOptions[i] = label;
        }
        JComboBox<String> actionComboBox = new JComboBox<>(actionOptions);

        // Preselect the current action if found
        if (currentActionIndex >= 0) {
            actionComboBox.setSelectedIndex(currentActionIndex);
        }

        JPanel selectionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        selectionPanel.add(selectLabel);
        selectionPanel.add(actionComboBox);

        dialogPanel.add(infoPanel, BorderLayout.NORTH);
        dialogPanel.add(selectionPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Switch Student Action",
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = actionComboBox.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= activeActions.size()) {
                JOptionPane.showMessageDialog(this, "Invalid action selection.", "Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            models.RecoveryCourseAction newAction = activeActions.get(selectedIndex);
            int newActionNumber = newAction.getActionNumber();

            if (newActionNumber == currentActionNumber) {
                JOptionPane.showMessageDialog(this, "Selected action is the same as current action.", "No Change", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // Update the enrollment
            dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
            List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

            models.RecoveryCourseEnrollment targetEnrollment = null;
            for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                if (enrollment.getActionNumber() == currentActionNumber) {
                    targetEnrollment = enrollment;
                    break;
                }
            }

            if (targetEnrollment == null) {
                JOptionPane.showMessageDialog(this, "Enrollment not found.", "Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // Update enrollment with new action details
            targetEnrollment.setActionNumber(newActionNumber);
            targetEnrollment.setTitle(newAction.getTitle());
            targetEnrollment.setDescription(newAction.getDescription());

            // Add note about the switch
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
            String switchNote = "[Action switched by instructor on " + timestamp + "] Changed from Action #" +
                                currentActionNumber + " to Action #" + newActionNumber;

            String existingNotes = targetEnrollment.getNotes();
            String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
                                ? existingNotes + "\n" + switchNote
                                : switchNote;
            targetEnrollment.setNotes(updatedNotes);

            boolean success = enrollmentDAO.updateEnrollment(targetEnrollment);

            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Action switched successfully!\nStudent is now on Action #" + newActionNumber,
                    "Success",
                    JOptionPane.PLAIN_MESSAGE);
                tableModel.setRowCount(0);
                loadRecoveryData(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to switch action.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Toggles the active status of an action.
     * Validates that action number doesn't conflict with other active actions.
     */
    private void toggleActionStatus(String actionId, DefaultTableModel actionsTableModel, String courseId) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        models.RecoveryCourseAction action = actionDAO.loadAction(actionId);

        if (action == null) {
            JOptionPane.showMessageDialog(this, "Action not found.", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        // If we're activating (current is inactive, will become active)
        if (!action.isActive()) {
            // Check if action number already exists as ACTIVE
            List<models.RecoveryCourseAction> existingActions = actionDAO.loadActionsByCourse(courseId);

            for (models.RecoveryCourseAction existingAction : existingActions) {
                // Skip the current action being toggled
                if (existingAction.getId().equals(actionId)) {
                    continue;
                }

                if (existingAction.getActionNumber() == action.getActionNumber() && existingAction.isActive()) {
                    JOptionPane.showMessageDialog(this,
                        "Cannot activate this action!\n" +
                        "Action number " + action.getActionNumber() + " already exists as ACTIVE for this course.\n" +
                        "Action numbers cannot repeat while both are active.\n" +
                        "Please deactivate the other action with this number first, or change the action number.",
                        "Duplicate Action Number Conflict",
                        JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            }
        }

        // Toggle status
        action.setActive(!action.isActive());
        actionDAO.updateAction(action);

        String statusMessage = action.isActive() ? "activated" : "deactivated";
        JOptionPane.showMessageDialog(this, "Action " + statusMessage + " successfully!", "Success", JOptionPane.PLAIN_MESSAGE);

        actionsTableModel.setRowCount(0);
        loadCourseActionsData(courseId, actionsTableModel);
    }

    /**
     * Logs out the instructor and returns to login screen.
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
     * Main method for testing the instructor dashboard.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InstructorDashboard("Dr. Smith", "I001"));
    }
}