package models;

import enums.ComponentType;
import enums.GradeStatus;
import java.util.Date;

/**
 * Represents a grade record for a specific course component (assignment or exam).
 * Tracks individual assessment results and includes attempt number for retakes.
 */
public class Grade {
    private String gradeId;
    private String studentId;
    private String courseId;
    private String courseName;
    private int attemptNumber;
    private ComponentType component;
    private String grade;
    private double gradePoint;
    private GradeStatus status;
    private String semesterId;
    private String instructorId;
    private Date gradedDate;

    /**
     * Calculates the grade point value based on the letter grade.
     * @return the grade point on a 4.0 scale
     */
    public double calculateGradePoint() {
        // To be implemented
        return 0.0;
    }

    /**
     * Checks if the grade is passing (C or better).
     * @return true if passed, false otherwise
     */
    public boolean isPassed() {
        // To be implemented
        return false;
    }

    /**
     * Checks if the grade is failing (F).
     * @return true if failed, false otherwise
     */
    public boolean isFailed() {
        // To be implemented
        return false;
    }

    /**
     * Gets the component type (Assignment or Exam).
     * @return the ComponentType enum value
     */
    public ComponentType getComponentType() {
        return component;
    }

    // Getters and setters to be implemented
}
