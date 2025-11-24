package models;

import dao.CourseDAO;
import dao.InstructorDAO;
import dao.StudentDAO;
import enums.RecoveryStatus;
import java.io.Serializable;
import java.util.Date;

// Represents a course recovery plan for a student who failed a course.

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


    public RecoveryPlan() {
        this.currentActionNumber = 1;
        this.status = RecoveryStatus.PENDING;
    }


    // Constructor with all required fields.

    public RecoveryPlan(String planId, String studentId, String courseId, String instructorId) {
        this.planId = planId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.currentActionNumber = 1;
        this.status = RecoveryStatus.PENDING;
        this.startDate = new Date();
    }

    
    public void updateActionNumber(int actionNumber) {
        if (actionNumber < 1 || actionNumber > 4) {
            throw new IllegalArgumentException("Action number must be between 1 and 4");
        }
        this.currentActionNumber = actionNumber;
    }

    public void updateStatus(RecoveryStatus status) {
        this.status = status;
        if (status == RecoveryStatus.COMPLETED) {
            this.completedDate = new Date();
        }
    }

    public void addNotes(String notes) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = notes;
        } else {
            this.notes += "\n" + notes;
        }
    }

    public boolean isOverdue() {
        if (status == RecoveryStatus.COMPLETED || targetEndDate == null) {
            return false;
        }
        Date now = new Date();
        return now.after(targetEndDate);
    }

    public boolean isCompleted() {
        return status == RecoveryStatus.COMPLETED;
    }

    public Student getStudent() {
        if (studentId == null) {
            return null;
        }
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.loadStudent(studentId);
    }

    public Course getCourse() {
        if (courseId == null) {
            return null;
        }
        CourseDAO courseDAO = new CourseDAO();
        return courseDAO.loadCourse(courseId);
    }

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
