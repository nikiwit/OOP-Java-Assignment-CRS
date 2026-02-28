package services;

import models.Result;
import models.Course;
import dao.CourseDAO;
import enums.GradeStatus;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service for calculating GPA and CGPA values.
 * Implements academic performance calculations based on Credit Hour System.
 */
public class CGPACalculator extends AbstractService {

    private final CourseDAO courseDAO;

    public CGPACalculator() {
        this.courseDAO = new CourseDAO();
    }

    @Override
    protected String getServiceName() {
        return "CGPACalculator";
    }

    /**
     * Calculates the cumulative GPA (CGPA) from course results.
     * Formula: (Total Grade Points) / (Total Credit Hours)
     * Uses most recent attempt for each course. Includes failed courses.
     * @param results list of course result records
     * @return calculated CGPA value (0.0 if no valid courses)
     */
    public double calculateCGPA(List<Result> results) {
        if (results == null || results.isEmpty()) {
            return 0.0;
        }

        // Group results by courseId to handle multiple attempts
        Map<String, List<Result>> resultsByCourse = new HashMap<>();
        for (Result result : results) {
            String courseId = result.getCourseId();
            resultsByCourse.putIfAbsent(courseId, new ArrayList<>());
            resultsByCourse.get(courseId).add(result);
        }

        double totalWeightedPoints = 0.0;
        int totalCreditHours = 0;

        for (List<Result> courseResults : resultsByCourse.values()) {
            Result mostRecent = courseResults.get(0);
            for (Result r : courseResults) {
                if (r.getAttemptNumber() > mostRecent.getAttemptNumber()) {
                    mostRecent = r;
                }
            }

            if (mostRecent.getStatus() == GradeStatus.INCOMPLETE) {
                continue;
            }

            Course course = courseDAO.loadCourse(mostRecent.getCourseId());
            if (course == null) {
                logError("Course not found: " + mostRecent.getCourseId());
                continue;
            }

            int creditHours = course.getCredits();
            double gradePoint = mostRecent.getGradePoint();

            totalWeightedPoints += gradePoint * creditHours;
            totalCreditHours += creditHours;
        }

        return totalCreditHours > 0 ? totalWeightedPoints / totalCreditHours : 0.0;
    }

    /**
     * Calculates the GPA for a specific semester.
     * @param results list of result records
     * @param semesterId the semester to calculate for
     * @return semester GPA value
     */
    public double calculateSemesterGPA(List<Result> results, String semesterId) {
        if (results == null || results.isEmpty() || semesterId == null) {
            return 0.0;
        }

        double totalWeightedPoints = 0.0;
        int totalCreditHours = 0;

        for (Result result : results) {
            if (!semesterId.equals(result.getSemesterId())) {
                continue;
            }

            if (result.getStatus() == GradeStatus.INCOMPLETE) {
                continue;
            }

            Course course = courseDAO.loadCourse(result.getCourseId());
            if (course == null) {
                continue;
            }

            int creditHours = course.getCredits();
            double gradePoint = result.getGradePoint();

            totalWeightedPoints += gradePoint * creditHours;
            totalCreditHours += creditHours;
        }

        return totalCreditHours > 0 ? totalWeightedPoints / totalCreditHours : 0.0;
    }

    /**
     * Calculates the total credit hours completed from results.
     * @param results list of result records
     * @return total credit hours
     */
    public int getTotalCreditHours(List<Result> results) {
        if (results == null || results.isEmpty()) {
            return 0;
        }

        int totalCredits = 0;
        Set<String> processedCourses = new HashSet<>();

        for (Result result : results) {
            if (result.getStatus() == GradeStatus.INCOMPLETE) {
                continue;
            }

            String courseKey = result.getCourseId();
            if (processedCourses.contains(courseKey)) {
                continue;
            }

            Course course = courseDAO.loadCourse(result.getCourseId());
            if (course != null) {
                totalCredits += course.getCredits();
                processedCourses.add(courseKey);
            }
        }

        return totalCredits;
    }
}

