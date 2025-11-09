package dao;

import models.Course;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course entities.
 * Handles CRUD operations for course data stored in text files.
 * Implements data persistence layer for the Course model.
 */
public class CourseDAO {
    private static final String COURSES_FILE = "courses.txt";
    private static final String DELIMITER = ",";
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public CourseDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public CourseDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Saves a course to the data file.
     * @param course the course to save
     */
    public void saveCourse(Course course) {
        if (course == null || course.getCourseId() == null) {
            throw new IllegalArgumentException("Course and course ID cannot be null");
        }

        // Format: CourseID,CourseName,Credits,Semester,Capacity,InstructorID
        String data = String.format("%s%s%s%s%d%s%d%s%s",
                course.getCourseId(), DELIMITER,
                course.getCourseName(), DELIMITER,
                course.getCredits(), DELIMITER,
                course.getCapacity(), DELIMITER,
                course.getInstructorId());

        fileManager.appendToFile(COURSES_FILE, data);
    }

    /**
     * Loads a course by ID from the data file.
     * @param courseId the course ID
     * @return the Course object, or null if not found
     */
    public Course loadCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return null;
        }

        String content = fileManager.loadFromTextFile(COURSES_FILE);
        if (content.isEmpty()) {
            return null;
        }

        String[] lines = content.split("\n");
        for (int i = 1; i < lines.length; i++) { // Skip header line
            String line = lines[i];
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 6 && parts[0].equals(courseId)) {
                return parseCourse(parts);
            }
        }

        return null;
    }

    /**
     * Loads all courses from the data file.
     * @return list of all courses
     */
    public List<Course> loadAllCourses() {
        List<Course> courses = new ArrayList<>();
        String content = fileManager.loadFromTextFile(COURSES_FILE);

        if (content.isEmpty()) {
            return courses;
        }

        String[] lines = content.split("\n");
        for (int i = 1; i < lines.length; i++) { // Skip header line
            String line = lines[i];
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 6) {
                Course course = parseCourse(parts);
                if (course != null) {
                    courses.add(course);
                }
            }
        }

        return courses;
    }

    /**
     * Updates an existing course's information.
     * @param course the course with updated data
     */
    public void updateCourse(Course course) {
        if (course == null || course.getCourseId() == null) {
            throw new IllegalArgumentException("Course and course ID cannot be null");
        }

        List<Course> courses = loadAllCourses();
        StringBuilder updatedContent = new StringBuilder();
        updatedContent.append("CourseID,CourseName,Credits,Semester,Capacity,InstructorID\n");

        boolean found = false;
        for (Course existing : courses) {
            if (existing.getCourseId().equals(course.getCourseId())) {
                updatedContent.append(String.format("%s%s%s%s%d%s%d%s%s\n",
                        course.getCourseId(), DELIMITER,
                        course.getCourseName(), DELIMITER,
                        course.getCredits(), DELIMITER,
                        course.getCapacity(), DELIMITER,
                        course.getInstructorId()));
                found = true;
            } else {
                updatedContent.append(String.format("%s%s%s%s%d%s%d%s%s\n",
                        existing.getCourseId(), DELIMITER,
                        existing.getCourseName(), DELIMITER,
                        existing.getCredits(), DELIMITER,
                        existing.getCapacity(), DELIMITER,
                        existing.getInstructorId()));
            }
        }

        if (!found) {
            throw new IllegalStateException("Course not found: " + course.getCourseId());
        }

        fileManager.saveToTextFile(COURSES_FILE, updatedContent.toString());
    }

    /**
     * Loads all courses assigned to a specific instructor.
     * @param instructorId the instructor ID
     * @return list of courses
     */
    public List<Course> loadCoursesByInstructor(String instructorId) {
        List<Course> instructorCourses = new ArrayList<>();

        if (instructorId == null || instructorId.trim().isEmpty()) {
            return instructorCourses;
        }

        List<Course> allCourses = loadAllCourses();
        for (Course course : allCourses) {
            if (instructorId.equals(course.getInstructorId())) {
                instructorCourses.add(course);
            }
        }

        return instructorCourses;
    }

    /**
     * Parses a CSV line into a Course object.
     * Format: CourseID,CourseName,Credits,Semester,Capacity,InstructorID
     * @param parts the CSV parts
     * @return the Course object
     */
    private Course parseCourse(String[] parts) {
        try {
            String courseId = parts[0].trim();
            String courseName = parts[1].trim();
            int credits = Integer.parseInt(parts[2].trim());
            // Skip semester (parts[3]) as it's not in Course model
            int capacity = Integer.parseInt(parts[4].trim());
            String instructorId = parts[5].trim();

            return new Course(courseId, courseName, credits, capacity, instructorId);
        } catch (Exception e) {
            System.err.println("Error parsing course: " + e.getMessage());
            return null;
        }
    }
}
