package models;

import dao.GradeDAO;
import dao.ResultDAO;
import dao.RecoveryPlanDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Student Class
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String firstName;
    private String lastName;
    private String major;
    private String email;
    private String status;
    private int year;
    private int semester;
    private boolean retake;

    public Student() {
    }

    //  Constructor with all required fields.
    public Student(String studentId, String firstName, String lastName, String major,
                   String email, String status, int year, int semester, boolean retake) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.email = email;
        this.status = status;
        this.year = year;
        this.semester = semester;
        this.retake = retake;
    }

    public double calculateCGPA() {
        List<Grade> allGrades = getGrades();
        if (allGrades == null || allGrades.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        int count = 0;

        for (Grade grade : allGrades) {
            if (grade.getGradePoint() > 0) {
                totalGradePoints += grade.getGradePoint();
                count++;
            }
        }

        return count > 0 ? totalGradePoints / count : 0.0;
    }

    public List<Course> getFailedCourses() {
        List<Result> allResults = getResults();
        List<Course> failedCourses = new ArrayList<>();

        if (allResults != null) {
            for (Result result : allResults) {
                if (result.needsRecovery()) {
                    Course course = result.getCourse();
                    if (course != null) {
                        failedCourses.add(course);
                    }
                }
            }
        }

        return failedCourses;
    }

    
    public boolean isEligibleForProgression() {
        double cgpa = calculateCGPA();
        int failedCoursesCount = getFailedCourses().size();

        return cgpa >= 2.0 && failedCoursesCount <= 3;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addGrade(Grade grade) {
        if (grade != null) {
            GradeDAO gradeDAO = new GradeDAO();
            gradeDAO.saveGrade(grade);
        }
    }

    public List<Grade> getGrades() {
        GradeDAO gradeDAO = new GradeDAO();
        return gradeDAO.loadGradesByStudent(this.studentId);
    }

    public List<Result> getResults() {
        ResultDAO resultDAO = new ResultDAO();
        return resultDAO.loadResultsByStudent(this.studentId);
    }

    public List<RecoveryPlan> getRecoveryPlans() {
        RecoveryPlanDAO recoveryPlanDAO = new RecoveryPlanDAO();
        return recoveryPlanDAO.loadPlansByStudent(this.studentId);
    }

    // Getters and Setters

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public boolean isRetake() {
        return retake;
    }

    public void setRetake(boolean retake) {
        this.retake = retake;
    }
    public List<RecoveryCourseEnrollment> getRecoveryCourseEnrollments() {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        return enrollmentDAO.loadEnrollmentsByStudent(this.studentId);
    }

   
    public List<RecoveryCourseEnrollment> getRecoveryCourseEnrollmentsByCourse(String courseId) {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        return enrollmentDAO.loadEnrollmentsByStudentAndCourse(this.studentId, courseId);
    }

   
    public boolean addNoteToRecoveryTask(String courseId, int actionNumber, String note) {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Find the specific enrollment
        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(this.studentId, courseId);

        RecoveryCourseEnrollment targetEnrollment = null;
        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == actionNumber) {
                targetEnrollment = enrollment;
                break;
            }
        }

        if (targetEnrollment == null) {
            System.err.println("Error: Enrollment not found for course " + courseId + ", action " + actionNumber);
            return false;
        }

        // Append student note with timestamp
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
        String studentNote = "[Student " + timestamp + "] " + note;

        String existingNotes = targetEnrollment.getNotes();
        String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
                            ? existingNotes + "\n" + studentNote
                            : studentNote;

        targetEnrollment.setNotes(updatedNotes);

        boolean success = enrollmentDAO.updateEnrollment(targetEnrollment);

        if (success) {
            System.out.println("Note added to recovery task " + actionNumber + " in course " + courseId);
        }

        return success;
    }

    public boolean submitRecoveryTask(String courseId, int actionNumber) {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Find the specific enrollment
        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(this.studentId, courseId);

        RecoveryCourseEnrollment targetEnrollment = null;
        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == actionNumber) {
                targetEnrollment = enrollment;
                break;
            }
        }

        if (targetEnrollment == null) {
            System.err.println("Error: Enrollment not found");
            return false;
        }

        // Can only submit if currently in progress
        if (targetEnrollment.getStatus() != RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS) {
            System.err.println("Error: Task must be IN_PROGRESS to submit. Current status: " + targetEnrollment.getStatus());
            return false;
        }

        // Change status to SUBMITTED
        targetEnrollment.setStatus(RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED);

        // Add submission note
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
        String submissionNote = "[Submitted on " + timestamp + "]";

        String existingNotes = targetEnrollment.getNotes();
        String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
                            ? existingNotes + "\n" + submissionNote
                            : submissionNote;

        targetEnrollment.setNotes(updatedNotes);

        boolean success = enrollmentDAO.updateEnrollment(targetEnrollment);

        if (success) {
            System.out.println("Recovery task " + actionNumber + " submitted for grading in course " + courseId);
        }

        return success;
    }

    public String getRecoveryProgressSummary() {
        List<RecoveryCourseEnrollment> allEnrollments = getRecoveryCourseEnrollments();

        if (allEnrollments.isEmpty()) {
            return "No recovery tasks assigned.";
        }

        // Group by course
        java.util.Map<String, List<RecoveryCourseEnrollment>> byCourse = new java.util.HashMap<>();
        for (RecoveryCourseEnrollment enrollment : allEnrollments) {
            byCourse.computeIfAbsent(enrollment.getCourseId(), k -> new ArrayList<>()).add(enrollment);
        }

        StringBuilder summary = new StringBuilder();
        summary.append("=== Recovery Progress Summary for ").append(getFullName()).append(" ===\n\n");

        for (java.util.Map.Entry<String, List<RecoveryCourseEnrollment>> entry : byCourse.entrySet()) {
            String courseId = entry.getKey();
            List<RecoveryCourseEnrollment> courseTasks = entry.getValue();

            // Sort by action number
            courseTasks.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

            int completed = 0;
            int failed = 0;
            int inProgress = 0;

            for (RecoveryCourseEnrollment task : courseTasks) {
                if (task.isCompleted()) completed++;
                else if (task.isFailed()) failed++;
                else if (task.isInProgress()) inProgress++;
            }

            summary.append("Course: ").append(courseId).append("\n");
            summary.append("  Total Tasks: ").append(courseTasks.size()).append("\n");
            summary.append("  Completed: ").append(completed).append("\n");
            summary.append("  In Progress: ").append(inProgress).append("\n");
            summary.append("  Failed: ").append(failed).append("\n");
            summary.append("  Progress: ").append(String.format("%.1f%%", (completed * 100.0 / courseTasks.size()))).append("\n\n");
        }

        return summary.toString();
    }
}
