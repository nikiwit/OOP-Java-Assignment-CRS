package dao;

import models.MajorCourse;
import models.Course;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for MajorCourse mapping entities.
 * Handles CRUD operations for major-course mapping data stored in text files.
 * Implements data persistence layer for the MajorCourse model.
 */
public class MajorCourseDAO {
    private FileManager fileManager;

    /**
     * Saves a major-course mapping to the data file.
     * @param mc the major-course mapping to save
     */
    public void saveMajorCourse(MajorCourse mc) {
        // To be implemented
    }

    /**
     * Loads all courses required for a specific major, year, and semester.
     * @param major the major name
     * @param year the academic year
     * @param semester the semester number
     * @return list of required courses
     */
    public List<Course> loadCoursesByMajor(String major, int year, int semester) {
        // To be implemented
        return null;
    }

    /**
     * Loads all major-course mappings from the data file.
     * @return list of all major-course mappings
     */
    public List<MajorCourse> loadAllMajorCourses() {
        // To be implemented
        return null;
    }

    // Additional helper methods to be implemented
}
