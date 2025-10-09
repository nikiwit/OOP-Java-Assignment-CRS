package dao;

import models.Student;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for Student entities.
 * Handles CRUD operations for student data stored in text files.
 * Implements data persistence layer for the Student model.
 */
public class StudentDAO {
    private FileManager fileManager;

    /**
     * Saves a student to the data file.
     * @param student the student to save
     */
    public void saveStudent(Student student) {
        // To be implemented
    }

    /**
     * Loads a student by ID from the data file.
     * @param studentId the student ID
     * @return the Student object, or null if not found
     */
    public Student loadStudent(String studentId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all students from the data file.
     * @return list of all students
     */
    public List<Student> loadAllStudents() {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing student's information.
     * @param student the student with updated data
     */
    public void updateStudent(Student student) {
        // To be implemented
    }

    /**
     * Deletes a student from the data file.
     * @param studentId the student ID to delete
     */
    public void deleteStudent(String studentId) {
        // To be implemented
    }

    // Additional helper methods to be implemented
}
