package dao;

import models.RecoveryCourseEnrollment;
import models.RecoveryCourseEnrollment.RecoveryEnrollmentStatus;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for RecoveryCourseEnrollment entities.
 * Handles CRUD operations for recovery course enrollment data stored in text files.
 * Manages student progress through recovery tasks.
 */
public class RecoveryCourseEnrollmentDAO {
    private static final String ENROLLMENTS_FILE = "recovery_courses_enrollements.txt";
    private static final String DELIMITER = ",";
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public RecoveryCourseEnrollmentDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public RecoveryCourseEnrollmentDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Loads all enrollments for a specific student and course.
     * @param studentId student ID
     * @param courseId course ID
     * @return list of enrollments
     */
    public List<RecoveryCourseEnrollment> loadEnrollmentsByStudentAndCourse(String studentId, String courseId) {
        List<RecoveryCourseEnrollment> allEnrollments = loadAllEnrollments();
        List<RecoveryCourseEnrollment> filtered = new ArrayList<>();

        for (RecoveryCourseEnrollment enrollment : allEnrollments) {
            if (enrollment.getStudentId().equals(studentId) &&
                enrollment.getCourseId().equals(courseId)) {
                filtered.add(enrollment);
            }
        }

        return filtered;
    }

    /**
     * Loads all enrollments for a specific course.
     * @param courseId course ID
     * @return list of enrollments
     */
    public List<RecoveryCourseEnrollment> loadEnrollmentsByCourse(String courseId) {
        List<RecoveryCourseEnrollment> allEnrollments = loadAllEnrollments();
        List<RecoveryCourseEnrollment> filtered = new ArrayList<>();

        for (RecoveryCourseEnrollment enrollment : allEnrollments) {
            if (enrollment.getCourseId().equals(courseId)) {
                filtered.add(enrollment);
            }
        }

        return filtered;
    }

    /**
     * Loads all enrollments for a specific student.
     * @param studentId student ID
     * @return list of enrollments
     */
    public List<RecoveryCourseEnrollment> loadEnrollmentsByStudent(String studentId) {
        List<RecoveryCourseEnrollment> allEnrollments = loadAllEnrollments();
        List<RecoveryCourseEnrollment> filtered = new ArrayList<>();

        for (RecoveryCourseEnrollment enrollment : allEnrollments) {
            if (enrollment.getStudentId().equals(studentId)) {
                filtered.add(enrollment);
            }
        }

        return filtered;
    }

    /**
     * Loads a specific enrollment by ID.
     * @param enrollmentId enrollment ID
     * @return enrollment object or null
     */
    public RecoveryCourseEnrollment loadEnrollment(String enrollmentId) {
        List<RecoveryCourseEnrollment> allEnrollments = loadAllEnrollments();

        for (RecoveryCourseEnrollment enrollment : allEnrollments) {
            if (enrollment.getId().equals(enrollmentId)) {
                return enrollment;
            }
        }

        return null;
    }

    /**
     * Loads all enrollments from the data file.
     * @return list of all enrollments
     */
    public List<RecoveryCourseEnrollment> loadAllEnrollments() {
        List<RecoveryCourseEnrollment> enrollments = new ArrayList<>();
        String content = fileManager.loadFromTextFile(ENROLLMENTS_FILE);

        if (content.isEmpty()) {
            return enrollments;
        }

        String[] lines = content.split("\n");
        for (int i = 1; i < lines.length; i++) { // Skip header
            String line = lines[i];
            if (line.trim().isEmpty()) {
                continue;
            }

            RecoveryCourseEnrollment enrollment = parseEnrollment(line);
            if (enrollment != null) {
                enrollments.add(enrollment);
            }
        }

        return enrollments;
    }

    /**
     * Updates an enrollment's status.
     * @param enrollmentId enrollment ID
     * @param newStatus new status
     * @return true if successful
     */
    public boolean updateEnrollmentStatus(String enrollmentId, RecoveryEnrollmentStatus newStatus) {
        RecoveryCourseEnrollment enrollment = loadEnrollment(enrollmentId);
        if (enrollment == null) {
            return false;
        }

        enrollment.setStatus(newStatus);
        return updateEnrollment(enrollment);
    }

