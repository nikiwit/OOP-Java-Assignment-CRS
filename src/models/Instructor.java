package models;

import enums.UserRole;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instructor in the system.
 */
public class Instructor extends User {
    private static final long serialVersionUID = 1L;

    private String instructorName;
    private List<String> assignedCourseIds;

    // Default constructor
    public Instructor() {
        super();
        setRole(UserRole.INSTRUCTOR);
        this.assignedCourseIds = new ArrayList<>();
    }

    // Constructor that matches User.java
    public Instructor(String userId, String email, String password, String instructorName) {
        super(userId, UserRole.INSTRUCTOR, instructorName, email, password, true);
        this.instructorName = instructorName;
        this.assignedCourseIds = new ArrayList<>();
    }
    

    // Getters/Setters
    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
        this.setName(instructorName); // keeps User.name in sync
    }

    public List<String> getAssignedCourseIds() {
        return assignedCourseIds;
    }

    public void setAssignedCourseIds(List<String> ids) {
        this.assignedCourseIds = ids;
    }
}
