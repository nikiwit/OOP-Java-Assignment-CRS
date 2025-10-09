package dao;

import models.Grade;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for Grade entities.
 * Handles CRUD operations for grade data stored in text files.
 * Implements data persistence layer for the Grade model.
 */
public class GradeDAO {
    private FileManager fileManager;

    /**
     * Saves a grade to the data file.
     * @param grade the grade to save
     */
    public void saveGrade(Grade grade) {
        // To be implemented
    }

    /**
     * Loads a grade by ID from the data file.
     * @param gradeId the grade ID
     * @return the Grade object, or null if not found
     */
    public Grade loadGrade(String gradeId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all grades for a specific student.
     * @param studentId the student ID
     * @return list of grades
     */
    public List<Grade> loadGradesByStudent(String studentId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all grades for a specific course.
     * @param courseId the course ID
     * @return list of grades
     */
    public List<Grade> loadGradesByCourse(String courseId) {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing grade record.
     * @param grade the grade with updated data
     */
    public void updateGrade(Grade grade) {
        // To be implemented
    }

    // Additional helper methods to be implemented
}
