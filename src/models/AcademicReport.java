package models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import dao.StudentDAO;

/**
 * Represents an academic performance report for a student.
 * Generates comprehensive reports including grades, GPA calculations,
 * and exports to PDF format as required by the assignment.
 */
public class AcademicReport {
    private String reportId;
    private String studentId;
    private String semesterId;
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
        Student student =getStudent();
        String name = (student != null) ? student.getFullName() : "undefined";
        return "Report ID: " + reportId +
               "\nStudent ID: " + studentId +
               "\nName: " + name+
               "\nSemester ID: " + semesterId +
               "\nSemester GPA: " + semesterGPA +
               "\nCumulative CGPA: " + cumulativeCGPA;
        
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
        Student s = getStudent();
        if (s == null || s.getGrades() == null) return 0.0;

        double totalPoints = 0.0;
        double totalCredits = 0.0;

        List<Grade> grades = s.getGrades();
        for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            if (g.getSemesterId().equals(this.semesterId)) {
                totalPoints += g.getGradePoint() * g.getCreditHours();
                totalCredits += g.getCreditHours();
            }
        }

        semesterGPA = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;
        return semesterGPA;
    }

    /**
     * Calculates the cumulative GPA (CGPA) across all semesters.
     * @return the cumulative CGPA value
     */
    public double calculateCGPA() {
        Student s = getStudent();
        if (s == null || s.getGrades() == null) return 0.0;

        double totalPoints = 0.0;
        double totalCredits = 0.0;

        List<Grade> grades = s.getGrades();
        for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            totalPoints += g.getGradePoint() * g.getCreditHours();
            totalCredits += g.getCreditHours();
        }

        cumulativeCGPA = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;
        return cumulativeCGPA;
    }

    /**
     * Gets the student for this report.
     * @return the Student object
     */
    public Student getStudent() {
        StudentDAO dao = new StudentDAO();
        return dao.loadStudent(studentId);
    }

    /**
     * Gets all grades for this report.
     * @return list of grades
     */
    public java.util.List<Grade> getGrades() {
        Student s = getStudent();
        if (s == null || s.getGrades() == null) return null;

        List<Grade> grades = s.getGrades();
        List<Grade> semesterGrades = new ArrayList<>();
         for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            if (g.getSemesterId().equals(this.semesterId)) {
                semesterGrades.add(g);
            }
        }
        return semesterGrades;
    }

    /**
     * Gets all results for this report.
     * @return list of results
     */
    public java.util.List<Result> getResults() {
    Student s = getStudent();
    if (s == null || s.getResults() == null) return null;

    List<Result> results = s.getResults();
    List<Result> semesterResults = new ArrayList<>();

    for (int i = 0; i < results.size(); i++) {
        Result r = results.get(i);
        if (r.getSemesterId().equals(this.semesterId)) {
            semesterResults.add(r);
        }
    }
    return semesterResults; 
    }

    // Getters and setters to be implemented
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSemesterId() { return semesterId; }
    public void setSemesterId(String semesterId) { this.semesterId = semesterId; }

    public double getSemesterGPA() { return semesterGPA; }
    public void setSemesterGPA(double semesterGPA) { this.semesterGPA = semesterGPA; }

    public double getCumulativeCGPA() { return cumulativeCGPA; }
    public void setCumulativeCGPA(double cumulativeCGPA) { this.cumulativeCGPA = cumulativeCGPA; }

    public Date getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(Date generatedDate) { this.generatedDate = generatedDate; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

}
