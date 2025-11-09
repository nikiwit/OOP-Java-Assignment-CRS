package dao;

import models.Result;
import enums.GradeStatus;
import utils.FileManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Result entities.
 * Handles CRUD operations for result data stored in text files.
 * Implements data persistence layer for the Result model.
 */
public class ResultDAO {
    private FileManager fileManager;
    private static final String RESULTS_FILE = "data/results.txt";

    /**
     * Constructor initializes FileManager.
     */
    public ResultDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Saves a result to the data file.
     * @param result the result to save
     */
    public void saveResult(Result result) {
        // To be implemented
    }

    /**
     * Loads a result by ID from the data file.
     * @param resultId the result ID
     * @return the Result object, or null if not found
     */
    public Result loadResult(String resultId) {
        List<Result> allResults = loadAllResults();
        for (Result result : allResults) {
            if (result.getResultId().equals(resultId)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Loads all results for a specific student.
     * @param studentId the student ID
     * @return list of results
     */
    public List<Result> loadResultsByStudent(String studentId) {
        List<Result> studentResults = new ArrayList<>();
        List<Result> allResults = loadAllResults();

        for (Result result : allResults) {
            if (result.getStudentId().equals(studentId)) {
                studentResults.add(result);
            }
        }

        return studentResults;
    }

    /**
     * Loads all results from the data file.
     * @return list of all results
     */
    public List<Result> loadAllResults() {
        List<Result> results = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(RESULTS_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse CSV line
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    try {
                        Result result = new Result();
                        result.setResultId(parts[0].trim());
                        result.setStudentId(parts[1].trim());
                        result.setCourseId(parts[2].trim());
                        result.setCourseName(parts[3].trim());
                        result.setAttemptNumber(Integer.parseInt(parts[4].trim()));
                        result.setGrade(parts[5].trim());
                        result.setGradePoint(Double.parseDouble(parts[6].trim()));
                        result.setPassedExam(parts[7].trim().equals("1"));
                        result.setPassedAssignment(parts[8].trim().equals("1"));
                        result.setStatus(GradeStatus.valueOf(parts[9].trim()));
                        result.setSemesterId(parts[10].trim());

                        // Parse date
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = dateFormat.parse(parts[11].trim());
                            result.setResultDate(date);
                        } catch (Exception dateEx) {
                            // If date parsing fails, use current date
                            result.setResultDate(new Date());
                        }

                        results.add(result);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error parsing result line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading results file: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Updates an existing result record.
     * @param result the result with updated data
     */
    public void updateResult(Result result) {
        // To be implemented
    }

    /**
     * Loads results for a specific student and course.
     * @param studentId the student ID
     * @param courseId the course ID
     * @return list of results
     */
    public List<Result> loadResultsByStudentAndCourse(String studentId, String courseId) {
        List<Result> matchingResults = new ArrayList<>();
        List<Result> studentResults = loadResultsByStudent(studentId);

        for (Result result : studentResults) {
            if (result.getCourseId().equals(courseId)) {
                matchingResults.add(result);
            }
        }

        return matchingResults;
    }

    /**
     * Gets the most recent result for a student in a specific course.
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the most recent Result, or null if not found
     */
    public Result getMostRecentResult(String studentId, String courseId) {
        List<Result> results = loadResultsByStudentAndCourse(studentId, courseId);

        if (results.isEmpty()) {
            return null;
        }

        // Find the result with the highest attempt number
        Result mostRecent = results.get(0);
        for (Result result : results) {
            if (result.getAttemptNumber() > mostRecent.getAttemptNumber()) {
                mostRecent = result;
            }
        }

        return mostRecent;
    }

    /**
     * Loads all results for a specific course.
     * @param courseId the course ID
     * @return list of results for this course
     */
    public List<Result> loadResultsByCourse(String courseId) {
        List<Result> courseResults = new ArrayList<>();
        List<Result> allResults = loadAllResults();

        for (Result result : allResults) {
            if (result.getCourseId().equals(courseId)) {
                courseResults.add(result);
            }
        }

        return courseResults;
    }

    // Additional helper methods to be implemented
}
