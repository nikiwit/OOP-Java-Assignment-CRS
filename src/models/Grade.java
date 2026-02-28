package models;

import enums.ComponentType;
import enums.GradeStatus;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a grade record for a specific course component (assignment or exam).
 * Tracks individual assessment results and includes attempt number for retakes.
 */
public class Grade implements Serializable {
    private static final long serialVersionUID = 1L;

    private String gradeId;
    private String studentId;
    private String courseId;
    private String courseName;
    private int attemptNumber;
    private ComponentType component;
    private String grade;
    private double gradePoint;
    private GradeStatus status;
    private String semesterId;
    private String instructorId;
    private Date gradedDate;
    private int creditHours;

    /**
     * Default constructor.
     */
    public Grade() {
        this.attemptNumber = 1;
        this.status = GradeStatus.TRANSIT;
    }

    /**
     * Constructor with required fields.
     *
     * @param gradeId unique grade identifier
     * @param studentId student ID
     * @param courseId course ID
     * @param component component type (Assignment or Exam)
     * @param grade letter grade
     */
    public Grade(String gradeId, String studentId, String courseId, ComponentType component, String grade) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.component = component;
        this.grade = grade;
        this.attemptNumber = 1;
        this.gradePoint = calculateGradePoint();
        this.status = this.gradePoint >= 2.0 ? GradeStatus.PASSED : GradeStatus.FAILED;
        this.gradedDate = new Date();
    }

    /**
     * Calculates the grade point value based on the letter grade.
     * @return the grade point on a 4.0 scale
     */
    public double calculateGradePoint() {
        if (grade == null || grade.isEmpty()) {
            return 0.0;
        }

        switch (grade.toUpperCase()) {
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "D+":
                return 1.3;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return 0.0;
        }
    }

    /**
     * Checks if the grade is passing (C or better).
     * @return true if passed, false otherwise
     */
    public boolean isPassed() {
        return gradePoint >= 2.0;
    }

    /**
     * Checks if the grade is failing (F).
     * @return true if failed, false otherwise
     */
    public boolean isFailed() {
        return grade != null && grade.equalsIgnoreCase("F");
    }

    /**
     * Gets the component type (Assignment or Exam).
     * @return the ComponentType enum value
     */
    public ComponentType getComponentType() {
        return component;
    }

    // Getters and Setters

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public ComponentType getComponent() {
        return component;
    }

    public void setComponent(ComponentType component) {
        this.component = component;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
        this.gradePoint = calculateGradePoint();
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }

    public GradeStatus getStatus() {
        return status;
    }

    public void setStatus(GradeStatus status) {
        this.status = status;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public Date getGradedDate() {
        return gradedDate;
    }

    public void setGradedDate(Date gradedDate) {
        this.gradedDate = gradedDate;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }
}
