package models;

import java.util.List;

/**
 * Represents an instructor user in the Course Recovery System.
 * Instructors can create and manage recovery plans, grade student work,
 * and track student progress in their assigned courses.
 * Extends the User class demonstrating inheritance.
 */
public class Instructor extends User {
    private String instructorId;
    private String instructorName;
    private List<Course> assignedCourses;

    /**
     * Creates a new recovery plan for a student who failed a course.
     * @param student the student requiring recovery
     * @param course the course that needs to be recovered
     * @param task the initial recovery task description
     * @return the created RecoveryPlan object
     */
    public RecoveryPlan createRecoveryPlan(Student student, Course course, String task) {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing recovery plan with new information.
     * @param plan the recovery plan to update
     */
    public void updateRecoveryPlan(RecoveryPlan plan) {
        // To be implemented
    }

    /**
     * Tracks and monitors the progress of a student's recovery plan.
     * @param plan the recovery plan to track
     */
    public void trackProgress(RecoveryPlan plan) {
        // To be implemented
    }

    /**
     * Grades a student's recovery work and updates the grade record.
     * @param grade the grade object to process
     */
    public void gradeRecovery(Grade grade) {
        // To be implemented
    }

    /**
     * Retrieves all courses assigned to this instructor.
     * @return list of assigned courses
     */
    public List<Course> getAssignedCourses() {
        // To be implemented
        return assignedCourses;
    }

    /**
     * Assigns a new course to this instructor.
     * @param course the course to assign
     */
    public void assignCourse(Course course) {
        // To be implemented
    }

    // Getters and setters to be implemented
}
