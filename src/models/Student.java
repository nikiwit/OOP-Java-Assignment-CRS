package models;

import java.util.List;

/**
 * Represents a student in the Course Recovery System.
 * Contains student information, academic records, and methods for
 * calculating academic performance and eligibility for progression.
 */
public class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private String major;
    private String email;
    private String status;
    private int year;
    private int semester;
    private boolean retake;
    private List<Grade> grades;
    private List<Result> results;
    private List<RecoveryPlan> recoveryPlans;

    /**
     * Calculates the student's Cumulative Grade Point Average (CGPA).
     * @return the calculated CGPA value
     */
    public double calculateCGPA() {
        // To be implemented
        return 0.0;
    }

    /**
     * Retrieves all courses that the student has failed.
     * @return list of failed courses
     */
    public List<Course> getFailedCourses() {
        // To be implemented
        return null;
    }

    /**
     * Checks if the student is eligible to progress to the next level of study.
     * Eligibility requires CGPA >= 2.0 and no more than 3 failed courses.
     * @return true if eligible, false otherwise
     */
    public boolean isEligibleForProgression() {
        // To be implemented
        return false;
    }

    /**
     * Returns the student's full name (first name + last name).
     * @return the full name as a string
     */
    public String getFullName() {
        // To be implemented
        return firstName + " " + lastName;
    }

    /**
     * Adds a grade record to the student's academic history.
     * @param grade the grade to add
     */
    public void addGrade(Grade grade) {
        // To be implemented
    }

    // Getters and setters to be implemented
}
