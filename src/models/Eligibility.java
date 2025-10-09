package models;

import enums.EligibilityStatus;

/**
 * Represents a student's eligibility status for academic progression.
 * Checks CGPA requirements and failed course counts to determine
 * if a student can advance to the next level of study.
 */
public class Eligibility {
    private String studentId;
    private String semesterId;
    private double cgpa;
    private int failedCoursesCount;
    private EligibilityStatus status;

    /**
     * Checks if the student meets eligibility criteria for progression.
     * Requires CGPA >= 2.0 and no more than 3 failed courses.
     * @return true if eligible, false otherwise
     */
    public boolean checkEligibility() {
        // To be implemented
        return false;
    }

    /**
     * Updates the eligibility status based on current academic performance.
     */
    public void updateStatus() {
        // To be implemented
    }

    /**
     * Checks if the student meets the minimum CGPA requirement (2.0).
     * @return true if CGPA is 2.0 or higher, false otherwise
     */
    public boolean meetsMinimumCGPA() {
        // To be implemented
        return false;
    }

    /**
     * Checks if the student has more than the allowed number of failed courses.
     * @return true if failed courses exceed 3, false otherwise
     */
    public boolean hasExcessiveFailures() {
        // To be implemented
        return false;
    }

    // Getters and setters to be implemented
}
