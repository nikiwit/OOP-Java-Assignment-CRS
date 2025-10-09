package dao;

import models.MajorStudent;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for MajorStudent mapping entities.
 * Handles CRUD operations for student-major mapping data stored in text files.
 * Implements data persistence layer for the MajorStudent model.
 */
public class MajorStudentDAO {
    private FileManager fileManager;

    /**
     * Saves a student-major mapping to the data file.
     * @param ms the student-major mapping to save
     */
    public void saveMajorStudent(MajorStudent ms) {
        // To be implemented
    }

    /**
     * Loads a student-major mapping by student ID.
     * @param studentId the student ID
     * @return the MajorStudent object, or null if not found
     */
    public MajorStudent loadMajorStudent(String studentId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all student-major mappings from the data file.
     * @return list of all student-major mappings
     */
    public List<MajorStudent> loadAllMajorStudents() {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing student-major mapping.
     * @param ms the mapping with updated data
     */
    public void updateMajorStudent(MajorStudent ms) {
        // To be implemented
    }

    // Additional helper methods to be implemented
}
