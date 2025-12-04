package models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import dao.StudentDAO;
import dao.GradeDAO;
import dao.ResultDAO;
import dao.CourseDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Represents an academic performance report for a student. Generates
 * comprehensive reports including grades, GPA calculations, and exports to PDF
 * format as required by the assignment.
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
     *
     * @return formatted report as a string
     */
    public String generateReport() {
        String name = getStudent().getFullName();

        StringBuilder report = new StringBuilder();
        report.append("Report ID: ").append(reportId)
                .append("\nStudent ID: ").append(studentId)
                .append("\nName: ").append(name)
                .append("\nSemester ID: ").append(semesterId)
                .append("\nSemester GPA: ").append(semesterGPA)
                .append("\nCumulative CGPA: ").append(cumulativeCGPA)
                .append("\n\n=== Grade Records ===\n");
        report.append(getGrades());
        report.append(getResults());
        report.append(getClass());

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
                    ? getFilePath() : defaultDir + File.separator + "AcademicReport_"
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
     *
     * @return the semester GPA value
     */
    public double calculateSemesterGPA() {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        List<Result> results = resultDAO.loadResultsByStudent(studentId);
        List<Result> semesterResults = new ArrayList<>();

        for (Result result : results) {
            if (result.getSemesterId().equals(semesterId)) {
                semesterResults.add(result);
            }
        }

        if (semesterResults == null) {
            return 0.0;
        }

        double totalGrade = 0.0;

        for (Result result : semesterResults) {
            totalGrade += result.getGradePoint();
        }

        semesterGPA = totalGrade / semesterResults.size();
        setSemesterGPA(semesterGPA);
        return semesterGPA;
    }

    /**
     * Calculates the cumulative GPA (CGPA) across all semesters.
     *
     * @return the cumulative CGPA value
     */
    public double calculateCGPA() {
        dao.ResultDAO resultDAO = new dao.ResultDAO();
        List<Result> results = resultDAO.loadResultsByStudent(studentId);
        if (results == null) {
            return 0.0;
        }

        double totalGrade = 0.0;

        for (Result result : results) {
            totalGrade += result.getGradePoint();
        }

        cumulativeCGPA = totalGrade / results.size();
        setCumulativeCGPA(cumulativeCGPA);
        return cumulativeCGPA;
    }

    /**
     * Gets the student for this report.
     *
     * @return the Student object
     */
    public Student getStudent() {
        StudentDAO dao = new StudentDAO();
        return dao.loadStudent(studentId);
    }

    /**
     * Gets all grades for this report.
     *
     * @return list of grades
     */
    public java.util.List<Grade> getGrades() {
        GradeDAO dao = new GradeDAO();
        List<Grade> grades = dao.loadGradesByStudent(studentId);
        if (grades == null) {
            return null;
        }
        List<Grade> semesterGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getSemesterId().equals(semesterId)) {
                semesterGrades.add(grade);
            }
        }
        return semesterGrades;
    }

    /**
     * Gets all results for this report.
     *
     * @return list of results
     */
    public java.util.List<Result> getResults() {
        ResultDAO dao = new ResultDAO();
        List<Result> results = dao.loadResultsByStudent(studentId);
        if (results == null) {
            return null;
        }
        List<Result> semesterResults = new ArrayList<>();
        for (Result result : results) {
            if (result.getSemesterId().equals(semesterId)) {
                semesterResults.add(result);
            }
        }
        return semesterResults;
    }

    public java.util.List<Course> getCourses() {
        CourseDAO dao = new CourseDAO();

        List<Course> courses = dao.loadAllCourses();
        if (courses == null) {
            return null;
        }
        List<Course> semesterCourses = new ArrayList<>();
        for (Course course : courses) {
            for (Result r : getResults()) {
                if (course.getCourseId().equals(r.getCourseId())) {
                    semesterCourses.add(course);
                    break; // avoid duplicates
                }
            }
        }
        return semesterCourses;
    }

    // Getters and setters to be implemented
    public String getReportId() {
        return reportId;
    }

    //report id will not set from other class
    public void setReportId() {
        String randomDigits = String.format("%04d", (int) (Math.random() * 10000));
        this.reportId = getStudentId() + "_" + getSemesterId() + "_" + randomDigits;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public double getSemesterGPA() {
        return semesterGPA;
    }

    public void setSemesterGPA(double semesterGPA) {
        this.semesterGPA = semesterGPA;
    }

    public double getCumulativeCGPA() {
        return cumulativeCGPA;
    }

    public void setCumulativeCGPA(double cumulativeCGPA) {
        this.cumulativeCGPA = cumulativeCGPA;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Date generateDate) {
        this.generatedDate = generatedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
