package models;

/**
 * Represents the mapping between a student and their major program details.
 * Tracks student's current year, semester, and retake status within their major.
 */
public class MajorStudent {
    private String studentId;
    private String major;
    private int year;
    private int semester;
    private boolean retake;

    /**
     * Checks if the student needs to retake courses.
     * @return true if retake is needed, false otherwise
     */
    public boolean needsRetake() {
        // To be implemented
        return retake;
    }

    /**
     * Gets the Student object associated with this mapping.
     * @return the Student object
     */
    public Student getStudent() {
        // To be implemented
        return null;
    }

    // Getters and setters to be implemented
}
