package dao;

import models.Grade;
import enums.ComponentType;
import enums.GradeStatus;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Grade entities.
 * Handles CRUD operations for grade data stored in text files.
 */
public class GradeDAO {
    private static final String GRADES_FILE = "data/grades.txt";

    public GradeDAO() {
    }

    /**
     * Loads all grades from the data file.
     * @return list of all grades
     */
    public List<Grade> loadAllGrades() {
        List<Grade> grades = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(GRADES_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse CSV line: GradeID,StudentID,CourseID,CourseName,AttemptNumber,Component,Grade,GradePoint,Status,SemesterID,InstructorID,GradedDate
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    try {
                        Grade grade = new Grade();
                        grade.setGradeId(parts[0].trim());
                        grade.setStudentId(parts[1].trim());
                        grade.setCourseId(parts[2].trim());
                        grade.setCourseName(parts[3].trim());
                        grade.setAttemptNumber(Integer.parseInt(parts[4].trim()));
                        grade.setComponent(ComponentType.valueOf(parts[5].trim().toUpperCase()));
                        grade.setGrade(parts[6].trim());
                        grade.setGradePoint(Double.parseDouble(parts[7].trim()));
                        grade.setStatus(GradeStatus.valueOf(parts[8].trim().toUpperCase()));
                        grade.setSemesterId(parts[9].trim());
                        grade.setInstructorId(parts[10].trim());

                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = dateFormat.parse(parts[11].trim());
                            grade.setGradedDate(date);
                        } catch (Exception dateEx) {
                            grade.setGradedDate(new Date());
                        }

                        grades.add(grade);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error parsing grade line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading grades file: " + e.getMessage());
            e.printStackTrace();
        }

        return grades;
    }

    /**
     * Loads a grade by ID from the data file.
     * @param gradeId the grade ID
     * @return the Grade object, or null if not found
     */
    public Grade loadGrade(String gradeId) {
        List<Grade> allGrades = loadAllGrades();
        for (Grade grade : allGrades) {
            if (grade.getGradeId().equals(gradeId)) {
                return grade;
            }
        }
        return null;
    }

    /**
     * Loads all grades for a specific student.
     * @param studentId the student ID
     * @return list of grades for this student
     */
    public List<Grade> loadGradesByStudent(String studentId) {
        List<Grade> studentGrades = new ArrayList<>();
        List<Grade> allGrades = loadAllGrades();

        for (Grade grade : allGrades) {
            if (grade.getStudentId().equals(studentId)) {
                studentGrades.add(grade);
            }
        }

        return studentGrades;
    }

    /**
     * Loads all grades for a specific course.
     * @param courseId the course ID
     * @return list of grades for this course
     */
    public List<Grade> loadGradesByCourse(String courseId) {
        List<Grade> courseGrades = new ArrayList<>();
        List<Grade> allGrades = loadAllGrades();

        for (Grade grade : allGrades) {
            if (grade.getCourseId().equals(courseId)) {
                courseGrades.add(grade);
            }
        }

        return courseGrades;
    }

    /**
     * Saves a new grade to the data file.
     * @param grade the grade to save
     */
    public void saveGrade(Grade grade) {
        // To be implemented
    }

    /**
     * Updates an existing grade in the data file.
     * @param grade the grade with updated data
     */
    public void updateGrade(Grade grade) {
        // To be implemented
    }
}
