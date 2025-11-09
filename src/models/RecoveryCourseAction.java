package models;

import java.io.Serializable;

/**
 * Represents a specific action or task in a course recovery plan.
 * Contains structured recovery steps with action numbers, descriptions, and notes.
 * Defines the template for recovery tasks that students must complete.
 */
public class RecoveryCourseAction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String courseId;
    private String instructorId;
    private int actionNumber;
    private String title;
    private String description;
    private boolean isActive;
    private boolean hasGrade;

    /**
     * Default constructor.
     */
    public RecoveryCourseAction() {
        this.isActive = true;
        this.hasGrade = false;
    }

    /**
     * Constructor with all fields.
     *
     * @param id action ID
     * @param courseId course ID
     * @param instructorId instructor ID
     * @param actionNumber sequence number
     * @param title action title
     * @param description detailed description
     * @param isActive whether action is currently active
     * @param hasGrade whether action requires grading
     */
    public RecoveryCourseAction(String id, String courseId, String instructorId,
                               int actionNumber, String title, String description,
                               boolean isActive, boolean hasGrade) {
        this.id = id;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.actionNumber = actionNumber;
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.hasGrade = hasGrade;
    }

    /**
     * Gets detailed information about the recovery action.
     * @return formatted action details string
     */
    public String getActionDetails() {
        StringBuilder details = new StringBuilder();
        details.append("=== Recovery Action ===\n");
        details.append("Action #").append(actionNumber).append(": ").append(title).append("\n");
        details.append("Course: ").append(courseId).append("\n");
        details.append("Description: ").append(description).append("\n");
        details.append("Requires Grade: ").append(hasGrade ? "Yes" : "No").append("\n");
        details.append("Status: ").append(isActive ? "Active" : "Inactive").append("\n");
        details.append("=======================");
        return details.toString();
    }

    /**
     * Checks if this action is a prerequisite for the next action.
     * @return true if action must be completed before progressing
     */
    public boolean isPrerequisite() {
        return isActive;
    }

    /**
     * Returns a string representation of the recovery action.
     * @return formatted string describing the action
     */
    @Override
    public String toString() {
        return "Action " + actionNumber + ": " + title +
               (hasGrade ? " [Graded]" : " [Not Graded]");
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHasGrade() {
        return hasGrade;
    }

    public void setHasGrade(boolean hasGrade) {
        this.hasGrade = hasGrade;
    }
}
