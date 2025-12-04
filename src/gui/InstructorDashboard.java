package gui;

import dao.CourseDAO;
import static gui.UIConstants.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import models.Course;


public class InstructorDashboard extends JFrame {

    private static Color PRIMARY_COLOR = PRIMARY;
    private static Color SECONDARY_COLOR = PRIMARY_DARK;
    private static Color BACKGROUND_COLOR = BACKGROUND;
    private static Color TEXT_COLOR = TEXT_PRIMARY;

    private JLabel welcomeLabel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private String userId;
    private List<Course> instructorCourses;

    // Grading panel components (for filter functionality)
    private JComboBox<String> gradingCourseFilter;
    private JComboBox<String> gradingStatusFilter;
    private DefaultTableModel gradingTableModel;


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


    private void initializeFrame() {
        setTitle("CRS - Academic Officer Dashboard");
        setMinimumSize(WINDOW_MIN_SIZE);
        setSize(WINDOW_MIN_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Initializes GUI
     */
    private void initializeComponents(String instructorName) {
        setLayout(new BorderLayout());

        // Layout on InstructorDashboard is Header, Sidebar, Main Content

        JPanel headerPanel = createHeaderPanel(instructorName);
        add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BACKGROUND_COLOR);

        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createCoursesPanel(), "Courses");
        mainContentPanel.add(createRecoveryPlansPanel(), "RecoveryPlans");
        mainContentPanel.add(createFailedComponentsPanel(), "Grading");
        mainContentPanel.add(createEligibilityCheckPanel(), "EligibilityCheck");
        mainContentPanel.add(createReportsPanel(), "Reports");
        add(mainContentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the header panel with title and user info.
     */
    private JPanel createHeaderPanel(String instructorName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(PRIMARY_COLOR);
        welcomeLabel = new JLabel(instructorName);
        welcomeLabel.setFont(FONT_BODY);
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel);
        panel.add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Academic Officer Dashboard");
        titleLabel.setFont(FONT_H1);
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(PRIMARY_COLOR);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFont(FONT_BUTTON);
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(true);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        rightPanel.add(logoutButton);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

   
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));

        // Add top spacing
        // panel.add(Box.createVerticalStrut(SPACING_LG));

