package models;

import dao.InstructorDAO;
import dao.RecoveryCourseActionDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course in the academic system.
 * Contains course information, enrollment capacity, instructor assignment,
 * and associated recovery actions for students who fail the course.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String courseName;
    private int credits;
    private int capacity;
    private int enrolledCount;
    private String instructorId;

    /**
     * Default constructor.
     */
    public Course() {
        this.enrolledCount = 0;
    }

    /**
     * Constructor with all required fields.
     *
     * @param courseId unique course identifier
     * @param courseName course name
     * @param credits number of credits
     * @param capacity maximum enrollment capacity
     * @param instructorId instructor ID
     */
    public Course(String courseId, String courseName, int credits, int capacity, String instructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.instructorId = instructorId;
        this.enrolledCount = 0;
    }

    /**
     * Retrieves detailed information about the course.
     * @return formatted string with course details
     */
    public String getCourseDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Course ID: ").append(courseId).append("\n");
        details.append("Course Name: ").append(courseName).append("\n");
        details.append("Credits: ").append(credits).append("\n");
        details.append("Capacity: ").append(enrolledCount).append("/").append(capacity).append("\n");
        details.append("Instructor ID: ").append(instructorId).append("\n");
        return details.toString();
    }

    /**
     * Checks if the course has available enrollment seats.
     * @return true if seats are available, false otherwise
     */
    public boolean hasAvailableSeats() {
        return enrolledCount < capacity;
    }

    /**
     * Gets the instructor assigned to this course.
     * @return the Instructor object
     */
    public Instructor getInstructor() {
        if (instructorId == null) {
            return null;
        }
        InstructorDAO instructorDAO = new InstructorDAO();
        return instructorDAO.loadInstructor(instructorId);
    }

    /**
     * Adds a recovery action to the course's recovery plan.
     * @param action the recovery action to add
     */
    public void addRecoveryAction(RecoveryCourseAction action) {
        if (action != null) {
            RecoveryCourseActionDAO actionDAO = new RecoveryCourseActionDAO();
            actionDAO.saveAction(action);
        }
    }

    /**
     * Gets all recovery actions for this course.
     * @return list of recovery actions
     */
    public List<RecoveryCourseAction> getRecoveryActions() {
        RecoveryCourseActionDAO actionDAO = new RecoveryCourseActionDAO();
        return actionDAO.loadActionsByCourse(courseId);
    }

    /**
     * Enrolls a student in the course if seats are available.
     * @return true if enrollment successful, false otherwise
     */
    public boolean enrollStudent() {
        if (hasAvailableSeats()) {
            enrolledCount++;
            return true;
        }
        return false;
    }

    /**
     * Removes a student enrollment from the course.
     */
    public void unenrollStudent() {
        if (enrolledCount > 0) {
            enrolledCount--;
        }
    }

    // Getters and Setters

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }
}
