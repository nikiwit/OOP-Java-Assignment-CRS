package services;

import models.Grade;
import java.util.List;

/**
 * Utility service for calculating GPA and CGPA values.
 * Provides static methods for academic performance calculations
 * based on the Credit Hour System.
 */
public class CGPACalculator {

    /**
     * Calculates the cumulative GPA (CGPA) from all grades.
     * Formula: (Total Grade Points) / (Total Credit Hours)
     * @param grades list of all grade records
     * @return calculated CGPA value
     */
    public static double calculateCGPA(List<Grade> grades) {
        // To be implemented
        return 0.0;
    }

    /**
     * Calculates the GPA for a specific semester.
     * @param grades list of grade records
     * @param semesterId the semester to calculate for
     * @return semester GPA value
     */
    public static double calculateSemesterGPA(List<Grade> grades, String semesterId) {
        // To be implemented
        return 0.0;
    }

    /**
     * Calculates the total credit hours from a list of grades.
     * @param grades list of grade records
     * @return total credit hours
     */
    public static int getTotalCreditHours(List<Grade> grades) {
        // To be implemented
        return 0;
    }

    /**
     * Calculates the total grade points from a list of grades.
     * Grade points = (Grade Point Value × Credit Hours) for each course
     * @param grades list of grade records
     * @return total grade points
     */
    public static double getTotalGradePoints(List<Grade> grades) {
        // To be implemented
        return 0.0;
    }

    // Additional helper methods to be implemented
}
