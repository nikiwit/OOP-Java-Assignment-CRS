package dao;

import models.Grade;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for Grade entities - GradeDAO
 */
public class GradeDAO {
    private FileManager fileManager;
    public void saveGrade(Grade grade) {
        // To be implemented
    }

    /**
     * Loads a grade by ID from the data file.
     */
    public Grade loadGrade(String gradeId) {
        // To be implemented
        return null;
    }

    public List<Grade> loadGradesByStudent(String studentId) {
        // To be implemented
        return null;
    }

    public List<Grade> loadGradesByCourse(String courseId) {
        // To be implemented
        return null;
    }

    public void updateGrade(Grade grade) {
        // To be implemented
    }

    // Additional helper methods to be implemented
}
