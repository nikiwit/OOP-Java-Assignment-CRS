package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a course-level recovery plan template (NOT student-specific).
 * Defines the uniform recovery strategy that multiple students can follow.
 * Contains plan metadata, title, description, and references to recovery actions.
 * This is the template that instructors create and modify for each course.
 */
public class CourseRecoveryPlanTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String templateId;
    private String courseId;
    private String instructorId;
    private String planTitle;
    private String planDescription;
    private List<String> actionIds;  // References to RecoveryCourseAction IDs
    private boolean isActive;
    private Date createdDate;
    private Date lastModifiedDate;

    /**
     * Default constructor.
     */
    public CourseRecoveryPlanTemplate() {
        this.actionIds = new ArrayList<>();
        this.isActive = true;
        this.createdDate = new Date();
        this.lastModifiedDate = new Date();
    }

    /**
     * Constructor with essential fields.
     *
     * @param templateId unique template identifier
     * @param courseId course ID
     * @param instructorId instructor ID who created the template
     * @param planTitle title of the recovery plan
     * @param planDescription description of the recovery strategy
     */
    public CourseRecoveryPlanTemplate(String templateId, String courseId, String instructorId,
                                     String planTitle, String planDescription) {
        this.templateId = templateId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.planTitle = planTitle;
        this.planDescription = planDescription;
        this.actionIds = new ArrayList<>();
        this.isActive = true;
        this.createdDate = new Date();
        this.lastModifiedDate = new Date();
    }

    /**
     * Adds a recovery action to this template.
     * @param actionId the ID of the RecoveryCourseAction to add
     */
    public void addAction(String actionId) {
        if (actionId != null && !actionIds.contains(actionId)) {
            actionIds.add(actionId);
            this.lastModifiedDate = new Date();
        }
    }

    /**
     * Removes a recovery action from this template.
     * @param actionId the ID of the RecoveryCourseAction to remove
     * @return true if removed successfully
     */
    public boolean removeAction(String actionId) {
        boolean removed = actionIds.remove(actionId);
        if (removed) {
            this.lastModifiedDate = new Date();
        }
        return removed;
    }

    /**
     * Gets the total number of actions in this template.
     * @return count of actions
     */
    public int getActionCount() {
        return actionIds.size();
    }

    /**
     * Checks if this template contains a specific action.
     * @param actionId action ID to check
     * @return true if action is in the template
     */
    public boolean containsAction(String actionId) {
        return actionIds.contains(actionId);
    }

    /**
     * Updates the template metadata (title and description).
     * @param newTitle new plan title
     * @param newDescription new plan description
     */
    public void updateMetadata(String newTitle, String newDescription) {
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            this.planTitle = newTitle;
        }
        if (newDescription != null) {
            this.planDescription = newDescription;
        }
        this.lastModifiedDate = new Date();
    }

    /**
     * Deactivates this template (prevents new student enrollments).
     */
    public void deactivate() {
        this.isActive = false;
        this.lastModifiedDate = new Date();
    }

    /**
     * Activates this template (allows new student enrollments).
     */
    public void activate() {
        this.isActive = true;
        this.lastModifiedDate = new Date();
    }

    /**
     * Gets formatted display of template information.
     * @return formatted string
     */
    public String getTemplateDisplay() {
        StringBuilder display = new StringBuilder();
        display.append("=== Recovery Plan Template ===\n");
        display.append("Template ID: ").append(templateId).append("\n");
        display.append("Course: ").append(courseId).append("\n");
        display.append("Title: ").append(planTitle).append("\n");
        display.append("Description: ").append(planDescription).append("\n");
        display.append("Total Actions: ").append(actionIds.size()).append("\n");
        display.append("Status: ").append(isActive ? "Active" : "Inactive").append("\n");
        display.append("Created: ").append(createdDate).append("\n");
        display.append("Last Modified: ").append(lastModifiedDate).append("\n");
        display.append("=============================");
        return display.toString();
    }

    @Override
    public String toString() {
        return String.format("Recovery Plan Template: %s | Course: %s | Actions: %d | Status: %s",
                planTitle, courseId, actionIds.size(), isActive ? "Active" : "Inactive");
    }

    // Getters and Setters

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
        this.lastModifiedDate = new Date();
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
        this.lastModifiedDate = new Date();
    }

    public List<String> getActionIds() {
        return new ArrayList<>(actionIds);  // Return copy for immutability
    }

    public void setActionIds(List<String> actionIds) {
        this.actionIds = new ArrayList<>(actionIds);
        this.lastModifiedDate = new Date();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        this.lastModifiedDate = new Date();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
