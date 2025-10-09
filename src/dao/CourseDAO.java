package dao;

import models.Course;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for Course entities.
 * Handles CRUD operations for course data stored in text files.
 * Implements data persistence layer for the Course model.
 */
public class CourseDAO {
    private FileManager fileManager;

    /**
     * Saves a course to the data file.
     * @param course the course to save
     */
    public void saveCourse(Course course) {
        // To be implemented
    }

    /**
     * Loads a course by ID from the data file.
     * @param courseId the course ID
     * @return the Course object, or null if not found
     */
    public Course loadCourse(String courseId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all courses from the data file.
     * @return list of all courses
     */
    public List<Course> loadAllCourses() {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing course's information.
     * @param course the course with updated data
     */
    public void updateCourse(Course course) {
        // To be implemented
    }

    /**
     * Loads all courses assigned to a specific instructor.
     * @param instructorId the instructor ID
     * @return list of courses
     */
    public List<Course> loadCoursesByInstructor(String instructorId) {
        // To be implemented
        return null;
    }

    // Additional helper methods to be implemented
}
