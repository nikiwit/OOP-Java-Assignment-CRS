package models;

import dao.CourseDAO;
import enums.ComponentType;
import enums.GradeStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents the overall result for a course, combining all component grades.
 * Tracks whether both assignment and exam components have been passed,
 * and determines if recovery is needed.
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resultId;
    private String studentId;
    private String courseId;
    private String courseName;
    private int attemptNumber;
    private String grade;
    private double gradePoint;
    private boolean passedExam;
    private boolean passedAssignment;
    private GradeStatus status;
    private String semesterId;
    private Date resultDate;

    /**
     * Default constructor.
     */
    public Result() {
        this.attemptNumber = 1;
        this.passedExam = false;
        this.passedAssignment = false;
        this.status = GradeStatus.TRANSIT;
    }

    /**
     * Constructor with required fields.
     *
     * @param resultId unique result identifier
     * @param studentId student ID
     * @param courseId course ID
     * @param passedExam whether exam was passed
     * @param passedAssignment whether assignment was passed
     */
    public Result(String resultId, String studentId, String courseId, boolean passedExam, boolean passedAssignment) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.passedExam = passedExam;
        this.passedAssignment = passedAssignment;
        this.attemptNumber = 1;
        this.status = isPassed() ? GradeStatus.PASSED : GradeStatus.FAILED;
        this.resultDate = new Date();
    }

    /**
     * Checks if the student passed the overall course.
     * @return true if passed, false otherwise
     */
    public boolean isPassed() {
        return passedExam && passedAssignment;
    }

    /**
     * Determines if the student needs course recovery.
     * @return true if recovery is needed, false otherwise
     */
    public boolean needsRecovery() {
        return !passedExam || !passedAssignment;
    }

    /**
     * Gets a list of failed components (Assignment and/or Exam).
     * @return list of failed ComponentType values
     */
    public List<ComponentType> getFailedComponents() {
        List<ComponentType> failedComponents = new ArrayList<>();

        if (!passedExam) {
            failedComponents.add(ComponentType.EXAM);
        }
        if (!passedAssignment) {
            failedComponents.add(ComponentType.ASSIGNMENT);
        }

        return failedComponents;
    }

    /**
     * Gets the course associated with this result.
     * @return the Course object
     */
    public Course getCourse() {
        if (courseId == null) {
            return null;
        }
        CourseDAO courseDAO = new CourseDAO();
        return courseDAO.loadCourse(courseId);
    }

    // Getters and Setters

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }

    public boolean isPassedExam() {
        return passedExam;
    }

    public void setPassedExam(boolean passedExam) {
        this.passedExam = passedExam;
        updateStatus();
    }

    public boolean isPassedAssignment() {
        return passedAssignment;
    }

    public void setPassedAssignment(boolean passedAssignment) {
        this.passedAssignment = passedAssignment;
        updateStatus();
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

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    /**
     * Updates the status based on exam and assignment results.
     */
    private void updateStatus() {
        if (isPassed()) {
            this.status = GradeStatus.PASSED;
        } else {
            this.status = GradeStatus.FAILED;
        }
    }
}
