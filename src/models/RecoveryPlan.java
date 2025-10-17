package models;

import enums.RecoveryStatus;
import java.util.Date;

/**
 * Represents a course recovery plan for a student who failed a course.
 * Contains recovery tasks, timeline, status tracking, and instructor notes.
 * Manages the recovery process from initiation to completion.
 */
public class RecoveryPlan {
    private String planId;
    private String studentId;
    private String courseId;
    private String instructorId;
    private int currentActionNumber;
    private RecoveryStatus status;
    private String notes;
    private Date startDate;
    private Date targetEndDate;
    private Date completedDate;

    /**
     * Updates the current action number in the recovery plan.
     * @param actionNumber the new action number (1-4)
     */
    public void updateActionNumber(int actionNumber) {
        this.currentActionNumber = actionNumber;
    }

    /**
     * Updates the status of the recovery plan.
     * @param status the new RecoveryStatus value
     */
    public void updateStatus(RecoveryStatus status) {
        this.status = status;
        if (status == RecoveryStatus.COMPLETED) {
            this.completedDate = new Date();
        }
    }

    /**
     * Adds instructor notes to the recovery plan.
     * @param notes additional notes to append
     */
    public void addNotes(String notes) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = notes;
        } else {
            this.notes += "\n" + notes;
        }
    }

    /**
     * Checks if the recovery plan is overdue.
     * @return true if current date is past target end date and not completed
     */
    public boolean isOverdue() {
        if (status == RecoveryStatus.COMPLETED) {
            return false;
        }
        Date now = new Date();
        return now.after(targetEndDate);
    }

    /**
     * Checks if the recovery plan has been completed.
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return status == RecoveryStatus.COMPLETED;
    }

    /**
     * Gets the student associated with this recovery plan.
     * @return the Student object
     */
    public Student getStudent() {
        // To be implemented
        return null;
    }

    /**
     * Gets the course associated with this recovery plan.
     * @return the Course object
     */
    public Course getCourse() {
        // To be implemented
        return null;
    }

    /**
     * Gets the instructor managing this recovery plan.
     * @return the Instructor object
     */
    public Instructor getInstructor() {
        // To be implemented
        return null;
    }

    // Getters and setters to be implemented
}
