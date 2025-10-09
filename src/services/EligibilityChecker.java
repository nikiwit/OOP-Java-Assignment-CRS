package services;

import models.Student;
import models.Semester;
import models.Eligibility;
import models.Grade;
import java.util.List;

/**
 * Utility service for checking student eligibility for academic progression.
 * Validates CGPA requirements (>= 2.0) and failed course limits (<= 3).
 * Contains static utility methods for eligibility calculations.
 */
public class EligibilityChecker {
    private static final double MIN_CGPA = 2.0;
    private static final int MAX_FAILED_COURSES = 3;

    /**
     * Checks if a student is eligible to progress to the next level.
     * @param student the student to check
     * @param semester the current semester
     * @return Eligibility object with status and details
     */
    public static Eligibility checkEligibility(Student student, Semester semester) {
        // To be implemented
        return null;
    }

    /**
     * Counts the number of failed courses for a student.
     * @param student the student
     * @return count of failed courses
     */
    public static int getFailedCoursesCount(Student student) {
        // To be implemented
        return 0;
    }

    /**
     * Gets a list of all students who are not eligible for progression.
     * @param students list of all students to check
     * @return list of ineligible students
     */
    public static List<Student> getIneligibleStudents(List<Student> students) {
        // To be implemented
        return null;
    }

    /**
     * Calculates the CGPA from a list of grades.
     * @param grades list of grade records
     * @return calculated CGPA value
     */
    public static double calculateCGPA(List<Grade> grades) {
        // To be implemented
        return 0.0;
    }

    // Additional helper methods to be implemented
}
