package models;

import java.util.List;

/**
 * Represents a course in the academic system.
 * Contains course information, enrollment capacity, instructor assignment,
 * and associated recovery actions for students who fail the course.
 */
public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private int capacity;
    private String instructorId;
    private List<RecoveryCourseAction> recoveryActions;

    /**
     * Retrieves detailed information about the course.
     * @return formatted string with course details
     */
    public String getCourseDetails() {
        // To be implemented
        return null;
    }

    /**
     * Checks if the course has available enrollment seats.
     * @return true if seats are available, false otherwise
     */
    public boolean hasAvailableSeats() {
        // To be implemented
        return false;
    }

    /**
     * Gets the instructor assigned to this course.
     * @return the Instructor object
     */
    public Instructor getInstructor() {
        // To be implemented
        return null;
    }

    /**
     * Adds a recovery action to the course's recovery plan.
     * @param action the recovery action to add
     */
    public void addRecoveryAction(RecoveryCourseAction action) {
        // To be implemented
    }

    // Getters and setters to be implemented
}
