package services;

import models.Student;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility service for checking student eligibility for academic progression.
 * Validates CGPA requirements (>= 2.0) and failed course limits (<= 3).
 * Contains static utility methods for eligibility calculations.
 * Eligibility is calculated on-the-fly, not stored.
 */
public class EligibilityChecker {
    private static final double MIN_CGPA = 2.0;
    private static final int MAX_FAILED_COURSES = 3;

    /**
     * Checks if a student is eligible to progress to the next level.
     * @param student the student to check
     * @return true if eligible (CGPA >= 2.0 AND failed courses <= 3), false otherwise
     */
    public static boolean checkEligibility(Student student) {
        double cgpa = calculateCGPA(student);
        int failedCount = getFailedCoursesCount(student);
        return cgpa >= MIN_CGPA && failedCount <= MAX_FAILED_COURSES;
    }

    /**
     * Counts the number of failed courses for a student.
     * @param student the student
     * @return count of failed courses
     */
    public static int getFailedCoursesCount(Student student) {
        // To be implemented - count courses where status is FAILED
        return 0;
    }

    /**
     * Gets a list of all students who are not eligible for progression.
     * @param students list of all students to check
     * @return list of ineligible students
     */
    public static List<Student> getIneligibleStudents(List<Student> students) {
        List<Student> ineligible = new ArrayList<>();
        for (Student student : students) {
            if (!checkEligibility(student)) {
                ineligible.add(student);
            }
        }
        return ineligible;
    }

    /**
     * Calculates the CGPA for a student.
     * @param student the student
     * @return calculated CGPA value
     */
    public static double calculateCGPA(Student student) {
        // To be implemented - use CGPACalculator
        return 0.0;
    }

    // Additional helper methods to be implemented
}