    /**
     * Updates an enrollment's grade.
     * @param enrollmentId enrollment ID
     * @param grade grade value
     * @return true if successful
     */
    public boolean updateEnrollmentGrade(String enrollmentId, Integer grade) {
        RecoveryCourseEnrollment enrollment = loadEnrollment(enrollmentId);
        if (enrollment == null) {
            return false;
        }

        enrollment.setGrade(grade);
        return updateEnrollment(enrollment);
    }

    /**
     * Updates an enrollment's notes.
     * @param enrollmentId enrollment ID
     * @param notes new notes
     * @return true if successful
     */
    public boolean updateEnrollmentNotes(String enrollmentId, String notes) {
        RecoveryCourseEnrollment enrollment = loadEnrollment(enrollmentId);
        if (enrollment == null) {
            return false;
        }

        enrollment.setNotes(notes);
        return updateEnrollment(enrollment);
    }

    /**
     * Updates an existing enrollment.
     * @param enrollment enrollment to update
     * @return true if successful
     */
    public boolean updateEnrollment(RecoveryCourseEnrollment enrollment) {
        List<RecoveryCourseEnrollment> allEnrollments = loadAllEnrollments();
        StringBuilder updatedContent = new StringBuilder();

        // Write header
        updatedContent.append("id,student_id,course_id,plan_step_id,action_number,title,description,status,grade,notes\n");

        boolean found = false;
        for (RecoveryCourseEnrollment existing : allEnrollments) {
            if (existing.getId().equals(enrollment.getId())) {
                updatedContent.append(formatEnrollment(enrollment));
                found = true;
            } else {
                updatedContent.append(formatEnrollment(existing));
            }
        }

        if (!found) {
            return false;
        }

        fileManager.saveToTextFile(ENROLLMENTS_FILE, updatedContent.toString());
        return true;
    }

    /**
     * Formats an enrollment for file storage.
     * @param enrollment enrollment to format
     * @return CSV formatted string
     */
    private String formatEnrollment(RecoveryCourseEnrollment enrollment) {
        String gradeStr = enrollment.getGrade() != null ? enrollment.getGrade().toString() : "";
        String notes = enrollment.getNotes() != null ?
                       enrollment.getNotes().replace("\n", " ").replace("\"", "\"\"") : "";

        return String.format("%s,%s,%s,%s,%d,%s,\"%s\",%s,%s,%s\n",
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getPlanStepId() != null ? enrollment.getPlanStepId() : "",
                enrollment.getActionNumber(),
                enrollment.getTitle(),
                enrollment.getDescription(),
                enrollment.getStatus(),
                gradeStr,
                notes);
    }

    /**
     * Parses a CSV line into a RecoveryCourseEnrollment object.
     * Format: id,student_id,course_id,plan_step_id,action_number,title,description,status,grade,notes
     * @param line CSV line
     * @return enrollment object or null
     */
    private RecoveryCourseEnrollment parseEnrollment(String line) {
        try {
            // Handle quoted fields
            List<String> parts = parseCSVLine(line);

            if (parts.size() < 8) {
                return null;
            }

            RecoveryCourseEnrollment enrollment = new RecoveryCourseEnrollment();
            enrollment.setId(parts.get(0).trim());
            enrollment.setStudentId(parts.get(1).trim());
            enrollment.setCourseId(parts.get(2).trim());
            enrollment.setPlanStepId(parts.size() > 3 ? parts.get(3).trim() : null);
            enrollment.setActionNumber(Integer.parseInt(parts.get(4).trim()));
            enrollment.setTitle(parts.get(5).trim());
            enrollment.setDescription(parts.get(6).trim());
            enrollment.setStatus(RecoveryEnrollmentStatus.fromString(parts.get(7).trim()));

            // Parse grade (optional)
            if (parts.size() > 8 && !parts.get(8).trim().isEmpty()) {
                try {
                    enrollment.setGrade(Integer.parseInt(parts.get(8).trim()));
                } catch (NumberFormatException e) {
                    enrollment.setGrade(null);
                }
            }

            // Parse notes (optional)
            if (parts.size() > 9 && !parts.get(9).trim().isEmpty()) {
                enrollment.setNotes(parts.get(9).trim());
            }

            return enrollment;
        } catch (Exception e) {
            System.err.println("Error parsing enrollment: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses a CSV line handling quoted fields.
     * @param line CSV line
     * @return list of field values
     */
    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result;
    }
}