        // Navigation Buttons with keyboard shortcuts
        String[] menuItems = {"Dashboard", "My Courses", "Recovery Plans", "Grading", "Eligibility Check","Reports"};
        String[] cardNames = {"Dashboard", "Courses", "RecoveryPlans", "Grading", "EligibilityCheck","Reports"};
        int[] mnemonics = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5,KeyEvent.VK_6};

        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createSidebarButton(menuItems[i], cardNames[i], mnemonics[i]);
            panel.add(button);
            panel.add(Box.createVerticalStrut(SPACING_SM));
        }
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createSidebarButton(String text, String cardName, int mnemonic) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(SIDEBAR_WIDTH, SIDEBAR_BUTTON_HEIGHT));
        button.setPreferredSize(new Dimension(SIDEBAR_WIDTH, SIDEBAR_BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(SIDEBAR_WIDTH, SIDEBAR_BUTTON_HEIGHT));
        button.setBackground(SIDEBAR_BG);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BODY_BOLD);
        // button.setFocusPainted(false);
        // button.setBorderPainted(false);
        // button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, SPACING_LG, 0, 0));

        
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

  
    private void updateActiveButton(JButton activeButton) {
        // Set sidebar button to initilzia state
        Component[] components = ((JPanel) activeButton.getParent()).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(SIDEBAR_BG);
            }
        }
        
        activeButton.setBackground(SIDEBAR_ACTIVE);
    }

    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        // panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        //  Statistics for Academic Officer
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

        // Courses List
        JPanel coursesPanel = new JPanel(new BorderLayout(10, 10));
        coursesPanel.setBackground(BACKGROUND_COLOR);

        JLabel coursesLabel = new JLabel("My Courses");
        coursesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        coursesLabel.setForeground(TEXT_COLOR);
        coursesPanel.add(coursesLabel, BorderLayout.NORTH);

        JPanel coursesListPanel = new JPanel();
        coursesListPanel.setLayout(new BoxLayout(coursesListPanel, BoxLayout.Y_AXIS));
        coursesListPanel.setBackground(Color.WHITE);
        coursesListPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        for (Course course : instructorCourses) {
            JPanel courseItem = createCourseListItem(course);
            coursesListPanel.add(courseItem);
            coursesListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Additional feature: scrollable courses list
        JScrollPane coursesScrollPane = new JScrollPane(coursesListPanel);
        coursesScrollPane.setPreferredSize(new Dimension(700, 300));
        coursesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        coursesScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        coursesPanel.add(coursesScrollPane, BorderLayout.CENTER);

        mainContent.add(coursesPanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCourseListItem(Course course) {
        JPanel item = new JPanel(new BorderLayout(10, 5));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Course ID and Name
        JLabel courseLabel = new JLabel(course.getCourseId() + " - " + course.getCourseName());
        courseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        courseLabel.setForeground(TEXT_COLOR);

        // Course details (Include: credits, capacity)
        JLabel detailsLabel = new JLabel("Credits: " + course.getCredits() + " | Capacity: " + course.getCapacity());
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsLabel.setForeground(new Color(100, 100, 100));

        JPanel textPanel = new JPanel(new BorderLayout(5, 2));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(courseLabel, BorderLayout.NORTH);
        textPanel.add(detailsLabel, BorderLayout.CENTER);

        item.add(textPanel, BorderLayout.CENTER);

        // Additional feature: Add hover effect
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

   
    private int getStudentsInRecoveryCount() {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        List<String> uniqueStudents = new ArrayList<>();

        for (Course course : instructorCourses) {
            List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByCourse(course.getCourseId());
            for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                String studentId = enrollment.getStudentId();
                if (!uniqueStudents.contains(studentId)) {
                    uniqueStudents.add(studentId);
                }
            }
        }

        return uniqueStudents.size();
    }

    private int getTotalStudentsCount() {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        List<String> uniqueStudents = new ArrayList<>();

        for (Course course : instructorCourses) {
            List<models.Result> results = resultDAO.loadResultsByCourse(course.getCourseId());
            for (models.Result result : results) {
                String studentId = result.getStudentId();
                if (!uniqueStudents.contains(studentId)) {
                    uniqueStudents.add(studentId);
                }
            }
        }

        return uniqueStudents.size();
    }

    private int getFailedStudentsCount() {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        List<String> failedStudents = new ArrayList<>();

        for (Course course : instructorCourses) {
            List<models.Result> results = resultDAO.loadResultsByCourse(course.getCourseId());
            for (models.Result result : results) {
                if (result.getStatus() == enums.GradeStatus.FAILED) {
                    String key = result.getStudentId() + "_" + result.getCourseId();
                    if (!failedStudents.contains(key)) {
                        failedStudents.add(key);
                    }
                }
            }
        }

        return failedStudents.size();
    }

// Statistics Card
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


    // Courses Panel (second button)

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
       
        JLabel titleLabel = new JLabel("My Courses (" + instructorCourses.size() + " total)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Course ID", "Course Name", "Credits", "Capacity", "Instructor ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton recoveryActionsButton = createActionButton("Recovery Actions", PRIMARY_COLOR);
        JButton refreshButton = createActionButton("Refresh", SECONDARY_COLOR);

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

    private JPanel createRecoveryPlansPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);

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

        // Switch Actions button (to switch action for recovery of the student)
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
                        "Cannot switch action for a submitted task.",
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


                    // Update Student Recovery Enrollment Status

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
            tableModel.setRowCount(0); // Clear
            loadRecoveryData(tableModel); // Reload
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

    private void loadRecoveryData(DefaultTableModel tableModel) {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        dao.ResultDAO resultDAO = new dao.ResultDAO();

        int invalidEnrollmentsCount = 0;
        StringBuilder invalidEnrollments = new StringBuilder();

        for (Course course : instructorCourses) {
            List<models.RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByCourse(course.getCourseId());

            List<models.RecoveryCourseAction> courseActions = actionDAO.loadActionsByCourse(course.getCourseId());
            List<models.RecoveryCourseAction> activeActions = new ArrayList<>();
            for (models.RecoveryCourseAction action : courseActions) {
                if (action.isActive()) {
                    activeActions.add(action);
                }
            }

            for (models.RecoveryCourseEnrollment enrollment : enrollments) {
                String validationWarning = "";

                models.RecoveryCourseAction correspondingAction = null;
                for (models.RecoveryCourseAction action : activeActions) {
                    if (action.getActionNumber() == enrollment.getActionNumber()) {
                        correspondingAction = action;
                        break;
                    }
                }
                if (correspondingAction == null) {
                    validationWarning = "INVALID: Action not found or inactive";
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
                    passedExamDisplay = studentResult.isPassedExam() ? "✓" : "✗";
                    passedAssignmentDisplay = studentResult.isPassedAssignment() ? "✓" : "✗";
                }

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

       if (invalidEnrollmentsCount > 0) {
            String message = "Invalid enrollments found:\n\n"
                        + invalidEnrollments
                        + "\nPlease fix them using 'Switch Action'.";

            JOptionPane.showMessageDialog(this, message);
        }
    }


    private void showGradeDialog(String studentId, String courseId, int actionNumber, DefaultTableModel tableModel) {
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
            JLabel warningLabel = new JLabel("SUBMITTED not available");
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

    private JPanel createFailedComponentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Student Grading - Course Results Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(BACKGROUND_COLOR);

        JLabel courseFilterLabel = new JLabel("Filter by Course:");
        courseFilterLabel.setFont(new Font("Arial", Font.BOLD, 14));

        List<String> courseOptions = new ArrayList<>();
        courseOptions.add("All Courses");
        for (Course course : instructorCourses) {
            courseOptions.add(course.getCourseId() + " - " + course.getCourseName());
        }

        gradingCourseFilter = new JComboBox<>(courseOptions.toArray(new String[0]));
        gradingCourseFilter.setPreferredSize(new Dimension(300, 35));
        gradingCourseFilter.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel statusFilterLabel = new JLabel("Filter by Status:");
        statusFilterLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] statusOptions = {"All Students", "PASSED", "FAILED", "TRANSIT", "INCOMPLETE"};
        gradingStatusFilter = new JComboBox<>(statusOptions);
        gradingStatusFilter.setPreferredSize(new Dimension(180, 35));
        gradingStatusFilter.setFont(new Font("Arial", Font.PLAIN, 13));

        filterPanel.add(courseFilterLabel);
        filterPanel.add(gradingCourseFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(statusFilterLabel);
        filterPanel.add(gradingStatusFilter);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table showing student results
        String[] columnNames = {"Student ID", "Student Name", "Course ID", "Grade", "Status", "Failed Components"};
        gradingTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Load all student data initially
        loadFailedComponentsData(gradingTableModel, null, "All Students");

        JTable table = new JTable(gradingTableModel) {
            @Override
            public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                java.awt.Component c = super.prepareRenderer(renderer, row, column);

                // Color-code rows based on status (UI good practice)
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

        // Add filter action listeners
        gradingCourseFilter.addActionListener(e -> applyGradingFilters());
        gradingStatusFilter.addActionListener(e -> applyGradingFilters());

        

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = createActionButton("Refresh", new Color(46, 204, 113));
        JButton viewDetailsButton = createActionButton("View Student Details", PRIMARY_COLOR);

        refreshButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "Data refreshed successfully!", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        viewDetailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String studentId = (String) gradingTableModel.getValueAt(selectedRow, 0);
                String studentName = (String) gradingTableModel.getValueAt(selectedRow, 1);
                String courseId = (String) gradingTableModel.getValueAt(selectedRow, 2);
                String grade = (String) gradingTableModel.getValueAt(selectedRow, 3);
                String status = (String) gradingTableModel.getValueAt(selectedRow, 4);
                String failedComponents = (String) gradingTableModel.getValueAt(selectedRow, 5);

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
     * Applies filters to the grading table based on selected course and status.
     */
    private void applyGradingFilters() {
        String selectedCourse = (String) gradingCourseFilter.getSelectedItem();
        String courseIdFilter = null;

        if (selectedCourse != null && !selectedCourse.equals("All Courses")) {
            courseIdFilter = selectedCourse.split(" - ")[0];
        }

        String selectedStatus = (String) gradingStatusFilter.getSelectedItem();
        gradingTableModel.setRowCount(0);
        loadFailedComponentsData(gradingTableModel, courseIdFilter, selectedStatus);
    }

    private void showStudentDetailsDialog(String studentId, String studentName, String courseId,
                                         String grade, String status, String failedComponents) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Student Details", true);
        dialog.setLayout(new BorderLayout(SPACING_MD, SPACING_MD));
        dialog.setSize(500, 550); // Increased height to fit all content
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(SPACING_MD, SPACING_MD));
        mainPanel.setBackground(BACKGROUND_WHITE);
        mainPanel.setBorder(BORDER_EMPTY_LG);

        JPanel topPanel = new JPanel(new BorderLayout(SPACING_SM, SPACING_SM));
        topPanel.setBackground(BACKGROUND_WHITE);
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
        // detailsPanel.setBorder(BorderFactory.createCompoundBorder(
        //     BorderFactory.createTitledBorder(
        //         BORDER_LINE,
        //         "Student Information",
        //         javax.swing.border.TitledBorder.LEFT,
        //         javax.swing.border.TitledBorder.TOP,
        //         FONT_BODY_BOLD,
        //         TEXT_PRIMARY
        //     ),
        //     BorderFactory.createEmptyBorder(SPACING_MD, SPACING_MD, SPACING_MD, SPACING_MD)
        // ));

        addDetailRow(detailsPanel, "Student ID:", studentId);
        addDetailRow(detailsPanel, "Course ID:", courseId);
        addDetailRow(detailsPanel, "Grade:", grade);
        addDetailRow(detailsPanel, "Status:", status);
        addDetailRow(detailsPanel, "Failed Components:", failedComponents);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, SPACING_MD, SPACING_MD));
        buttonPanel.setBackground(BACKGROUND_WHITE);

        JButton closeButton = createPrimaryButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

       

        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JLabel createStudentImageLabel() {
        try {
            String imageUrl = "https://www.w3schools.com/howto/img_avatar.png"; // example avatar
            ImageIcon userIcon = new ImageIcon(new java.net.URL(imageUrl));
            Image scaledImage = userIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            userIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(userIcon, JLabel.CENTER);
            imageLabel.setPreferredSize(new Dimension(120, 120));
            return imageLabel;

        } catch (Exception ex) {
            JLabel blackSquare = new JLabel();
            blackSquare.setPreferredSize(new Dimension(120, 120));
            blackSquare.setOpaque(true);
            blackSquare.setBackground(Color.BLACK);
            return blackSquare;
        }
    }
    
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

    
    private void loadFailedComponentsData(DefaultTableModel tableModel, String courseIdFilter, String statusFilter) {
        dao.StudentDAO studentDAO = new dao.StudentDAO();
        dao.ResultDAO resultDAO = new dao.ResultDAO();

        // Simple list to remember which student+course rows we've already added
        List<String> addedKeys = new ArrayList<>();

        for (Course course : instructorCourses) {
            String courseId = course.getCourseId();

            if (courseIdFilter != null && !courseId.equals(courseIdFilter)) {
                continue;
            }

            List<models.Result> results = resultDAO.loadResultsByCourse(courseId);
            if (results == null || results.isEmpty()) {
                continue;
            }

            for (models.Result result : results) {
                String studentId = result.getStudentId();
                String key = studentId + "_" + courseId;

                if (addedKeys.contains(key)) {
                    continue;
                }
                addedKeys.add(key);

                if (statusFilter != null && !statusFilter.equals("All Students")) {
                    String resultStatus = result.getStatus() != null ? result.getStatus().toString() : "";
                    if (!resultStatus.equals(statusFilter)) {
                        continue;
                    }
                }

                models.Student student = studentDAO.loadStudent(studentId);
                String studentName = (student != null) ? student.getFullName() : "Unknown";

                // Grade display
                String gradeDisplay = (result.getGrade() != null)
                        ? result.getGrade() + " (" + result.getGradePoint() + ")"
                        : "N/A";

                String statusDisplay = (result.getStatus() != null) ? result.getStatus().toString() : "N/A";
                String failedComponents = buildFailedComponentsString(result);
                Object[] rowData = {
                    studentId,
                    studentName,
                    courseId,
                    gradeDisplay,
                    statusDisplay,
                    failedComponents
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private String buildFailedComponentsString(models.Result result) {
        if (result == null) return "-";

        boolean isFailedStatus = result.getStatus() == enums.GradeStatus.FAILED;
        boolean needsRecovery = result.needsRecovery();

        if (isFailedStatus || needsRecovery) {
            String parts = "";

            if (!result.isPassedExam()) {
                parts = "EXAM";
            }

            if (!result.isPassedAssignment()) {
                if (!parts.isEmpty()) {
                    parts = parts + ", ASSIGNMENT";
                } else {
                    parts = "ASSIGNMENT";
                }
            }

            if (parts.isEmpty()) {
                return "None";
            }
            return parts;
        }

        return "-";
    }

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


    private JButton createPrimaryButton(String text) {
        return createActionButton(text, PRIMARY);
    }

    private JButton createSuccessButton(String text) {
        return createActionButton(text, new Color(46, 204, 113));
    }
    private void showCourseRecoveryActionsDialog(String courseId, String courseName) {
        JDialog actionsDialog = new JDialog(this, "Recovery Actions - " + courseName, true);
        actionsDialog.setSize(1000, 650);
        actionsDialog.setLocationRelativeTo(this);
        actionsDialog.setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Recovery Actions for: " + courseName + " (" + courseId + ")");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        actionsDialog.add(titlePanel, BorderLayout.NORTH);

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
        // Additional feature: Scrolling 
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

        addActionButton.addActionListener(e -> {
            showAddCourseActionDialog(courseId, actionsTableModel);
        });

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

   
    private String getCourseActionIdFromTable(String courseId, int rowIndex, DefaultTableModel tableModel) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<models.RecoveryCourseAction> actions = actionDAO.loadActionsByCourse(courseId);

        if (rowIndex >= 0 && rowIndex < actions.size()) {
            return actions.get(rowIndex).getId();
        }
        return null;
    }


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

                String actionId = String.valueOf(System.currentTimeMillis());

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

    
    private void showSwitchActionDialog(String studentId, String courseId, int currentActionNumber, DefaultTableModel tableModel) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<models.RecoveryCourseAction> allActions = actionDAO.loadActionsByCourse(courseId);

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

        activeActions.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.add(new JLabel("Student ID:"));
        infoPanel.add(new JLabel(studentId));
        infoPanel.add(new JLabel("Course ID:"));
        infoPanel.add(new JLabel(courseId));
        infoPanel.add(new JLabel("Current Action #:"));
        infoPanel.add(new JLabel(String.valueOf(currentActionNumber)));

        JLabel selectLabel = new JLabel("Select New Action:");
        String[] actionOptions = new String[activeActions.size()];
        int currentActionIndex = -1;
        for (int i = 0; i < activeActions.size(); i++) {
            models.RecoveryCourseAction action = activeActions.get(i);
            String label = "Action #" + action.getActionNumber() + ": " + action.getTitle();

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

    // Change the action status (active or inactive)
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

    private JPanel createEligibilityCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Eligibility Check & Enrollment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(BACKGROUND_COLOR);

        JLabel eligibilityLabel = new JLabel("Eligibility:");
        JComboBox<String> eligibilityFilterBox = new JComboBox<>();
        eligibilityFilterBox.addItem("All Students");
        eligibilityFilterBox.addItem("Eligible");
        eligibilityFilterBox.addItem("Ineligible");

        JLabel majorLabel = new JLabel("Major:");
        JComboBox<String> majorFilterBox = new JComboBox<>();
        majorFilterBox.addItem("All Majors");

        JLabel yearLabel = new JLabel("Year:");
        JComboBox<String> yearFilterBox = new JComboBox<>();
        yearFilterBox.addItem("All Years");
        yearFilterBox.addItem("1");
        yearFilterBox.addItem("2");
        yearFilterBox.addItem("3");
        yearFilterBox.addItem("4");

        JCheckBox showRecoveryEligibleBox = new JCheckBox("Recovery Eligible Only");
        showRecoveryEligibleBox.setBackground(BACKGROUND_COLOR);
        showRecoveryEligibleBox.setToolTipText("Show only students eligible for recovery (CGPA >= 2.0 and 1-3 failed courses)");

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

        String[] columns = {"Student ID", "Name", "Major", "Year", "Semester", "CGPA", "Failed Courses", "Failed Course Names", "Eligibility"};
        DefaultTableModel eligibilityTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable eligibilityTable = new JTable(eligibilityTableModel);
        eligibilityTable.setFont(FONT_BODY);
        eligibilityTable.setRowHeight(35);
        eligibilityTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        eligibilityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        eligibilityTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        eligibilityTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        eligibilityTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        eligibilityTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        eligibilityTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        eligibilityTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        eligibilityTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        eligibilityTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        eligibilityTable.getColumnModel().getColumn(8).setPreferredWidth(90);

        eligibilityTable.getColumnModel().getColumn(8).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    JLabel label = (JLabel) c;
                    label.setHorizontalAlignment(JLabel.CENTER);

                    if (!isSelected) {
                        String status = value.toString();
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

        // Center-align table column headers and make them bold
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(SECONDARY_COLOR);
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> {
            resetInstructorEligibilityFilters(eligibilityFilterBox, majorFilterBox, yearFilterBox, showRecoveryEligibleBox);
            loadInstructorEligibilityData(eligibilityTableModel, majorFilterBox);
        });

        JButton enrollButton = new JButton("Enroll for Recovery");
        enrollButton.setBackground(new Color(46, 204, 113));
        enrollButton.setForeground(Color.BLACK);
        enrollButton.setFont(new Font("Arial", Font.BOLD, 14));
        enrollButton.setFocusPainted(false);
        enrollButton.addActionListener(e -> {
            enrollSelectedStudentForInstructor(eligibilityTable);
            resetInstructorEligibilityFilters(eligibilityFilterBox, majorFilterBox, yearFilterBox, showRecoveryEligibleBox);
            loadInstructorEligibilityData(eligibilityTableModel, majorFilterBox);
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(enrollButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        eligibilityFilterBox.addActionListener(e -> filterInstructorEligibilityData(
            eligibilityTableModel, eligibilityFilterBox, majorFilterBox, yearFilterBox, showRecoveryEligibleBox));

        majorFilterBox.addActionListener(e -> {
            if (majorFilterBox.getSelectedItem() != null) {
                filterInstructorEligibilityData(eligibilityTableModel, eligibilityFilterBox, majorFilterBox, yearFilterBox, showRecoveryEligibleBox);
            }
        });

        yearFilterBox.addActionListener(e -> {
            if (yearFilterBox.getSelectedItem() != null) {
                filterInstructorEligibilityData(eligibilityTableModel, eligibilityFilterBox, majorFilterBox, yearFilterBox, showRecoveryEligibleBox);
            }
        });

        showRecoveryEligibleBox.addActionListener(e -> filterInstructorEligibilityData(
            eligibilityTableModel, eligibilityFilterBox, majorFilterBox, yearFilterBox, showRecoveryEligibleBox));

        loadInstructorEligibilityData(eligibilityTableModel, majorFilterBox);
        utils.TableUtils.centerAlignTable(eligibilityTable);

        return panel;
    }

    /**
     * Resets all eligibility filters to their default values.
     */
    private void resetInstructorEligibilityFilters(JComboBox<String> eligibilityFilterBox,
            JComboBox<String> majorFilterBox, JComboBox<String> yearFilterBox,
            JCheckBox showRecoveryEligibleBox) {
        eligibilityFilterBox.setSelectedItem("All Students");
        majorFilterBox.setSelectedItem("All Majors");
        yearFilterBox.setSelectedItem("All Years");
        showRecoveryEligibleBox.setSelected(false);
    }

    /**
     * Checks if student has active recovery enrollments for ALL their failed courses.
     * @param studentId the student ID
     * @param failedCourses list of failed courses needing recovery
     * @return true if all failed courses have active enrollments
     */
    private boolean hasActiveRecoveryEnrollmentForAllCourses(
            String studentId, java.util.List<models.Course> failedCourses) {

        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        java.util.List<models.RecoveryCourseEnrollment> studentEnrollments =
            enrollmentDAO.loadEnrollmentsByStudent(studentId);

        java.util.Set<String> enrolledCourseIds = new java.util.HashSet<>();

        for (models.RecoveryCourseEnrollment enrollment : studentEnrollments) {
            if (enrollment.getStatus() == models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS ||
                enrollment.getStatus() == models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED) {
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

    private void loadInstructorEligibilityData(DefaultTableModel tableModel, JComboBox<String> majorFilterBox) {
        tableModel.setRowCount(0);
        majorFilterBox.removeAllItems();
        majorFilterBox.addItem("All Majors");

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        services.EligibilityChecker eligibilityChecker = new services.EligibilityChecker();

        java.util.Set<String> instructorStudentIds = new java.util.HashSet<>();
        for (Course course : instructorCourses) {
            java.util.List<models.Result> courseResults = resultDAO.loadResultsByCourse(course.getCourseId());
            for (models.Result result : courseResults) {
                instructorStudentIds.add(result.getStudentId());
            }
        }

        java.util.List<models.Student> allStudents = studentDAO.loadAllStudents();
        java.util.Set<String> majors = new java.util.HashSet<>();

        for (models.Student student : allStudents) {
            if (!instructorStudentIds.contains(student.getStudentId())) {
                continue;
            }

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
                if (i > 0) failedNames.append(", ");
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

            tableModel.addRow(row);
            majors.add(student.getMajor());
        }

        for (String major : majors) {
            majorFilterBox.addItem(major);
        }
    }

    private void filterInstructorEligibilityData(DefaultTableModel tableModel, JComboBox<String> eligibilityFilterBox,
            JComboBox<String> majorFilterBox, JComboBox<String> yearFilterBox, JCheckBox showRecoveryEligibleBox) {
        String selectedEligibility = (String) eligibilityFilterBox.getSelectedItem();
        String selectedMajor = (String) majorFilterBox.getSelectedItem();
        String selectedYear = (String) yearFilterBox.getSelectedItem();

        if (selectedEligibility == null || selectedMajor == null || selectedYear == null) {
            return;
        }

        dao.StudentDAO studentDAO = new dao.StudentDAO();
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        services.EligibilityChecker eligibilityChecker = new services.EligibilityChecker();

        // Get unique student IDs enrolled in instructor's courses
        java.util.Set<String> instructorStudentIds = new java.util.HashSet<>();
        for (Course course : instructorCourses) {
            java.util.List<models.Result> courseResults = resultDAO.loadResultsByCourse(course.getCourseId());
            for (models.Result result : courseResults) {
                instructorStudentIds.add(result.getStudentId());
            }
        }

        java.util.List<models.Student> allStudents = studentDAO.loadAllStudents();
        tableModel.setRowCount(0);

        for (models.Student student : allStudents) {
            // Only show students enrolled in this instructor's courses
            if (!instructorStudentIds.contains(student.getStudentId())) {
                continue;
            }

            if (!selectedMajor.equals("All Majors") && !student.getMajor().equals(selectedMajor)) {
                continue;
            }

            if (!selectedYear.equals("All Years") && student.getYear() != Integer.parseInt(selectedYear)) {
                continue;
            }

            boolean eligible = eligibilityChecker.checkEligibility(student);
            int failedCount = eligibilityChecker.getFailedCoursesCount(student);

            // Apply eligibility filter
            if (selectedEligibility.equals("Eligible") && !eligible) {
                continue;
            }
            if (selectedEligibility.equals("Ineligible") && eligible) {
                continue;
            }

            // Apply recovery eligible filter (eligible AND 1-3 failed courses)
            boolean recoveryEligible = eligible && failedCount >= 1 && failedCount <= 3;
            if (showRecoveryEligibleBox.isSelected() && !recoveryEligible) {
                continue;
            }

            double cgpa = eligibilityChecker.calculateCGPA(student);
            java.util.List<models.Course> failedCourses = eligibilityChecker.getFailedCourses(student);

            // SKIP students already enrolled in recovery for all failed courses
            if (hasActiveRecoveryEnrollmentForAllCourses(student.getStudentId(), failedCourses)) {
                continue;
            }

            StringBuilder failedNames = new StringBuilder();
            for (int i = 0; i < failedCourses.size(); i++) {
                if (i > 0) failedNames.append(", ");
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

            tableModel.addRow(row);
        }
    }

    private void enrollSelectedStudentForInstructor(JTable eligibilityTable) {
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

        // Validate enrollment eligibility
        if (!eligibilityStatus.contains("✓")) {
            JOptionPane.showMessageDialog(this,
                "Cannot enroll for recovery.\n\n" +
                studentName + " is not eligible for progression.\n" +
                "CGPA: " + cgpaStr + " | Failed Courses: " + failedCount + "\n\n" +
                "Students who are ineligible must repeat the year.\n" +
                "Recovery enrollment is only for eligible students with 1-3 failed courses.",
                "Ineligible for Recovery",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (failedCount == 0) {
            JOptionPane.showMessageDialog(this,
                "No recovery needed.\n\n" +
                studentName + " has no failed courses.\n" +
                "CGPA: " + cgpaStr + " | Failed Courses: 0\n\n" +
                "This student can progress normally without recovery enrollment.",
                "No Recovery Needed",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Student is eligible AND has 1-3 failed courses - check if instructor has eligible courses
        dao.StudentDAO studentDAO = new dao.StudentDAO();
        models.Student student = studentDAO.loadStudent(studentId);

        if (student != null) {
            // Check if student has any failed courses taught by this instructor
            services.EligibilityChecker eligibilityChecker = new services.EligibilityChecker();
            java.util.List<models.Course> allFailedCourses = eligibilityChecker.getFailedCourses(student);

            // Filter to only courses taught by this instructor
            java.util.List<models.Course> instructorFailedCourses = new java.util.ArrayList<>();
            for (models.Course course : allFailedCourses) {
                if (userId.equals(course.getInstructorId())) {
                    instructorFailedCourses.add(course);
                }
            }

            // If no failed courses belong to this instructor, show message and return
            if (instructorFailedCourses.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No recovery enrollment available.\n\n" +
                    studentName + " has not failed any courses you teach.\n\n" +
                    "This student has failed courses taught by other instructors.\n" +
                    "Only the admin can enroll students\n" +
                    "in recovery for those courses.",
                    "No Courses Available",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Pass instructorId to restrict enrollment to instructor's courses only
            EnrollmentDialog dialog = new EnrollmentDialog(this, student, userId);
            dialog.setVisible(true);
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

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            services.AuthenticationService authService = services.AuthenticationService.getInstance();
            authService.logout(userId);

            dispose();
            new LoginFrame();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InstructorDashboard("Dr. Smith", "I001"));
    }
}