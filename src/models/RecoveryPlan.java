package models;

import dao.CourseDAO;
import dao.InstructorDAO;
import dao.StudentDAO;
import enums.RecoveryStatus;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a course recovery plan for a student who failed a course.
 * Contains recovery tasks, timeline, status tracking, and instructor notes.
 * Manages the recovery process from initiation to completion.
 */
public class RecoveryPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String planId;
    private String studentId;
    private String courseId;
    private String instructorId;
    private int currentActionNumber;
    private RecoveryStatus status;
    private String notes;
    private Date startDate;
    private Date targetEndDate;
    private Date completedDate;

    /**
     * Default constructor.
     */
    public RecoveryPlan() {
        this.currentActionNumber = 1;
        this.status = RecoveryStatus.PENDING;
    }

    /**
     * Constructor with all required fields.
     *
     * @param planId unique plan identifier
     * @param studentId student ID
     * @param courseId course ID
     * @param instructorId instructor ID
     */
    public RecoveryPlan(String planId, String studentId, String courseId, String instructorId) {
        this.planId = planId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.currentActionNumber = 1;
        this.status = RecoveryStatus.PENDING;
        this.startDate = new Date();
    }

    /**
     * Updates the current action number in the recovery plan.
     * @param actionNumber the new action number (1-4)
     */
    public void updateActionNumber(int actionNumber) {
        if (actionNumber < 1 || actionNumber > 4) {
            throw new IllegalArgumentException("Action number must be between 1 and 4");
        }
        this.currentActionNumber = actionNumber;
    }

    /**
     * Updates the status of the recovery plan.
     * @param status the new RecoveryStatus value
     */
    public void updateStatus(RecoveryStatus status) {
        this.status = status;
        if (status == RecoveryStatus.COMPLETED) {
            this.completedDate = new Date();
        }
    }

    /**
     * Adds instructor notes to the recovery plan.
     * @param notes additional notes to append
     */
    public void addNotes(String notes) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = notes;
        } else {
            this.notes += "\n" + notes;
        }
    }

    /**
     * Checks if the recovery plan is overdue.
     * @return true if current date is past target end date and not completed
     */
    public boolean isOverdue() {
        if (status == RecoveryStatus.COMPLETED || targetEndDate == null) {
            return false;
        }
        Date now = new Date();
        return now.after(targetEndDate);
    }

    /**
     * Checks if the recovery plan has been completed.
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return status == RecoveryStatus.COMPLETED;
    }

    /**
     * Gets the student associated with this recovery plan.
     * @return the Student object
     */
    public Student getStudent() {
        if (studentId == null) {
            return null;
        }
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.loadStudent(studentId);
    }

    /**
     * Gets the course associated with this recovery plan.
     * @return the Course object
     */
    public Course getCourse() {
        if (courseId == null) {
            return null;
        }
        CourseDAO courseDAO = new CourseDAO();
        return courseDAO.loadCourse(courseId);
    }

    /**
     * Gets the instructor managing this recovery plan.
     * @return the Instructor object
     */
    public Instructor getInstructor() {
        if (instructorId == null) {
            return null;
        }
        InstructorDAO instructorDAO = new InstructorDAO();
        return instructorDAO.loadInstructor(instructorId);
    }

    // Getters and Setters

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
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

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public int getCurrentActionNumber() {
        return currentActionNumber;
    }

    public void setCurrentActionNumber(int currentActionNumber) {
        this.currentActionNumber = currentActionNumber;
    }

    public RecoveryStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
