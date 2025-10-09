package models;

/**
 * Represents the mapping between a major and its required courses.
 * Tracks which courses are required for specific majors in specific years and semesters.
 */
public class MajorCourse {
    private String major;
    private int year;
    private int semester;
    private String courseId;

    /**
     * Gets the Course object associated with this major course mapping.
     * @return the Course object
     */
    public Course getCourse() {
        // To be implemented
        return null;
    }

    /**
     * Checks if this course is required for a given major, year, and semester.
     * @param major the major to check
     * @param year the year to check
     * @param semester the semester to check
     * @return true if required, false otherwise
     */
    public boolean isRequiredForMajor(String major, int year, int semester) {
        // To be implemented
        return false;
    }

    // Getters and setters to be implemented
}
