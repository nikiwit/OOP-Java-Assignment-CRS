package models;

import enums.ComponentType;
import enums.GradeStatus;
import java.util.List;

/**
 * Represents the overall result for a course, combining all component grades.
 * Tracks whether both assignment and exam components have been passed,
 * and determines if recovery is needed.
 */
public class Result {
    private String resultId;
    private String studentId;
    private String courseId;
    private String courseName;
    private int attemptNumber;
    private String grade;
    private double gradePoint;
    private boolean passedExam;
    private boolean passedAssignment;
    private GradeStatus status;
    private String semesterId;

    /**
     * Checks if the student passed the overall course.
     * @return true if passed, false otherwise
     */
    public boolean isPassed() {
        // To be implemented
        return false;
    }

    /**
     * Determines if the student needs course recovery.
     * @return true if recovery is needed, false otherwise
     */
    public boolean needsRecovery() {
        // To be implemented
        return false;
    }

    /**
     * Gets a list of failed components (Assignment and/or Exam).
     * @return list of failed ComponentType values
     */
    public List<ComponentType> getFailedComponents() {
        // To be implemented
        return null;
    }

    // Getters and setters to be implemented
}
