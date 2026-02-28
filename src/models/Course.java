package models;

import dao.InstructorDAO;
import dao.RecoveryCourseActionDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course in the academic system.
 * Contains course information, enrollment capacity, instructor assignment,
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String courseName;
    private int credits;
    private int capacity;
    private int enrolledCount;
    private String instructorId;
    public Course() {
        this.enrolledCount = 0;
    }

    // Constructor with all required fields
    public Course(String courseId, String courseName, int credits, int capacity, String instructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.instructorId = instructorId;
        this.enrolledCount = 0;
    }


    public String getCourseDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Course ID: ").append(courseId).append("\n");
        details.append("Course Name: ").append(courseName).append("\n");
        details.append("Credits: ").append(credits).append("\n");
        details.append("Capacity: ").append(enrolledCount).append("/").append(capacity).append("\n");
        details.append("Instructor ID: ").append(instructorId).append("\n");
        return details.toString();
    }


    public boolean hasAvailableSeats() {
        return enrolledCount < capacity;
    }

    public Instructor getInstructor() {
        if (instructorId == null) {
            return null;
        }
        InstructorDAO instructorDAO = new InstructorDAO();
        return instructorDAO.loadInstructor(instructorId);
    }


    public void addRecoveryAction(RecoveryCourseAction action) {
        if (action != null) {
            RecoveryCourseActionDAO actionDAO = new RecoveryCourseActionDAO();
            actionDAO.saveAction(action);
        }
    }

    public List<RecoveryCourseAction> getRecoveryActions() {
        RecoveryCourseActionDAO actionDAO = new RecoveryCourseActionDAO();
        return actionDAO.loadActionsByCourse(courseId);
    }

    public boolean enrollStudent() {
        if (hasAvailableSeats()) {
            enrolledCount++;
            return true;
        }
        return false;
    }


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
