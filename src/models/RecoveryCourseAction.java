package models;




public class RecoveryCourseAction {
    private String id;
    private String courseId;
    private String instructorId;
    private int actionNumber;
    private String title;
    private String description;
    private boolean isActive;
    private boolean hasGrade;


    public RecoveryCourseAction() {
        this.isActive = true;
        this.hasGrade = false;
    }


    // Full constructor with all fields.

    public RecoveryCourseAction(String id,
                                String courseId,
                                String instructorId,
                                int actionNumber,
                                String title,
                                String description,
                                boolean isActive,
                                boolean hasGrade) {
        this.id = id;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.actionNumber = actionNumber;
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.hasGrade = hasGrade;
    }

   
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


    public boolean isPrerequisite() {
        return isActive;
    }

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

    // kept original getter name for compatibility
    public boolean isHasGrade() {
        return hasGrade;
    }

    public void setHasGrade(boolean hasGrade) {
        this.hasGrade = hasGrade;
    }
}
