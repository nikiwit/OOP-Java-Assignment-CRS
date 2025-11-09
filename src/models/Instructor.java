package models;

import dao.CourseDAO;
import dao.GradeDAO;
import dao.RecoveryPlanDAO;
import enums.RecoveryStatus;
import enums.UserRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents an instructor user in the Course Recovery System.
 * Instructors can create and manage recovery plans, grade student work,
 * and track student progress in their assigned courses.
 * Extends the User class demonstrating inheritance.
 */
public class Instructor extends User {
    private static final long serialVersionUID = 1L;

    private String instructorName;
    private List<String> assignedCourseIds;

    /**
     * Default constructor.
     */
    public Instructor() {
        super();
        setRole(UserRole.INSTRUCTOR);
        this.assignedCourseIds = new ArrayList<>();
    }

    /**
     * Constructor with all fields.
     *
     * @param userId user unique identifier
     * @param email instructor email address
     * @param password instructor password
     * @param instructorName instructor full name
     */
    public Instructor(String userId, String email, String password, String instructorName) {
        super(userId, UserRole.INSTRUCTOR, email, password);
        this.instructorName = instructorName;
        this.assignedCourseIds = new ArrayList<>();
    }

    /**
     * Creates a new recovery plan for a student who failed a course.
     *
     * @param student the student requiring recovery
     * @param course the course that needs to be recovered
     * @return the created RecoveryPlan object
     */
    public RecoveryPlan createRecoveryPlan(Student student, Course course) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course cannot be null");
        }

        // Verify this instructor is assigned to the course
        if (!assignedCourseIds.contains(course.getCourseId())) {
            throw new IllegalStateException("Instructor is not assigned to this course");
        }

        // Create new recovery plan
        RecoveryPlan plan = new RecoveryPlan();
        plan.setPlanId(UUID.randomUUID().toString());
        plan.setStudentId(student.getStudentId());
        plan.setCourseId(course.getCourseId());
        plan.setInstructorId(this.getUserId());
        plan.setCurrentActionNumber(1);
        plan.setStatus(RecoveryStatus.ACTIVE);
        plan.setStartDate(new Date());

        // Set target end date to 3 months from now (typical recovery period)
        Date targetDate = new Date();
        targetDate.setTime(targetDate.getTime() + (90L * 24 * 60 * 60 * 1000)); // 90 days
        plan.setTargetEndDate(targetDate);

        // Save the recovery plan using DAO
        RecoveryPlanDAO recoveryPlanDAO = new RecoveryPlanDAO();
        recoveryPlanDAO.saveRecoveryPlan(plan);

        System.out.println("Instructor " + instructorName + " created recovery plan for student " + student.getStudentId());
        return plan;
    }

    /**
     * Updates an existing recovery plan with new information.
     *
     * @param plan the recovery plan to update
     */
    public void updateRecoveryPlan(RecoveryPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Recovery plan cannot be null");
        }

        // Verify this instructor owns the plan
        if (!this.getUserId().equals(plan.getInstructorId())) {
            throw new IllegalStateException("Instructor can only update their own recovery plans");
        }

        RecoveryPlanDAO recoveryPlanDAO = new RecoveryPlanDAO();
        recoveryPlanDAO.updateRecoveryPlan(plan);

        System.out.println("Instructor " + instructorName + " updated recovery plan " + plan.getPlanId());
    }

    /**
     * Tracks and monitors the progress of a student's recovery plan.
     *
     * @param plan the recovery plan to track
     */
    public void trackProgress(RecoveryPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Recovery plan cannot be null");
        }

        // Verify this instructor owns the plan
        if (!this.getUserId().equals(plan.getInstructorId())) {
            throw new IllegalStateException("Instructor can only track their own recovery plans");
        }

        System.out.println("=== Recovery Plan Progress ===");
        System.out.println("Plan ID: " + plan.getPlanId());
        System.out.println("Student ID: " + plan.getStudentId());
        System.out.println("Course ID: " + plan.getCourseId());
        System.out.println("Current Action: " + plan.getCurrentActionNumber());
        System.out.println("Status: " + plan.getStatus());
        System.out.println("Start Date: " + plan.getStartDate());
        System.out.println("Target End Date: " + plan.getTargetEndDate());

        if (plan.isOverdue()) {
            System.out.println("WARNING: This recovery plan is OVERDUE!");
        }

        if (plan.isCompleted()) {
            System.out.println("Completed Date: " + plan.getCompletedDate());
        }

        if (plan.getNotes() != null && !plan.getNotes().isEmpty()) {
            System.out.println("Notes: " + plan.getNotes());
        }
        System.out.println("==============================");
    }

    /**
     * Grades a student's recovery work and updates the grade record.
     *
     * @param grade the grade object to process
     */
    public void gradeRecovery(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }

        // Verify this instructor is assigned to the course
        if (!assignedCourseIds.contains(grade.getCourseId())) {
            throw new IllegalStateException("Instructor is not assigned to this course");
        }

        // Set the instructor ID and graded date
        grade.setInstructorId(this.getUserId());
        grade.setGradedDate(new Date());

        // Calculate grade point based on letter grade
        double gradePoint = grade.calculateGradePoint();
        grade.setGradePoint(gradePoint);

        // Save the grade using DAO
        GradeDAO gradeDAO = new GradeDAO();
        gradeDAO.updateGrade(grade);

        System.out.println("Instructor " + instructorName + " graded recovery work: " + grade.getGrade());
    }

    /**
     * Retrieves all courses assigned to this instructor.
     *
     * @return list of Course objects assigned to this instructor
     */
    public List<Course> getAssignedCourses() {
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = new ArrayList<>();

        for (String courseId : assignedCourseIds) {
            Course course = courseDAO.loadCourse(courseId);
            if (course != null) {
                courses.add(course);
            }
        }

        return courses;
    }

    /**
     * Assigns a new course to this instructor.
     *
     * @param courseId the course ID to assign
     */
    public void assignCourse(String courseId) {
        if (courseId != null && !assignedCourseIds.contains(courseId)) {
            assignedCourseIds.add(courseId);
        }
    }

    /**
     * Removes a course assignment from this instructor.
     *
     * @param courseId the course ID to unassign
     */
    public void unassignCourse(String courseId) {
        assignedCourseIds.remove(courseId);
    }

    /**
     * Gets dashboard view of all students' recovery progress for this instructor's courses.
     * Shows current action, status, and course for each student.
     *
     * @return list of student progress summaries
     */
    public List<StudentRecoveryProgress> getStudentRecoveryDashboard() {
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();
        List<StudentRecoveryProgress> dashboard = new ArrayList<>();

        // Get all courses assigned to this instructor
        for (String courseId : assignedCourseIds) {
            // Get all enrollments for this course
            List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByCourse(courseId);

            // Group by student
            java.util.Map<String, List<RecoveryCourseEnrollment>> studentEnrollments = new java.util.HashMap<>();
            for (RecoveryCourseEnrollment enrollment : enrollments) {
                studentEnrollments.computeIfAbsent(enrollment.getStudentId(), k -> new ArrayList<>()).add(enrollment);
            }

            // Create progress summary for each student
            for (java.util.Map.Entry<String, List<RecoveryCourseEnrollment>> entry : studentEnrollments.entrySet()) {
                String studentId = entry.getKey();
                List<RecoveryCourseEnrollment> studentTasks = entry.getValue();

                // Sort by action number
                studentTasks.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

                // Find current task (first IN_PROGRESS or next after last SUBMITTED)
                RecoveryCourseEnrollment currentTask = null;
                int completedCount = 0;
                int totalTasks = studentTasks.size();

                for (RecoveryCourseEnrollment task : studentTasks) {
                    if (task.isInProgress()) {
                        currentTask = task;
                        break;
                    } else if (task.isCompleted()) {
                        completedCount++;
                    } else if (task.isFailed()) {
                        currentTask = task;
                        break;
                    }
                }

                if (currentTask == null && completedCount > 0 && completedCount < totalTasks) {
                    // All completed so far, show next task
                    currentTask = studentTasks.get(completedCount);
                }

                StudentRecoveryProgress progress = new StudentRecoveryProgress(
                    studentId,
                    courseId,
                    currentTask != null ? currentTask.getActionNumber() : 0,
                    currentTask != null ? currentTask.getTitle() : "Completed",
                    currentTask != null ? currentTask.getDescription() : "All tasks completed",
                    currentTask != null ? currentTask.getStatus().toString() : "COMPLETED",
                    completedCount,
                    totalTasks
                );

                dashboard.add(progress);
            }
        }

        return dashboard;
    }

    /**
     * Updates the grade and notes for a specific student's recovery task.
     * This method is called when grading recovery work.
     *
     * @param studentId student ID
     * @param courseId course ID
     * @param actionNumber action number to grade
     * @param grade grade value (0-100)
     * @param notes instructor notes/feedback
     * @return true if update successful
     */
    public boolean gradeRecoveryTask(String studentId, String courseId, int actionNumber, int grade, String notes) {
        // Verify this instructor teaches the course
        if (!assignedCourseIds.contains(courseId)) {
            System.err.println("Error: Instructor not assigned to course " + courseId);
            return false;
        }

        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Find the specific enrollment
        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

        RecoveryCourseEnrollment targetEnrollment = null;
        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == actionNumber) {
                targetEnrollment = enrollment;
                break;
            }
        }

        if (targetEnrollment == null) {
            System.err.println("Error: Enrollment not found for student " + studentId +
                             ", course " + courseId + ", action " + actionNumber);
            return false;
        }

        // Update grade and notes
        targetEnrollment.setGrade(grade);
        if (notes != null && !notes.isEmpty()) {
            String existingNotes = targetEnrollment.getNotes();
            String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
                                ? existingNotes + " | " + notes
                                : notes;
            targetEnrollment.setNotes(updatedNotes);
        }

        // Update status based on grade if task was submitted
        if (targetEnrollment.getStatus() == RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED) {
            if (grade >= 70) {
                // Keep as SUBMITTED (passed)
                System.out.println("Student passed task " + actionNumber + " with grade " + grade);
            } else {
                targetEnrollment.setStatus(RecoveryCourseEnrollment.RecoveryEnrollmentStatus.FAILED);
                System.out.println("Student failed task " + actionNumber + " with grade " + grade);
            }
        }

        boolean success = enrollmentDAO.updateEnrollment(targetEnrollment);

        if (success) {
            System.out.println("Graded " + studentId + "'s work for action " + actionNumber +
                             " in course " + courseId + ": Grade=" + grade);
        }

        return success;
    }

    /**
     * Updates notes for a student's recovery enrollment.
     *
     * @param studentId student ID
     * @param courseId course ID
     * @param actionNumber action number
     * @param notes new notes to add
     * @return true if successful
     */
    public boolean updateRecoveryNotes(String studentId, String courseId, int actionNumber, String notes) {
        // Verify this instructor teaches the course
        if (!assignedCourseIds.contains(courseId)) {
            System.err.println("Error: Instructor not assigned to course " + courseId);
            return false;
        }

        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Find the specific enrollment
        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

        RecoveryCourseEnrollment targetEnrollment = null;
        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == actionNumber) {
                targetEnrollment = enrollment;
                break;
            }
        }

        if (targetEnrollment == null) {
            return false;
        }

        // Append notes
        String existingNotes = targetEnrollment.getNotes();
        String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
                            ? existingNotes + "\n" + notes
                            : notes;
        targetEnrollment.setNotes(updatedNotes);

        return enrollmentDAO.updateEnrollment(targetEnrollment);
    }

    /**
     * Advances a student to the next recovery task if current task is complete.
     *
     * @param studentId student ID
     * @param courseId course ID
     * @param currentActionNumber current action number
     * @return true if advanced successfully
     */
    public boolean advanceToNextTask(String studentId, String courseId, int currentActionNumber) {
        // Verify this instructor teaches the course
        if (!assignedCourseIds.contains(courseId)) {
            System.err.println("Error: Instructor not assigned to course " + courseId);
            return false;
        }

        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Get all enrollments for this student and course
        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

        // Find current and next task
        RecoveryCourseEnrollment currentTask = null;
        RecoveryCourseEnrollment nextTask = null;

        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == currentActionNumber) {
                currentTask = enrollment;
            } else if (enrollment.getActionNumber() == currentActionNumber + 1) {
                nextTask = enrollment;
            }
        }

        if (currentTask == null) {
            System.err.println("Error: Current task not found");
            return false;
        }

        // Verify current task is completed
        if (!currentTask.isCompleted()) {
            System.err.println("Error: Current task must be completed before advancing");
            return false;
        }

        if (nextTask == null) {
            System.out.println("Student has completed all recovery tasks!");
            return true;
        }

        // Set next task to IN_PROGRESS
        nextTask.setStatus(RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS);

        return enrollmentDAO.updateEnrollment(nextTask);
    }

    /**
     * Gets comprehensive recovery dashboard combining RecoveryPlan and RecoveryCourseEnrollment data.
     * Shows all recovery plans and detailed task progress for students in instructor's courses.
     *
     * @return list of detailed recovery views
     */
    public List<DetailedRecoveryView> getRecoveryDashboard() {
        dao.RecoveryPlanDAO planDAO = new dao.RecoveryPlanDAO();
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();
        List<DetailedRecoveryView> dashboard = new ArrayList<>();

        // Load all recovery plans for this instructor
        List<RecoveryPlan> instructorPlans = planDAO.loadPlansByInstructor(this.getUserId());

        for (RecoveryPlan plan : instructorPlans) {
            // Only include plans for courses assigned to this instructor
            if (!assignedCourseIds.contains(plan.getCourseId())) {
                continue;
            }

            // Load corresponding enrollments
            List<RecoveryCourseEnrollment> enrollments =
                enrollmentDAO.loadEnrollmentsByStudentAndCourse(plan.getStudentId(), plan.getCourseId());

            // Load student information
            dao.StudentDAO studentDAO = new dao.StudentDAO();
            Student student = studentDAO.loadStudent(plan.getStudentId());

            // Create detailed view combining plan and enrollments
            DetailedRecoveryView view = new DetailedRecoveryView(plan, enrollments, student);
            dashboard.add(view);
        }

        return dashboard;
    }

    /**
     * Changes the recovery action number for a student and synchronizes with enrollments.
     * Updates RecoveryPlan via RecoveryPlanDAO and corresponding RecoveryCourseEnrollments.
     *
     * @param planId recovery plan ID
     * @param studentId student ID
     * @param courseId course ID
     * @param newActionNumber new action number to set
     * @return true if update successful
     */
    public boolean changeRecoveryAction(String planId, String studentId, String courseId, int newActionNumber) {
        // Verify this instructor teaches the course
        if (!assignedCourseIds.contains(courseId)) {
            System.err.println("Error: Instructor not assigned to course " + courseId);
            return false;
        }

        dao.RecoveryPlanDAO planDAO = new dao.RecoveryPlanDAO();
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Load the recovery plan
        RecoveryPlan plan = planDAO.loadRecoveryPlan(planId);
        if (plan == null) {
            System.err.println("Error: Recovery plan not found");
            return false;
        }

        // Verify plan belongs to this instructor
        if (!plan.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Plan not managed by this instructor");
            return false;
        }

        // Load all enrollments for this student and course
        List<RecoveryCourseEnrollment> enrollments =
            enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

        // Verify new action number exists in enrollments
        boolean actionExists = false;
        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == newActionNumber) {
                actionExists = true;
                break;
            }
        }

        if (!actionExists) {
            System.err.println("Error: Action number " + newActionNumber + " does not exist");
            return false;
        }

        // Update RecoveryPlan
        int oldActionNumber = plan.getCurrentActionNumber();
        plan.setCurrentActionNumber(newActionNumber);
        try {
            planDAO.updateRecoveryPlan(plan);
        } catch (Exception e) {
            System.err.println("Error updating recovery plan: " + e.getMessage());
            return false;
        }

        // Synchronize enrollments: update status based on new action number
        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() < newActionNumber) {
                // Tasks before current should be marked as SUBMITTED (completed)
                if (!enrollment.isCompleted() && !enrollment.isFailed()) {
                    enrollment.setStatus(RecoveryCourseEnrollment.RecoveryEnrollmentStatus.SUBMITTED);
                    enrollmentDAO.updateEnrollment(enrollment);
                }
            } else if (enrollment.getActionNumber() == newActionNumber) {
                // Current task should be IN_PROGRESS
                enrollment.setStatus(RecoveryCourseEnrollment.RecoveryEnrollmentStatus.IN_PROGRESS);
                enrollmentDAO.updateEnrollment(enrollment);
            }
            // Future tasks remain unchanged
        }

        System.out.println("Successfully changed action from " + oldActionNumber + " to " + newActionNumber);
        return true;
    }

    /**
     * Updates notes for a recovery plan and synchronizes with all related enrollments.
     * Updates both RecoveryPlan (via RecoveryPlanDAO) and RecoveryCourseEnrollments.
     *
     * @param planId recovery plan ID
     * @param notes new notes to add
     * @return true if update successful
     */
    public boolean updateRecoveryPlanNotes(String planId, String notes) {
        dao.RecoveryPlanDAO planDAO = new dao.RecoveryPlanDAO();
        dao.RecoveryCourseEnrollmentDAO enrollmentDAO = new dao.RecoveryCourseEnrollmentDAO();

        // Load the recovery plan
        RecoveryPlan plan = planDAO.loadRecoveryPlan(planId);
        if (plan == null) {
            System.err.println("Error: Recovery plan not found");
            return false;
        }

        // Verify this instructor manages the plan
        if (!plan.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Plan not managed by this instructor");
            return false;
        }

        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(plan.getCourseId())) {
            System.err.println("Error: Instructor not assigned to course");
            return false;
        }

        // Add timestamp to notes
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
        String instructorNote = "[Instructor " + timestamp + "] " + notes;

        // Update RecoveryPlan notes
        String existingNotes = plan.getNotes();
        String updatedNotes = existingNotes != null && !existingNotes.isEmpty()
            ? existingNotes + "\n" + instructorNote
            : instructorNote;
        plan.setNotes(updatedNotes);

        try {
            planDAO.updateRecoveryPlan(plan);
        } catch (Exception e) {
            System.err.println("Error updating recovery plan: " + e.getMessage());
            return false;
        }

        // Synchronize notes to current enrollment
        List<RecoveryCourseEnrollment> enrollments =
            enrollmentDAO.loadEnrollmentsByStudentAndCourse(plan.getStudentId(), plan.getCourseId());

        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == plan.getCurrentActionNumber()) {
                // Add note to current task enrollment
                String enrollmentNotes = enrollment.getNotes();
                String updatedEnrollmentNotes = enrollmentNotes != null && !enrollmentNotes.isEmpty()
                    ? enrollmentNotes + "\n" + instructorNote
                    : instructorNote;
                enrollment.setNotes(updatedEnrollmentNotes);
                enrollmentDAO.updateEnrollment(enrollment);
                break;
            }
        }

        System.out.println("Successfully updated notes for plan " + planId);
        return true;
    }

    /**
     * Inner class to represent student recovery progress summary.
     */
    public static class StudentRecoveryProgress {
        private String studentId;
        private String courseId;
        private int currentActionNumber;
        private String currentTaskTitle;
        private String currentTaskDescription;
        private String status;
        private int completedTasks;
        private int totalTasks;

        public StudentRecoveryProgress(String studentId, String courseId, int currentActionNumber,
                                      String currentTaskTitle, String currentTaskDescription,
                                      String status, int completedTasks, int totalTasks) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.currentActionNumber = currentActionNumber;
            this.currentTaskTitle = currentTaskTitle;
            this.currentTaskDescription = currentTaskDescription;
            this.status = status;
            this.completedTasks = completedTasks;
            this.totalTasks = totalTasks;
        }

        // Getters
        public String getStudentId() { return studentId; }
        public String getCourseId() { return courseId; }
        public int getCurrentActionNumber() { return currentActionNumber; }
        public String getCurrentTaskTitle() { return currentTaskTitle; }
        public String getCurrentTaskDescription() { return currentTaskDescription; }
        public String getStatus() { return status; }
        public int getCompletedTasks() { return completedTasks; }
        public int getTotalTasks() { return totalTasks; }

        @Override
        public String toString() {
            return String.format("Student: %s | Course: %s | Progress: %d/%d | Current: Action %d - %s [%s]",
                               studentId, courseId, completedTasks, totalTasks,
                               currentActionNumber, currentTaskTitle, status);
        }
    }

    /**
     * Inner class combining RecoveryPlan with RecoveryCourseEnrollment details.
     * Provides comprehensive view of a student's recovery progress.
     */
    public static class DetailedRecoveryView {
        private RecoveryPlan plan;
        private List<RecoveryCourseEnrollment> enrollments;
        private Student student;

        public DetailedRecoveryView(RecoveryPlan plan, List<RecoveryCourseEnrollment> enrollments, Student student) {
            this.plan = plan;
            this.enrollments = enrollments;
            this.student = student;
        }

        public RecoveryPlan getPlan() {
            return plan;
        }

        public List<RecoveryCourseEnrollment> getEnrollments() {
            return enrollments;
        }

        public Student getStudent() {
            return student;
        }

        /**
         * Gets formatted display of complete recovery information.
         * @return formatted string with all recovery details
         */
        public String getDetailedDisplay() {
            StringBuilder display = new StringBuilder();
            display.append("=== Recovery Plan Details ===\n");
            display.append("Plan ID: ").append(plan.getPlanId()).append("\n");
            display.append("Student: ").append(student != null ? student.getFirstName() + " " + student.getLastName() : plan.getStudentId()).append("\n");
            display.append("Student ID: ").append(plan.getStudentId()).append("\n");
            display.append("Course: ").append(plan.getCourseId()).append("\n");
            display.append("Current Action: ").append(plan.getCurrentActionNumber()).append("\n");
            display.append("Status: ").append(plan.getStatus()).append("\n");

            if (plan.getStartDate() != null) {
                display.append("Start Date: ").append(plan.getStartDate()).append("\n");
            }
            if (plan.getTargetEndDate() != null) {
                display.append("Target End Date: ").append(plan.getTargetEndDate()).append("\n");
            }
            if (plan.isOverdue()) {
                display.append("*** OVERDUE ***\n");
            }

            display.append("\n=== Task Progress ===\n");

            // Sort enrollments by action number
            enrollments.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

            int completedCount = 0;
            int failedCount = 0;
            int inProgressCount = 0;

            for (RecoveryCourseEnrollment enrollment : enrollments) {
                display.append("Task ").append(enrollment.getActionNumber()).append(": ").append(enrollment.getTitle()).append("\n");
                display.append("  Status: ").append(enrollment.getStatus()).append("\n");

                if (enrollment.getGrade() != null) {
                    display.append("  Grade: ").append(enrollment.getGrade()).append("%\n");
                }

                if (enrollment.getNotes() != null && !enrollment.getNotes().isEmpty()) {
                    display.append("  Notes: ").append(enrollment.getNotes()).append("\n");
                }

                display.append("\n");

                // Count statuses
                if (enrollment.isCompleted()) {
                    completedCount++;
                } else if (enrollment.isFailed()) {
                    failedCount++;
                } else if (enrollment.isInProgress()) {
                    inProgressCount++;
                }
            }

            display.append("=== Summary ===\n");
            display.append("Completed: ").append(completedCount).append("/").append(enrollments.size()).append("\n");
            display.append("In Progress: ").append(inProgressCount).append("\n");
            display.append("Failed: ").append(failedCount).append("\n");

            if (plan.getNotes() != null && !plan.getNotes().isEmpty()) {
                display.append("\n=== Plan Notes ===\n");
                display.append(plan.getNotes()).append("\n");
            }

            return display.toString();
        }

        @Override
        public String toString() {
            return String.format("Recovery Plan: %s | Student: %s | Course: %s | Action: %d | Status: %s | Tasks: %d",
                plan.getPlanId(),
                student != null ? student.getFirstName() + " " + student.getLastName() : plan.getStudentId(),
                plan.getCourseId(),
                plan.getCurrentActionNumber(),
                plan.getStatus(),
                enrollments.size());
        }
    }

    /**
     * Gets all recovery plan templates for courses assigned to this instructor.
     * @return list of recovery plan templates
     */
    public List<CourseRecoveryPlanTemplate> getRecoveryPlanTemplates() {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();
        List<CourseRecoveryPlanTemplate> allTemplates = new ArrayList<>();

        // Get templates for all assigned courses
        for (String courseId : assignedCourseIds) {
            List<CourseRecoveryPlanTemplate> courseTemplates = templateDAO.loadTemplatesByCourse(courseId);
            allTemplates.addAll(courseTemplates);
        }

        return allTemplates;
    }

    /**
     * Creates a new recovery plan template for a course.
     * @param courseId course ID
     * @param planTitle title of the recovery plan
     * @param planDescription description of the recovery strategy
     * @return the created template, or null if failed
     */
    public CourseRecoveryPlanTemplate createRecoveryPlanTemplate(String courseId, String planTitle, String planDescription) {
        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(courseId)) {
            System.err.println("Error: Instructor not assigned to course " + courseId);
            return null;
        }

        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();

        // Generate unique template ID
        String templateId = "TPL_" + courseId + "_" + System.currentTimeMillis();

        CourseRecoveryPlanTemplate template = new CourseRecoveryPlanTemplate(
            templateId, courseId, this.getUserId(), planTitle, planDescription
        );

        try {
            templateDAO.saveTemplate(template);
            System.out.println("Created recovery plan template: " + templateId);
            return template;
        } catch (Exception e) {
            System.err.println("Error creating template: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates the metadata (title and description) of a recovery plan template.
     * @param templateId template ID
     * @param newTitle new plan title
     * @param newDescription new plan description
     * @return true if update successful
     */
    public boolean updateRecoveryPlanTemplateMetadata(String templateId, String newTitle, String newDescription) {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();

        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null) {
            System.err.println("Error: Template not found");
            return false;
        }

        // Verify instructor owns the template
        if (!template.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Instructor does not own this template");
            return false;
        }

        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(template.getCourseId())) {
            System.err.println("Error: Instructor not assigned to course");
            return false;
        }

        template.updateMetadata(newTitle, newDescription);

        try {
            templateDAO.updateTemplate(template);
            System.out.println("Updated template metadata: " + templateId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating template: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new recovery action to a plan template.
     * Creates a new RecoveryCourseAction and associates it with the template.
     * @param templateId template ID
     * @param actionNumber action sequence number
     * @param title action title
     * @param description action description
     * @param hasGrade whether action requires grading
     * @return true if action added successfully
     */
    public boolean addActionToTemplate(String templateId, int actionNumber, String title,
                                       String description, boolean hasGrade) {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();

        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null) {
            System.err.println("Error: Template not found");
            return false;
        }

        // Verify instructor owns the template
        if (!template.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Instructor does not own this template");
            return false;
        }

        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(template.getCourseId())) {
            System.err.println("Error: Instructor not assigned to course");
            return false;
        }

        // Generate unique action ID
        String actionId = "ACT_" + template.getCourseId() + "_" + actionNumber + "_" + System.currentTimeMillis();

        // Create new RecoveryCourseAction
        RecoveryCourseAction action = new RecoveryCourseAction(
            actionId, template.getCourseId(), this.getUserId(),
            actionNumber, title, description, true, hasGrade
        );

        try {
            actionDAO.saveAction(action);
            template.addAction(actionId);
            templateDAO.updateTemplate(template);
            System.out.println("Added action " + actionNumber + " to template: " + templateId);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding action: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing recovery action's title and description.
     * @param actionId action ID
     * @param newTitle new action title
     * @param newDescription new action description
     * @return true if update successful
     */
    public boolean updateRecoveryAction(String actionId, String newTitle, String newDescription) {
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();

        RecoveryCourseAction action = actionDAO.loadAction(actionId);
        if (action == null) {
            System.err.println("Error: Action not found");
            return false;
        }

        // Verify instructor owns the action
        if (!action.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Instructor does not own this action");
            return false;
        }

        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(action.getCourseId())) {
            System.err.println("Error: Instructor not assigned to course");
            return false;
        }

        action.setTitle(newTitle);
        action.setDescription(newDescription);

        try {
            actionDAO.updateAction(action);
            System.out.println("Updated action: " + actionId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating action: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a recovery action from a plan template.
     * Only affects future student enrollments; existing enrollments are not modified.
     * @param templateId template ID
     * @param actionId action ID to remove
     * @return true if deletion successful
     */
    public boolean deleteActionFromTemplate(String templateId, String actionId) {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();

        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null) {
            System.err.println("Error: Template not found");
            return false;
        }

        // Verify instructor owns the template
        if (!template.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Instructor does not own this template");
            return false;
        }

        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(template.getCourseId())) {
            System.err.println("Error: Instructor not assigned to course");
            return false;
        }

        try {
            // Remove action from template
            template.removeAction(actionId);
            templateDAO.updateTemplate(template);

            // Deactivate the action (don't delete to preserve data integrity)
            RecoveryCourseAction action = actionDAO.loadAction(actionId);
            if (action != null) {
                action.setActive(false);
                actionDAO.updateAction(action);
            }

            System.out.println("Removed action " + actionId + " from template: " + templateId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting action: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets all recovery actions for a specific template.
     * @param templateId template ID
     * @return list of recovery actions
     */
    public List<RecoveryCourseAction> getTemplateActions(String templateId) {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();
        dao.RecoveryCourseActionDAO actionDAO = new dao.RecoveryCourseActionDAO();

        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null) {
            return new ArrayList<>();
        }

        List<RecoveryCourseAction> actions = new ArrayList<>();
        for (String actionId : template.getActionIds()) {
            RecoveryCourseAction action = actionDAO.loadAction(actionId);
            if (action != null) {
                actions.add(action);
            }
        }

        // Sort by action number
        actions.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

        return actions;
    }

    /**
     * Deletes a recovery plan template.
     * Only affects future student enrollments; existing enrollments are not modified.
     * @param templateId template ID to delete
     * @return true if deletion successful
     */
    public boolean deleteRecoveryPlanTemplate(String templateId) {
        dao.CourseRecoveryPlanTemplateDAO templateDAO = new dao.CourseRecoveryPlanTemplateDAO();

        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null) {
            System.err.println("Error: Template not found");
            return false;
        }

        // Verify instructor owns the template
        if (!template.getInstructorId().equals(this.getUserId())) {
            System.err.println("Error: Instructor does not own this template");
            return false;
        }

        // Verify instructor is assigned to the course
        if (!assignedCourseIds.contains(template.getCourseId())) {
            System.err.println("Error: Instructor not assigned to course");
            return false;
        }

        try {
            // Deactivate template instead of deleting
            template.deactivate();
            templateDAO.updateTemplate(template);
            System.out.println("Deactivated template: " + templateId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting template: " + e.getMessage());
            return false;
        }
    }

    // Getters and Setters

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public List<String> getAssignedCourseIds() {
        return assignedCourseIds;
    }

    public void setAssignedCourseIds(List<String> assignedCourseIds) {
        this.assignedCourseIds = assignedCourseIds != null ? assignedCourseIds : new ArrayList<>();
    }
}
