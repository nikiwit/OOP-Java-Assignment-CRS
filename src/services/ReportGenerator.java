package services;

import models.Student;
import models.AcademicReport;
import dao.CourseDAO;
import dao.ResultDAO;
import dao.SemesterDAO;
import dao.StudentDAO;

/**
 * Singleton service for generating academic reports and exporting them to PDF.
 * Uses iText library to create formatted PDF documents.
 * Implements the Singleton design pattern to ensure only one instance exists.
 */
public class ReportGenerator {
    private static ReportGenerator instance;
    private CourseDAO courseDao;
    private ResultDAO resultDao;
    private SemesterDAO semesterDao;
    private StudentDAO studentDao;
    /**
     * Private constructor to prevent direct instantiation.
     * Part of the Singleton pattern implementation.
     */
    private ReportGenerator() {
        // To be implemented
        this.courseDao=new CourseDAO();
        this.resultDao=new ResultDAO();
        this.semesterDao=new SemesterDAO();
        this.studentDao=new StudentDAO();
    }

    /**
     * Gets the single instance of ReportGenerator.
     * Creates the instance if it doesn't exist (lazy initialization).
     * @return the singleton instance
     */
    public static ReportGenerator getInstance() {
        // To be implemented
            if (instance == null) {
            instance = new ReportGenerator();
        }
        return instance;
    }

    /**
     * Generates a comprehensive academic report for a student.
     * @param student the student
     * @param semesterId the semester identifier (YYYYMM format)
     * @return the generated AcademicReport object
     */
    public AcademicReport generateAcademicReport(Student student, String semesterId) {
        // To be implemented
        AcademicReport report = new AcademicReport();

        report.setStudentId(student.getStudentId());
        report.setSemesterId(semesterId);
        report.setReportId();
        report.setGeneratedDate(new java.util.Date());
        report.calculateSemesterGPA();
        report.calculateCGPA();

        return report;
    }

    /**
     * Exports an academic report to a PDF file using iText library.
     * @param report the report to export
     * @return the file path of the generated PDF
     */
    public String exportReportToPDF(AcademicReport report) {
        if (report == null) {
            System.out.println("Error: Report is null. Cannot export PDF.");
            return null;
        }

        try {
            report.exportToPDF();
            System.out.println("PDF successfully generated at: " + report.getFilePath());
            return report.getFilePath();
        } catch (Exception e) {
            System.out.println("Error exporting report to PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Additional helper methods to be implemented
}
