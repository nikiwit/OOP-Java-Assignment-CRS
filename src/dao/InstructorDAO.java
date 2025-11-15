package dao;

import models.Instructor;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Instructor entities.
 * Handles CRUD operations for instructor data stored in text files.
 * Implements data persistence layer for the Instructor model.
 */
public class InstructorDAO {
    private static final String INSTRUCTORS_FILE = "instructors.txt";
    private static final String DELIMITER = ",";
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public InstructorDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public InstructorDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Saves an instructor to the data file.
     * @param instructor the instructor to save
     */
    public void saveInstructor(Instructor instructor) {
        if (instructor == null || instructor.getUserId() == null) {
            throw new IllegalArgumentException("Instructor and instructor ID cannot be null");
        }

        // Check if instructor already exists
        Instructor existing = loadInstructor(instructor.getUserId());
        if (existing != null) {
            updateInstructor(instructor);
            return;
        }

        // Format: userId,email,password,instructorName,assignedCourseIds
        String courseIds = String.join(";", instructor.getAssignedCourseIds());
        String data = String.format("%s%s%s%s%s%s%s%s%s",
                instructor.getUserId(), DELIMITER,
                instructor.getEmail(), DELIMITER,
                instructor.getPassword(), DELIMITER,
                instructor.getInstructorName(), DELIMITER,
                courseIds);

        fileManager.appendToFile(INSTRUCTORS_FILE, data);
    }

    /**
     * Loads an instructor by ID from the data file.
     * @param instructorId the instructor ID
     * @return the Instructor object, or null if not found
     */
    public Instructor loadInstructor(String instructorId) {
        if (instructorId == null || instructorId.trim().isEmpty()) {
            return null;
        }

        String content = fileManager.loadFromTextFile(INSTRUCTORS_FILE);
        if (content.isEmpty()) {
            return null;
        }

        String[] lines = content.split("\n");
        boolean isFirstLine = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Skip header line
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 2 && parts[0].trim().equals(instructorId)) {
                return parseInstructor(parts);
            }
        }

        return null;
    }

    /**
     * Loads all instructors from the data file.
     * @return list of all instructors
     */
    public List<Instructor> loadAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        String content = fileManager.loadFromTextFile(INSTRUCTORS_FILE);

        if (content.isEmpty()) {
            return instructors;
        }

        String[] lines = content.split("\n");
        boolean isFirstLine = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Skip header line
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 2) {
                Instructor instructor = parseInstructor(parts);
                if (instructor != null) {
                    instructors.add(instructor);
                }
            }
        }

        return instructors;
    }

    /**
     * Updates an existing instructor's information.
     * @param instructor the instructor with updated data
     */
    public void updateInstructor(Instructor instructor) {
        if (instructor == null || instructor.getUserId() == null) {
            throw new IllegalArgumentException("Instructor and instructor ID cannot be null");
        }

        List<Instructor> instructors = loadAllInstructors();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (Instructor existing : instructors) {
            if (existing.getUserId().equals(instructor.getUserId())) {
                // Update with new data
                String courseIds = String.join(";", instructor.getAssignedCourseIds());
                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%s\n",
                        instructor.getUserId(), DELIMITER,
                        instructor.getEmail(), DELIMITER,
                        instructor.getPassword(), DELIMITER,
                        instructor.getInstructorName(), DELIMITER,
                        courseIds));
                found = true;
            } else {
                // Keep existing data
                String courseIds = String.join(";", existing.getAssignedCourseIds());
                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%s\n",
                        existing.getUserId(), DELIMITER,
                        existing.getEmail(), DELIMITER,
                        existing.getPassword(), DELIMITER,
                        existing.getInstructorName(), DELIMITER,
                        courseIds));
            }
        }

        if (!found) {
            throw new IllegalStateException("Instructor not found: " + instructor.getUserId());
        }

        fileManager.saveToTextFile(INSTRUCTORS_FILE, updatedContent.toString());
    }

    /**
     * Deletes an instructor from the data file.
     * @param instructorId the instructor ID to delete
     */
    public void deleteInstructor(String instructorId) {
        if (instructorId == null || instructorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor ID cannot be null or empty");
        }

        List<Instructor> instructors = loadAllInstructors();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (Instructor instructor : instructors) {
            if (!instructor.getUserId().equals(instructorId)) {
                String courseIds = String.join(";", instructor.getAssignedCourseIds());
                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%s\n",
                        instructor.getUserId(), DELIMITER,
                        instructor.getEmail(), DELIMITER,
                        instructor.getPassword(), DELIMITER,
                        instructor.getInstructorName(), DELIMITER,
                        courseIds));
            } else {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("Instructor not found: " + instructorId);
        }

        fileManager.saveToTextFile(INSTRUCTORS_FILE, updatedContent.toString());
    }

    /**
     * Parses a CSV line into an Instructor object.
     * Format: InstructorID,InstructorName
     * @param parts the CSV parts
     * @return the Instructor object
     */
    private Instructor parseInstructor(String[] parts) {
        try {
            String userId = parts[0].trim();
            String instructorName = parts[1].trim();

            // Create instructor with minimal data (email and password can be empty for now)
            String email = userId.toLowerCase() + "@university.edu";
            String password = "password123"; // Default password

            Instructor instructor = new Instructor(userId, email, password, instructorName);

            return instructor;
        } catch (Exception e) {
            System.err.println("Error parsing instructor: " + e.getMessage());
            return null;
        }
    }
}
