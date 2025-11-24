package models;

import enums.UserRole;
import java.util.ArrayList;
import java.util.List;
import dao.RecoveryCourseActionDAO;
import dao.RecoveryCourseEnrollmentDAO;

public class Instructor extends User {
    private static final long serialVersionUID = 1L;

    private String instructorName;
    private List<String> assignedCourseIds;

    private transient RecoveryCourseActionDAO actionDAO;
    private transient RecoveryCourseEnrollmentDAO enrollmentDAO;

    public Instructor() {
        super();
        setRole(UserRole.INSTRUCTOR);
        this.assignedCourseIds = new ArrayList<>();
        initDAOs();
    }

    public Instructor(String userId, String email, String password, String instructorName) {
        super(userId, UserRole.INSTRUCTOR, instructorName, email, password, true);
        this.instructorName = instructorName;
        this.assignedCourseIds = new ArrayList<>();
        initDAOs();
    }

    private void initDAOs() {
        this.actionDAO = new RecoveryCourseActionDAO();
        this.enrollmentDAO = new RecoveryCourseEnrollmentDAO();
    }

    private void ensureDAOs() {
        if (actionDAO == null) actionDAO = new RecoveryCourseActionDAO();
        if (enrollmentDAO == null) enrollmentDAO = new RecoveryCourseEnrollmentDAO();
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
        this.setName(instructorName);
    }

    public List<String> getAssignedCourseIds() {
        return assignedCourseIds;
    }

    public void setAssignedCourseIds(List<String> ids) {
        this.assignedCourseIds = ids;
    }

    public void assignCourse(String courseId) {
        if (assignedCourseIds == null) {
            assignedCourseIds = new ArrayList<>();
        }
        if (!assignedCourseIds.contains(courseId)) {
            assignedCourseIds.add(courseId);
        }
    }

    public List<RecoveryCourseAction> getActionsForCourse(String courseId) {
        ensureDAOs();
        return actionDAO.loadActionsByCourse(courseId);
    }

    public boolean updateRecoveryAction(String actionId, String newTitle, String newDescription) {
        ensureDAOs();
        RecoveryCourseAction action = actionDAO.loadAction(actionId);
        if (action == null || !action.getInstructorId().equals(getUserId())) {
            return false;
        }

        action.setTitle(newTitle);
        action.setDescription(newDescription);

        try {
            actionDAO.updateAction(action);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateRecoveryNotes(String studentId, String courseId, int actionNumber, String notes) {
        ensureDAOs();
        List<RecoveryCourseEnrollment> enrollments = enrollmentDAO.loadEnrollmentsByStudentAndCourse(studentId, courseId);

        for (RecoveryCourseEnrollment enrollment : enrollments) {
            if (enrollment.getActionNumber() == actionNumber) {
                return enrollmentDAO.updateEnrollmentNotes(enrollment.getId(), notes);
            }
        }
        return false;
    }
}
