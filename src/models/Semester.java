package models;

import java.util.Date;

/**
 * Represents an academic semester in the system. Contains semester
 * identification (YYYYMM format), naming, and date range information.
 */
public class Semester {

    private String semesterId;      // Format: YYYYMM (e.g., 202501, 202505, 202509)
    private String semesterName;    // E.g., "Spring 2025", "Summer 2025", "Fall 2025"
    private Date startDate;
    private Date endDate;

    /**
     * Returns a string representation of the semester.
     *
     * @return formatted semester information
     */
    @Override
    public String toString() {
        return semesterName;
    }

    /**
     * Checks if this semester is the current active semester.
     *
     * @return true if current date is within semester date range
     */
    public boolean isCurrentSemester() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }

    // Getters and setters to be implemented
    // Getter and setter for semesterId
    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

// Getter and setter for semesterName
    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

// Getter and setter for startDate
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

// Getter and setter for endDate
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
