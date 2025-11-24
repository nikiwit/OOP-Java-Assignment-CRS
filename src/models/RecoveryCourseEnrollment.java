package models;

import java.io.Serializable;

// Represents a student's enrollment in a specific recovery course action.
public class RecoveryCourseEnrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String studentId;
    private String courseId;
    private String planStepId;
    private int actionNumber;
    private String title;
    private String description;
    private RecoveryEnrollmentStatus status;
    private Integer grade; 
    private String notes;


    public RecoveryCourseEnrollment() {
        this.status = RecoveryEnrollmentStatus.IN_PROGRESS;
    }

    
    // Constructor with essential fields.
    
    public RecoveryCourseEnrollment(String id, String studentId, String courseId,
                                   int actionNumber, String title, String description) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.actionNumber = actionNumber;
        this.title = title;
        this.description = description;
        this.status = RecoveryEnrollmentStatus.IN_PROGRESS;
    }

    
    public boolean requiresGrade() {
        return grade != null || status == RecoveryEnrollmentStatus.SUBMITTED;
    }

    public boolean isCompleted() {
        return status == RecoveryEnrollmentStatus.SUBMITTED;
    }

    public boolean isFailed() {
        return status == RecoveryEnrollmentStatus.FAILED;
    }

    public boolean isInProgress() {
        return status == RecoveryEnrollmentStatus.IN_PROGRESS;
    }

    public String getStatusDisplay() {
        StringBuilder display = new StringBuilder();
        display.append("Task ").append(actionNumber).append(": ").append(title).append("\n");
        display.append("Status: ").append(status).append("\n");

        if (grade != null) {
            display.append("Grade: ").append(grade).append("%\n");
        }

        if (notes != null && !notes.isEmpty()) {
            display.append("Notes: ").append(notes).append("\n");
        }

        return display.toString();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getPlanStepId() {
        return planStepId;
    }

    public void setPlanStepId(String planStepId) {
        this.planStepId = planStepId;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int actionNumber) {
        this.actionNumber = actionNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecoveryEnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryEnrollmentStatus status) {
        this.status = status;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Enum for recovery enrollment status.
     */
    public enum RecoveryEnrollmentStatus {
        IN_PROGRESS("IN-PROGRESS"),
        SUBMITTED("SUBMITTED"),
        FAILED("FAILED");

        private final String displayName;

        RecoveryEnrollmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        /**
         * Parse status from string (handles hyphenated format).
         * @param status string representation
         * @return corresponding enum value
         */
        public static RecoveryEnrollmentStatus fromString(String status) {
            if (status == null) {
                return IN_PROGRESS;
            }

            String normalized = status.toUpperCase().replace("-", "_");

            try {
                return RecoveryEnrollmentStatus.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                return IN_PROGRESS;
            }
        }
    }
}
