package dao;

import models.Instructor;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for Instructor entities.
 * Handles CRUD operations for instructor data stored in text files.
 * Implements data persistence layer for the Instructor model.
 */
public class InstructorDAO {
    private FileManager fileManager;

    /**
     * Saves an instructor to the data file.
     * @param instructor the instructor to save
     */
    public void saveInstructor(Instructor instructor) {
        // To be implemented
    }

    /**
     * Loads an instructor by ID from the data file.
     * @param instructorId the instructor ID
     * @return the Instructor object, or null if not found
     */
    public Instructor loadInstructor(String instructorId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all instructors from the data file.
     * @return list of all instructors
     */
    public List<Instructor> loadAllInstructors() {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing instructor's information.
     * @param instructor the instructor with updated data
     */
    public void updateInstructor(Instructor instructor) {
        // To be implemented
    }

    // Additional helper methods to be implemented
}
