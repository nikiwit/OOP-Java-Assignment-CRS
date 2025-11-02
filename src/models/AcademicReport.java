package models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import dao.StudentDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
        Student student = getStudent();
        String name = (student != null) ? student.getFullName() : "undefined";

        StringBuilder report = new StringBuilder();
        report.append("Report ID: ").append(reportId)
            .append("\nStudent ID: ").append(studentId)
            .append("\nName: ").append(name)
            .append("\nSemester ID: ").append(semesterId)
            .append("\nSemester GPA: ").append(semesterGPA)
            .append("\nCumulative CGPA: ").append(cumulativeCGPA)
            .append("\n\n=== Grade Records ===\n");

        List<Grade> grades = getGrades();
        if (grades != null && !grades.isEmpty()) {
            for (int i = 0; i < grades.size(); i++) {
                Grade g = grades.get(i);
                report.append("Course: ").append(g.getCourseName())
                    .append(" | Component: ").append(g.getComponent())
                    .append(" | Grade: ").append(g.getGrade())
                    .append(" | GradePoint: ").append(g.getGradePoint())
                    .append(" | Status: ").append(g.getStatus())
                    .append("\n");
            }
        } else {
            report.append("No grade records found for this semester.\n");
        }

        report.append("\n=== Result Records ===\n");

        List<Result> results = getResults();
        if (results != null && !results.isEmpty()) {
            for (int i = 0; i < results.size(); i++) {
                Result r = results.get(i);
                report.append("Course: ").append(r.getCourseName())
                    .append(" | Grade: ").append(r.getGrade())
                    .append(" | GradePoint: ").append(r.getGradePoint())
                    .append(" | PassedExam: ").append(r.getPassedExam())
                    .append(" | PassedAssignment: ").append(r.getPassedAssignment())
                    .append(" | Status: ").append(r.getStatus())
                    .append("\n");
            }
        } else {
            report.append("No result records found for this semester.\n");
        }

        return report.toString();
    }


    /**
     * Exports the academic report to a PDF file using iText library.
     */
    public void exportToPDF() {
        Document document = new Document();

        try {
            String defaultDir = "C:\\Users\\Public\\Downloads";
            String path = (getFilePath() != null && !getFilePath().isEmpty())
                ? getFilePath(): defaultDir + File.separator + "AcademicReport_"
                + getStudentId() + "_" + getSemesterId() + ".pdf";

            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            String reportContent = generateReport();
            document.add(new Paragraph(reportContent));

            String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(getGeneratedDate());
            document.add(new Paragraph("\nReport generated on: " + formattedDate));

            setFilePath(path);
            System.out.println("PDF successfully exported to: " + path);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
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
    //report id will not set from other class
    public void setReportId() {
            String randomDigits = String.format("%04d", (int)(Math.random() * 10000));
            this.reportId = getStudentId() + "_" + getSemesterId() + "_" + randomDigits;
    }
    
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
