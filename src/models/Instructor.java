package models;

import enums.UserRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dao.CourseRecoveryPlanTemplateDAO;
import dao.RecoveryCourseActionDAO;
import dao.RecoveryCourseEnrollmentDAO;

/**
 * Represents an instructor in the system.
 */
public class Instructor extends User {
    private static final long serialVersionUID = 1L;

    private String instructorName;
    private List<String> assignedCourseIds;

    // DAOs for recovery plan operations
    private transient CourseRecoveryPlanTemplateDAO templateDAO;
    private transient RecoveryCourseActionDAO actionDAO;
    private transient RecoveryCourseEnrollmentDAO enrollmentDAO;

    // Default constructor
    public Instructor() {
        super();
        setRole(UserRole.INSTRUCTOR);
        this.assignedCourseIds = new ArrayList<>();
        initDAOs();
    }

    // Constructor that matches User.java
    public Instructor(String userId, String email, String password, String instructorName) {
        super(userId, UserRole.INSTRUCTOR, instructorName, email, password, true);
        this.instructorName = instructorName;
        this.assignedCourseIds = new ArrayList<>();
        initDAOs();
    }

    private void initDAOs() {
        this.templateDAO = new CourseRecoveryPlanTemplateDAO();
        this.actionDAO = new RecoveryCourseActionDAO();
        this.enrollmentDAO = new RecoveryCourseEnrollmentDAO();
    }

    private void ensureDAOs() {
        if (templateDAO == null) templateDAO = new CourseRecoveryPlanTemplateDAO();
        if (actionDAO == null) actionDAO = new RecoveryCourseActionDAO();
        if (enrollmentDAO == null) enrollmentDAO = new RecoveryCourseEnrollmentDAO();
    }

    // Getters/Setters
    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
        this.setName(instructorName); // keeps User.name in sync
    }

    public List<String> getAssignedCourseIds() {
        return assignedCourseIds;
    }

    public void setAssignedCourseIds(List<String> ids) {
        this.assignedCourseIds = ids;
    }

    /**
     * Assigns a course to this instructor.
     * @param courseId the course ID to assign
     */
    public void assignCourse(String courseId) {
        if (assignedCourseIds == null) {
            assignedCourseIds = new ArrayList<>();
        }
        if (!assignedCourseIds.contains(courseId)) {
            assignedCourseIds.add(courseId);
        }
    }

    // ==================== Recovery Plan Template Methods ====================

    /**
     * Gets all recovery plan templates created by this instructor.
     * @return list of templates
     */
    public List<CourseRecoveryPlanTemplate> getRecoveryPlanTemplates() {
        ensureDAOs();
        return templateDAO.loadTemplatesByInstructor(getUserId());
    }

    /**
     * Creates a new recovery plan template.
     * @param courseId the course ID
     * @param title the plan title
     * @param description the plan description
     * @return the created template
     */
    public CourseRecoveryPlanTemplate createRecoveryPlanTemplate(String courseId, String title, String description) {
        ensureDAOs();
        CourseRecoveryPlanTemplate template = new CourseRecoveryPlanTemplate();
        template.setTemplateId(String.valueOf(System.currentTimeMillis()));
        template.setCourseId(courseId);
        template.setInstructorId(getUserId());
        template.setPlanTitle(title);
        template.setPlanDescription(description);
        template.setActive(true);
        template.setCreatedDate(new Date());
        template.setLastModifiedDate(new Date());
        template.setActionIds(new ArrayList<>());

        templateDAO.saveTemplate(template);
        return template;
    }

    /**
     * Updates template metadata (title and description).
     * @param templateId the template ID
     * @param newTitle the new title
     * @param newDescription the new description
     * @return true if successful
     */
    public boolean updateRecoveryPlanTemplateMetadata(String templateId, String newTitle, String newDescription) {
        ensureDAOs();
        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null || !template.getInstructorId().equals(getUserId())) {
            return false;
        }

        template.setPlanTitle(newTitle);
        template.setPlanDescription(newDescription);
        template.setLastModifiedDate(new Date());

        try {
            templateDAO.updateTemplate(template);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Deletes a recovery plan template.
     * @param templateId the template ID
     * @return true if successful
     */
    public boolean deleteRecoveryPlanTemplate(String templateId) {
        ensureDAOs();
        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null || !template.getInstructorId().equals(getUserId())) {
            return false;
        }

        try {
            templateDAO.deleteTemplate(templateId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== Recovery Action Methods ====================

    /**
     * Gets all actions for a template.
     * @param templateId the template ID
     * @return list of actions
     */
    public List<RecoveryCourseAction> getTemplateActions(String templateId) {
        ensureDAOs();
        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null) {
            return new ArrayList<>();
        }
        return actionDAO.loadActionsByCourse(template.getCourseId());
    }

    /**
     * Adds an action to a template.
     * @param templateId the template ID
     * @param actionNumber the action number
     * @param title the action title
     * @param description the action description
     * @param hasGrade whether this action has a grade
     * @return true if successful
     */
    public boolean addActionToTemplate(String templateId, int actionNumber, String title, String description, boolean hasGrade) {
        ensureDAOs();
        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null || !template.getInstructorId().equals(getUserId())) {
            return false;
        }

        RecoveryCourseAction action = new RecoveryCourseAction();
        action.setId(String.valueOf(System.currentTimeMillis()));
        action.setCourseId(template.getCourseId());
        action.setInstructorId(getUserId());
        action.setActionNumber(actionNumber);
        action.setTitle(title);
        action.setDescription(description);
        action.setHasGrade(hasGrade);
        action.setActive(true);

        try {
            actionDAO.saveAction(action);

            // Update template with new action ID
            List<String> actionIds = template.getActionIds();
            if (actionIds == null) {
                actionIds = new ArrayList<>();
            }
            actionIds.add(action.getId());
            template.setActionIds(actionIds);
            template.setLastModifiedDate(new Date());
            templateDAO.updateTemplate(template);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Deletes an action from a template.
     * @param templateId the template ID
     * @param actionId the action ID
     * @return true if successful
     */
    public boolean deleteActionFromTemplate(String templateId, String actionId) {
        ensureDAOs();
        CourseRecoveryPlanTemplate template = templateDAO.loadTemplate(templateId);
        if (template == null || !template.getInstructorId().equals(getUserId())) {
            return false;
        }

        try {
            actionDAO.deleteAction(actionId);

            // Update template to remove action ID
            List<String> actionIds = template.getActionIds();
            if (actionIds != null) {
                actionIds.remove(actionId);
                template.setActionIds(actionIds);
                template.setLastModifiedDate(new Date());
                templateDAO.updateTemplate(template);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Updates a recovery action.
     * @param actionId the action ID
     * @param newTitle the new title
     * @param newDescription the new description
     * @return true if successful
     */
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

    // ==================== Recovery Notes Methods ====================

    /**
     * Updates recovery notes for a student enrollment.
     * @param studentId the student ID
     * @param courseId the course ID
     * @param actionNumber the action number
     * @param notes the notes to add
     * @return true if successful
     */
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
