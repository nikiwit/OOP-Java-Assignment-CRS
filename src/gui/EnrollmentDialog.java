package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Student;
import models.Course;
import models.Instructor;
import models.RecoveryCourseEnrollment;
import models.RecoveryCourseAction;
import dao.InstructorDAO;
import dao.RecoveryCourseEnrollmentDAO;
import dao.RecoveryCourseActionDAO;
import services.EligibilityChecker;

/**
 * Dialog for enrolling students in recovery programs.
 */
public class EnrollmentDialog extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);

    private Student student;
    private EligibilityChecker eligibilityChecker;
    private JTable coursesTable;
    private DefaultTableModel coursesTableModel;
    private JComboBox<String> instructorComboBox;
    private JTextArea notesArea;
    private String instructorId;

    /**
     * Constructor for Admin.
     */
    public EnrollmentDialog(JFrame parent, Student student) {
        this(parent, student, null);
    }

    /**
     * Constructor for Instructor.
     * @param parent parent frame
     * @param student student to enroll
     * @param instructorId instructor ID, or null for admin
     */
    public EnrollmentDialog(JFrame parent, Student student, String instructorId) {
        super(parent, "Enroll Student for Recovery", true);
        this.student = student;
        this.eligibilityChecker = new EligibilityChecker();
        this.instructorId = instructorId;

        initializeDialog();
        loadFailedCourses();
    }

    private void initializeDialog() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        double cgpa = eligibilityChecker.calculateCGPA(student);
        int failedCount = eligibilityChecker.getFailedCoursesCount(student);
        boolean eligible = eligibilityChecker.checkEligibility(student);

        StringBuilder info = new StringBuilder();
        info.append("<html><body style='color: white;'>");
        info.append("<h2>").append(student.getFullName()).append("</h2>");
        info.append("<p><b>Student ID:</b> ").append(student.getStudentId()).append("</p>");
        info.append("<p><b>Major:</b> ").append(student.getMajor());
        info.append(" | <b>Year:</b> ").append(student.getYear());
        info.append(" | <b>Semester:</b> ").append(student.getSemester()).append("</p>");
        info.append("<p><b>CGPA:</b> ").append(String.format("%.2f", cgpa));
        info.append(" | <b>Failed Courses:</b> ").append(failedCount);
        info.append(" | <b>Status:</b> ").append(eligible ? "✓ Eligible" : "✗ Ineligible").append("</p>");
        info.append("</body></html>");

        JLabel infoLabel = new JLabel(info.toString());
        panel.add(infoLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Failed Courses - Select courses to enroll for recovery:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Select", "Course ID", "Course Name", "Failed Component"};
        coursesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        coursesTable = new JTable(coursesTableModel);
        coursesTable.setRowHeight(30);
        coursesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JPanel instructorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        instructorPanel.setBackground(BACKGROUND_COLOR);

        JLabel instructorLabel = new JLabel("Assign Instructor:");
        instructorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        instructorComboBox = new JComboBox<>();
        instructorComboBox.setPreferredSize(new Dimension(250, 25));
        loadInstructors();

        instructorPanel.add(instructorLabel);
        instructorPanel.add(instructorComboBox);
        bottomPanel.add(instructorPanel, BorderLayout.NORTH);

        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.setBackground(BACKGROUND_COLOR);

        JLabel notesLabel = new JLabel("Recovery Plan Notes:");
        notesLabel.setFont(new Font("Arial", Font.BOLD, 12));
        notesPanel.add(notesLabel, BorderLayout.NORTH);

        notesArea = new JTextArea(4, 40);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesPanel.add(notesScroll, BorderLayout.CENTER);

        bottomPanel.add(notesPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        JButton enrollButton = new JButton("Enroll for Recovery");
        enrollButton.setBackground(new Color(46, 204, 113));
        enrollButton.setForeground(Color.BLACK);
        enrollButton.setFont(new Font("Arial", Font.BOLD, 14));
        enrollButton.setFocusPainted(false);
        enrollButton.addActionListener(e -> enrollStudent());

        panel.add(cancelButton);
        panel.add(enrollButton);

        return panel;
    }

    private void loadFailedCourses() {
        List<Course> failedCourses = eligibilityChecker.getFailedCourses(student);

        if (instructorId != null) {
            List<Course> instructorCourses = new ArrayList<>();
            for (Course course : failedCourses) {
                if (instructorId.equals(course.getInstructorId())) {
                    instructorCourses.add(course);
                }
            }
            failedCourses = instructorCourses;
        }

        if (failedCourses.isEmpty()) {
            String message = "All failed courses are already enrolled in recovery";

            coursesTableModel.addRow(new Object[]{
                false,
                "N/A",
                message,
                "N/A"
            });
        } else {
            for (Course course : failedCourses) {
                String failedComponent = getFailedComponent(course.getCourseId());

                coursesTableModel.addRow(new Object[]{
                    false,
                    course.getCourseId(),
                    course.getCourseName(),
                    failedComponent
                });
            }
        }
    }

    private String getFailedComponent(String courseId) {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        List<models.Result> results = resultDAO.loadResultsByStudentAndCourse(
            student.getStudentId(), courseId);

        if (results != null && !results.isEmpty()) {
            models.Result latestResult = results.get(results.size() - 1);
            List<enums.ComponentType> failed = latestResult.getFailedComponents();

            if (failed != null && !failed.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < failed.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(failed.get(i).toString());
                }
                return sb.toString();
            }
        }

        return "Both Components";
    }

    private void loadInstructors() {
        List<Course> failedCourses = eligibilityChecker.getFailedCourses(student);

        if (instructorId != null) {
            List<Course> instructorCourses = new ArrayList<>();
            for (Course course : failedCourses) {
                if (instructorId.equals(course.getInstructorId())) {
                    instructorCourses.add(course);
                }
            }
            failedCourses = instructorCourses;
        }

        Set<String> instructorIds = new HashSet<>();
        for (Course course : failedCourses) {
            if (course.getInstructorId() != null && !course.getInstructorId().isEmpty()) {
                instructorIds.add(course.getInstructorId());
            }
        }

        instructorComboBox.addItem("Select Instructor");

        InstructorDAO instructorDAO = new InstructorDAO();
        for (String instId : instructorIds) {
            Instructor instructor = instructorDAO.loadInstructor(instId);
            if (instructor != null) {
                String display = instructor.getUserId() + " - " + instructor.getName();
                instructorComboBox.addItem(display);
            }
        }
    }

    private void enrollStudent() {
        // Validate selections
        List<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < coursesTableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) coursesTableModel.getValueAt(i, 0);
            if (selected != null && selected) {
                selectedRows.add(i);
            }
        }

        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one course for recovery enrollment.",
                "No Courses Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedInstructor = (String) instructorComboBox.getSelectedItem();
        if (selectedInstructor == null || selectedInstructor.equals("Select Instructor")) {
            JOptionPane.showMessageDialog(this,
                "Please select an instructor for the recovery plan.",
                "No Instructor Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String notes = notesArea.getText().trim();

        // Create recovery course enrollments
        RecoveryCourseEnrollmentDAO enrollmentDAO = new RecoveryCourseEnrollmentDAO();
        RecoveryCourseActionDAO actionDAO = new RecoveryCourseActionDAO();
        int successCount = 0;

        for (int row : selectedRows) {
            String courseId = (String) coursesTableModel.getValueAt(row, 1);

            // Check for duplicate enrollment
            List<RecoveryCourseEnrollment> existingEnrollments = enrollmentDAO.loadAllEnrollments();
            boolean isDuplicate = false;

            for (RecoveryCourseEnrollment existing : existingEnrollments) {
                if (existing.getStudentId().equals(student.getStudentId()) &&
                    existing.getCourseId().equals(courseId)) {

                    // Allow retake if previous enrollment FAILED
                    if (existing.getStatus() == RecoveryCourseEnrollment.RecoveryEnrollmentStatus.FAILED) {
                        continue;
                    }

                    // Prevent duplicate for IN_PROGRESS or SUBMITTED
                    JOptionPane.showMessageDialog(this,
                        "Student is already enrolled in this recovery course.\n\n" +
                        "Existing Enrollment:\n" +
                        "  ID: " + existing.getId() + "\n" +
                        "  Status: " + existing.getStatus() + "\n" +
                        "  Action: " + existing.getTitle() + "\n\n" +
                        "Complete or cancel the existing enrollment first.",
                        "Duplicate Enrollment Detected",
                        JOptionPane.ERROR_MESSAGE);
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                continue; // Skip this course and move to next selected course
            }

            // Get FIRST ACTIVE action for this course
            List<RecoveryCourseAction> courseActions = actionDAO.loadActionsByCourse(courseId);
            RecoveryCourseAction firstActiveAction = null;

            for (RecoveryCourseAction action : courseActions) {
                if (action.isActive()) {
                    if (firstActiveAction == null ||
                        action.getActionNumber() < firstActiveAction.getActionNumber()) {
                        firstActiveAction = action;
                    }
                }
            }

            if (firstActiveAction == null) {
                System.err.println("No active recovery actions available for course: " + courseId);
                JOptionPane.showMessageDialog(this,
                    "No active recovery actions available for course: " + courseId,
                    "No Active Actions", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Create recovery course enrollment starting with first active action
            RecoveryCourseEnrollment enrollment = new RecoveryCourseEnrollment();
            enrollment.setId(enrollmentDAO.generateNextEnrollmentId());
            enrollment.setStudentId(student.getStudentId());
            enrollment.setCourseId(courseId);
            enrollment.setPlanStepId(firstActiveAction.getId());
            enrollment.setActionNumber(firstActiveAction.getActionNumber());
            enrollment.setTitle(firstActiveAction.getTitle());
            enrollment.setDescription(firstActiveAction.getDescription());
            enrollment.setStatus(RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS);
            enrollment.setNotes(notes);

            // Save the enrollment
            try {
                saveEnrollment(enrollmentDAO, enrollment);
                successCount++;
            } catch (Exception e) {
                System.err.println("Error saving recovery enrollment: " + e.getMessage());
            }
        }

        // Show result
        if (successCount > 0) {
            JOptionPane.showMessageDialog(this,
                "Successfully enrolled student in " + successCount + " recovery plan(s).",
                "Enrollment Successful",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create recovery plans. Please try again.",
                "Enrollment Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveEnrollment(RecoveryCourseEnrollmentDAO dao, RecoveryCourseEnrollment enrollment) {
        List<RecoveryCourseEnrollment> allEnrollments = dao.loadAllEnrollments();

        StringBuilder content = new StringBuilder();
        content.append("id,student_id,course_id,plan_step_id,action_number,title,description,status,grade,notes\n");

        // Add existing enrollments
        for (RecoveryCourseEnrollment existing : allEnrollments) {
            content.append(formatEnrollment(existing));
        }

        // Add new enrollment
        content.append(formatEnrollment(enrollment));

        utils.FileManager fileManager = new utils.FileManager();
        fileManager.saveToTextFile("recovery_courses_enrollements.txt", content.toString());
    }

    private String formatEnrollment(RecoveryCourseEnrollment enrollment) {
        String gradeStr = enrollment.getGrade() != null ? enrollment.getGrade().toString() : "";
        String notes = enrollment.getNotes() != null
            ? enrollment.getNotes().replace("\n", " ").replace("\"", "\"\"")
            : "";

        StringBuilder sb = new StringBuilder();
        sb.append(enrollment.getId()).append(",");
        sb.append(enrollment.getStudentId()).append(",");
        sb.append(enrollment.getCourseId()).append(",");
        sb.append(enrollment.getPlanStepId() != null ? enrollment.getPlanStepId() : "").append(",");
        sb.append(enrollment.getActionNumber()).append(",");
        sb.append(enrollment.getTitle() != null ? enrollment.getTitle() : "").append(",");
        sb.append(enrollment.getDescription() != null ? enrollment.getDescription() : "").append(",");
        sb.append(enrollment.getStatus() != null ? enrollment.getStatus() : "").append(",");
        sb.append(gradeStr).append(",");
        sb.append("\"").append(notes).append("\"");
        sb.append("\n");

        return sb.toString();
    }
}
