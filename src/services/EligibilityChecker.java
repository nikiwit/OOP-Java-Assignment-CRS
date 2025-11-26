package services;

import models.Student;
import models.Result;
import models.Course;
import models.RecoveryCourseEnrollment;
import dao.ResultDAO;
import dao.RecoveryCourseEnrollmentDAO;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service for checking student eligibility for academic progression.
 * Validates CGPA requirements (>= 2.0) and failed course limits (<= 3).
 */
public class EligibilityChecker extends AbstractService {
    private static final double MIN_CGPA = 2.0;
    private static final int MAX_FAILED_COURSES = 3;

    private final ResultDAO resultDAO;
    private final CGPACalculator cgpaCalculator;
    private final RecoveryCourseEnrollmentDAO enrollmentDAO;

    public EligibilityChecker() {
        this.resultDAO = new ResultDAO();
        this.cgpaCalculator = new CGPACalculator();
        this.enrollmentDAO = new RecoveryCourseEnrollmentDAO();
    }

    @Override
    protected String getServiceName() {
        return "EligibilityChecker";
    }

    /**
     * Checks if a student is eligible to progress to the next level.
     * Criteria:
     * 1. CGPA must be >= 2.0
     * 2. Failed courses must be <= 3
     *
     * @param student the student to check
     * @return true if eligible, false otherwise
     */
    public boolean checkEligibility(Student student) {
        validateNotNull(student, "Student");
        validateNotEmpty(student.getStudentId(), "Student ID");

        double cgpa = calculateCGPA(student);
        int failedCount = getFailedCoursesCount(student);

        return cgpa >= MIN_CGPA && failedCount <= MAX_FAILED_COURSES;
    }

    /**
     * Calculates the CGPA for a student.
     * @param student the student
     * @return calculated CGPA value
     */
    public double calculateCGPA(Student student) {
        validateNotNull(student, "Student");

        List<Result> results = resultDAO.loadResultsByStudent(student.getStudentId());
        return cgpaCalculator.calculateCGPA(results);
    }

    /**
     * Counts the number of unique courses that need recovery.
     * A course needs recovery if the most recent attempt failed.
     * Multiple failed attempts of the same course count as 1 failed course.
     *
     * @param student the student
     * @return count of unique failed courses
     */
    public int getFailedCoursesCount(Student student) {
        validateNotNull(student, "Student");

        List<Result> results = resultDAO.loadResultsByStudent(student.getStudentId());
        if (results == null || results.isEmpty()) {
            return 0;
        }

        Map<String, List<Result>> resultsByCourse = new HashMap<>();
        for (Result result : results) {
            String courseId = result.getCourseId();
            resultsByCourse.putIfAbsent(courseId, new ArrayList<>());
            resultsByCourse.get(courseId).add(result);
        }

        Set<String> uniqueFailedCourses = new HashSet<>();

        for (List<Result> courseResults : resultsByCourse.values()) {
            Result mostRecent = courseResults.get(0);
            for (Result r : courseResults) {
                if (r.getAttemptNumber() > mostRecent.getAttemptNumber()) {
                    mostRecent = r;
                }
            }

            if (mostRecent.needsRecovery()) {
                uniqueFailedCourses.add(mostRecent.getCourseId());
            }
        }

        return uniqueFailedCourses.size();
    }

    /**
     * Gets all courses that a student has failed and needs to recover.
     * Excludes courses where the student already has an active recovery enrollment.
     *
     * @param student the student
     * @return list of failed courses that need enrollment
     */
    public List<Course> getFailedCourses(Student student) {
        validateNotNull(student, "Student");

        List<Result> results = resultDAO.loadResultsByStudent(student.getStudentId());
        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, List<Result>> resultsByCourse = new HashMap<>();
        for (Result result : results) {
            String courseId = result.getCourseId();
            resultsByCourse.putIfAbsent(courseId, new ArrayList<>());
            resultsByCourse.get(courseId).add(result);
        }

        List<Course> failedCourses = new ArrayList<>();
        for (List<Result> courseResults : resultsByCourse.values()) {
            Result mostRecent = courseResults.get(0);
            for (Result r : courseResults) {
                if (r.getAttemptNumber() > mostRecent.getAttemptNumber()) {
                    mostRecent = r;
                }
            }

            if (mostRecent.needsRecovery()) {
                Course course = mostRecent.getCourse();
                if (course != null) {
                    failedCourses.add(course);
                }
            }
        }

        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadAllEnrollments();
        Set<String> enrolledCourseIds = new HashSet<>();

        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(student.getStudentId())) {
                // Exclude courses with IN_PROGRESS or SUBMITTED status
                RecoveryCourseEnrollment.RecoveryEnrollmentStatus status = enrollment.getStatus();
                if (status == RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS ||
                    status == RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED) {
                    enrolledCourseIds.add(enrollment.getCourseId());
                }
            }
        }

        List<Course> coursesNeedingEnrollment = new ArrayList<>();
        for (Course course : failedCourses) {
            if (!enrolledCourseIds.contains(course.getCourseId())) {
                coursesNeedingEnrollment.add(course);
            }
        }

        return coursesNeedingEnrollment;
    }

    /**
     * Gets a list of all students who are not eligible for progression.
     * @param students list of all students to check
     * @return list of ineligible students
     */
    public List<Student> getIneligibleStudents(List<Student> students) {
        validateNotNull(students, "Students list");

        List<Student> ineligible = new ArrayList<>();
        for (Student student : students) {
            if (!checkEligibility(student)) {
                ineligible.add(student);
            }
        }
        return ineligible;
    }

    /**
     * Gets the minimum CGPA requirement for progression.
     * @return minimum CGPA value
     */
    public double getMinCGPA() {
        return MIN_CGPA;
    }

    /**
     * Gets the maximum allowed failed courses for progression.
     * @return maximum failed courses count
     */
    public int getMaxFailedCourses() {
        return MAX_FAILED_COURSES;
    }
}

