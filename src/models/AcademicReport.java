package models;

import dao.CourseDAO;
import dao.GradeDAO;
import dao.ResultDAO;
import dao.StudentDAO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
        StringBuilder report = new StringBuilder();

        // Basic info
        report.append("Report ID: ").append(reportId).append("\n")
                .append("Student ID: ").append(studentId).append("\n")
                .append("Name: ").append(getStudent() != null ? getStudent().getFullName() : "N/A").append("\n")
                .append("Semester: ").append(getSemesterId() != null ? getSemesterId() : "N/A").append("\n")
                .append("Course Code   Course Title   Credit Hours   Grade   Grade Point\n")
                .append("--------------------------------------------------------------\n");

        // Get results and courses
        List<Result> results = getResults();
        List<Course> courses = getCourses();

        if (results != null && courses != null) {
            for (Result r : results) {
                Course course = courses.stream()
                        .filter(c -> c.getCourseId().equals(r.getCourseId()))
                        .findFirst()
                        .orElse(null);
                if (course != null) {
                    report.append(String.format("%-12s %-15s %-13d %-6s %-11.2f\n",
                            course.getCourseId(), 
                            course.getCourseName(), 
                            course.getCredits(), 
                            r.getGrade(),
                            r.getGradePoint()));
                }
            }
        } else {
            report.append("No results available for this semester.\n");
        }

        // GPA info
        report.append("\nSemester GPA: ").append(String.format("%.2f", semesterGPA)).append("\n")
                .append("Cumulative CGPA: ").append(String.format("%.2f", cumulativeCGPA)).append("\n");

        // Grade records (optional if you want full grade details)

        return report.toString();
    }

    /**
     * Exports the academic report to a PDF file using iText library.
     */
    public void exportToPDF() {
        Document document = new Document();

        try {
            String defaultDir = "reports";
            String path = (getFilePath() != null && !getFilePath().isEmpty())
                    ? getFilePath() : defaultDir + File.separator + "AcademicReport_"
                    + getStudentId() + "_" + getSemesterId() + ".pdf";

            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // Title and Header Info
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            Paragraph title = new Paragraph("ACADEMIC REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Student Information
            Student student = getStudent();
            document.add(new Paragraph("Report ID: " + reportId, normalFont));
            document.add(new Paragraph("Student ID: " + studentId, normalFont));
            document.add(new Paragraph("Name: " + (student != null ? student.getFullName() : "N/A"), normalFont));
            document.add(new Paragraph("Semester: " + (getSemesterId() != null ? getSemesterId() : "N/A"), normalFont));
            document.add(new Paragraph("\n"));

            // Course Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Set column widths
            float[] columnWidths = {2f, 3f, 2f, 1.5f, 2f};
            table.setWidths(columnWidths);

            // Table Headers
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            String[] headers = {"Course Code", "Course Title", "Credit Hours", "Grade", "Grade Point"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
                table.addCell(cell);
            }

            // Get results and courses
            List<Result> results = getResults();
            List<Course> courses = getCourses();

            if (results != null && courses != null) {
                for (Result r : results) {
                    Course course = courses.stream()
                            .filter(c -> c.getCourseId().equals(r.getCourseId()))
                            .findFirst()
                            .orElse(null);
                    if (course != null) {
                        // Course Code
                        PdfPCell codeCell = new PdfPCell(new Phrase(course.getCourseId(), normalFont));
                        codeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        codeCell.setPadding(5);
                        table.addCell(codeCell);

                        // Course Title
                        PdfPCell titleCell = new PdfPCell(new Phrase(course.getCourseName(), normalFont));
                        titleCell.setPadding(5);
                        table.addCell(titleCell);

                        // Credit Hours
                        PdfPCell creditsCell = new PdfPCell(new Phrase(String.valueOf(course.getCredits()), normalFont));
                        creditsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        creditsCell.setPadding(5);
                        table.addCell(creditsCell);

                        // Grade
                        PdfPCell gradeCell = new PdfPCell(new Phrase(r.getGrade(), normalFont));
                        gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        gradeCell.setPadding(5);
                        table.addCell(gradeCell);

                        // Grade Point
                        PdfPCell pointCell = new PdfPCell(new Phrase(String.format("%.2f", r.getGradePoint()), normalFont));
                        pointCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pointCell.setPadding(5);
                        table.addCell(pointCell);
                    }
                }
            } else {
                PdfPCell noDataCell = new PdfPCell(new Phrase("No results available for this semester.", normalFont));
                noDataCell.setColspan(5);
                noDataCell.setPadding(10);
                noDataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(noDataCell);
            }

            document.add(table);

            // GPA Summary
            document.add(new Paragraph("\n"));
            Font summaryFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            document.add(new Paragraph("Semester GPA: " + String.format("%.2f", semesterGPA), summaryFont));
            document.add(new Paragraph("Cumulative CGPA: " + String.format("%.2f", cumulativeCGPA), summaryFont));

            // Footer
            Date generatedDate = getGeneratedDate();
            if (generatedDate == null) {
                generatedDate = new Date();
                setGeneratedDate(generatedDate);
            }

            String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(generatedDate);
            Paragraph footer = new Paragraph("\nReport generated on: " + formattedDate, normalFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

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
         String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(new java.util.Date());
        this.reportId = getStudentId() + "_" + getSemesterId() + "_" + timestamp;
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
        this.generatedDate = generateDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}