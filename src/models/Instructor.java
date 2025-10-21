package models;

import enums.UserRole;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instructor user in the Course Recovery System.
 * Instructors can create and manage recovery plans, grade student work,
 * and track student progress in their assigned courses.
 * Extends the User class demonstrating inheritance.
 */
public class Instructor extends User {
    private static final long serialVersionUID = 1L;

    private String instructorName;
    private List<String> assignedCourseIds;

    /**
     * Default constructor.
     */
    public Instructor() {
        super();
        setRole(UserRole.INSTRUCTOR);
        this.assignedCourseIds = new ArrayList<>();
    }

    /**
     * Constructor with all fields.
     *
     * @param userId user unique identifier
     * @param email instructor email address
     * @param password instructor password
     * @param instructorName instructor full name
     */
    public Instructor(String userId, String email, String password, String instructorName) {
        super(userId, UserRole.INSTRUCTOR, email, password);
        this.instructorName = instructorName;
        this.assignedCourseIds = new ArrayList<>();
    }

    /**
     * Creates a new recovery plan for a student who failed a course.
     *
     * @param student the student requiring recovery
     * @param course the course that needs to be recovered
     * @return the created RecoveryPlan object
     */
    public RecoveryPlan createRecoveryPlan(Student student, Course course) {
        // Will be implemented when RecoveryPlan is fully connected
        System.out.println("Instructor creating recovery plan for student");
        return null;
    }

    /**
     * Updates an existing recovery plan with new information.
     *
     * @param plan the recovery plan to update
     */
    public void updateRecoveryPlan(RecoveryPlan plan) {
        // Will be implemented when RecoveryPlanDAO is connected
        System.out.println("Instructor updating recovery plan");
    }

    /**
     * Tracks and monitors the progress of a student's recovery plan.
     *
     * @param plan the recovery plan to track
     */
    public void trackProgress(RecoveryPlan plan) {
        // Will be implemented when RecoveryPlanDAO is connected
        System.out.println("Instructor tracking progress for plan");
    }

    /**
     * Grades a student's recovery work and updates the grade record.
     *
     * @param grade the grade object to process
     */
    public void gradeRecovery(Grade grade) {
        // Will be implemented when GradeDAO is connected
        System.out.println("Instructor grading recovery work");
    }

    /**
     * Retrieves all courses assigned to this instructor.
     *
     * @return list of assigned course IDs
     */
    public List<String> getAssignedCourses() {
        return assignedCourseIds;
    }

    /**
     * Assigns a new course to this instructor.
     *
     * @param courseId the course ID to assign
     */
    public void assignCourse(String courseId) {
        if (courseId != null && !assignedCourseIds.contains(courseId)) {
            assignedCourseIds.add(courseId);
        }
    }

    // Getters and Setters

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public List<String> getAssignedCourseIds() {
        return assignedCourseIds;
    }

    public void setAssignedCourseIds(List<String> assignedCourseIds) {
        this.assignedCourseIds = assignedCourseIds;
    }
}
