package models;

import java.util.Date;
import java.util.List;

/**
 * Represents an academic performance report for a student.
 * Generates comprehensive reports including grades, GPA calculations,
 * and exports to PDF format as required by the assignment.
 */
public class AcademicReport {
    private String reportId;
    private Student student;
    private String semester;
    private String year;
    private List<Grade> grades;
    private List<Result> results;
    private double semesterGPA;
    private double cumulativeCGPA;
    private Date generatedDate;
    private String filePath;

    /**
     * Generates the complete academic report content.
     * @return formatted report as a string
     */
    public String generateReport() {
        // To be implemented
        return null;
    }

    /**
     * Exports the academic report to a PDF file using iText library.
     */
    public void exportToPDF() {
        // To be implemented
    }

    /**
     * Calculates the GPA for a specific semester.
     * @return the semester GPA value
     */
    public double calculateSemesterGPA() {
        // To be implemented
        return 0.0;
    }

    /**
     * Calculates the cumulative GPA (CGPA) across all semesters.
     * @return the cumulative CGPA value
     */
    public double calculateCGPA() {
        // To be implemented
        return 0.0;
    }

    /**
     * Includes a grade record in the report.
     * @param grade the grade to include
     */
    public void includeGrade(Grade grade) {
        // To be implemented
    }

    // Getters and setters to be implemented
}
