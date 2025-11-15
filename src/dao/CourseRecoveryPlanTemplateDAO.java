package dao;

import models.CourseRecoveryPlanTemplate;
import utils.FileManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data Access Object for CourseRecoveryPlanTemplate entities.
 * Handles CRUD operations for course recovery plan templates stored in text files.
 * Manages the uniform recovery plan templates that instructors create for courses.
 */
public class CourseRecoveryPlanTemplateDAO {
    private static final String TEMPLATES_FILE = "recovery_plan_templates.txt";
    private static final String DELIMITER = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public CourseRecoveryPlanTemplateDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public CourseRecoveryPlanTemplateDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Saves a recovery plan template to the data file.
     * @param template the template to save
     */
    public void saveTemplate(CourseRecoveryPlanTemplate template) {
        if (template == null || template.getTemplateId() == null) {
            throw new IllegalArgumentException("Template and template ID cannot be null");
        }

        // Check if template already exists
        CourseRecoveryPlanTemplate existing = loadTemplate(template.getTemplateId());
        if (existing != null) {
            updateTemplate(template);
            return;
        }

        // Format: templateId,courseId,instructorId,planTitle,planDescription,actionIds,isActive,createdDate,lastModifiedDate
        String actionIdsStr = String.join(";", template.getActionIds());
        String createdDateStr = template.getCreatedDate() != null ? DATE_FORMAT.format(template.getCreatedDate()) : "";
        String lastModifiedDateStr = template.getLastModifiedDate() != null ? DATE_FORMAT.format(template.getLastModifiedDate()) : "";

        // Escape commas and newlines in title and description
        String title = template.getPlanTitle() != null ? template.getPlanTitle().replace("\n", "\\n").replace(",", "\\,") : "";
        String description = template.getPlanDescription() != null ? template.getPlanDescription().replace("\n", "\\n").replace(",", "\\,") : "";

        String data = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%b%s%s%s%s",
                template.getTemplateId(), DELIMITER,
                template.getCourseId(), DELIMITER,
                template.getInstructorId(), DELIMITER,
                title, DELIMITER,
                description, DELIMITER,
                actionIdsStr, DELIMITER,
                template.isActive(), DELIMITER,
                createdDateStr, DELIMITER,
                lastModifiedDateStr);

        fileManager.appendToFile(TEMPLATES_FILE, data);
    }

    /**
     * Loads a template by ID.
     * @param templateId the template ID
     * @return the CourseRecoveryPlanTemplate object, or null if not found
     */
    public CourseRecoveryPlanTemplate loadTemplate(String templateId) {
        if (templateId == null || templateId.trim().isEmpty()) {
            return null;
        }

        String content = fileManager.loadFromTextFile(TEMPLATES_FILE);
        if (content.isEmpty()) {
            return null;
        }

        String[] lines = content.split("\n");
        boolean isFirstLine = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Skip header line
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 9 && parts[0].equals(templateId)) {
                return parseTemplate(parts);
            }
        }

        return null;
    }

    /**
     * Loads all templates for a specific course.
     * @param courseId the course ID
     * @return list of templates
     */
    public List<CourseRecoveryPlanTemplate> loadTemplatesByCourse(String courseId) {
        List<CourseRecoveryPlanTemplate> allTemplates = loadAllTemplates();
        List<CourseRecoveryPlanTemplate> courseTemplates = new ArrayList<>();

        for (CourseRecoveryPlanTemplate template : allTemplates) {
            if (template.getCourseId() != null && template.getCourseId().equals(courseId)) {
                courseTemplates.add(template);
            }
        }

        return courseTemplates;
    }

    /**
     * Loads all templates managed by a specific instructor.
     * @param instructorId the instructor ID
     * @return list of templates
     */
    public List<CourseRecoveryPlanTemplate> loadTemplatesByInstructor(String instructorId) {
        List<CourseRecoveryPlanTemplate> allTemplates = loadAllTemplates();
        List<CourseRecoveryPlanTemplate> instructorTemplates = new ArrayList<>();

        for (CourseRecoveryPlanTemplate template : allTemplates) {
            if (template.getInstructorId() != null && template.getInstructorId().equals(instructorId)) {
                instructorTemplates.add(template);
            }
        }

        return instructorTemplates;
    }

    /**
     * Loads all active templates for a course.
     * @param courseId the course ID
     * @return list of active templates
     */
    public List<CourseRecoveryPlanTemplate> loadActiveTemplatesByCourse(String courseId) {
        List<CourseRecoveryPlanTemplate> courseTemplates = loadTemplatesByCourse(courseId);
        List<CourseRecoveryPlanTemplate> activeTemplates = new ArrayList<>();

        for (CourseRecoveryPlanTemplate template : courseTemplates) {
            if (template.isActive()) {
                activeTemplates.add(template);
            }
        }

        return activeTemplates;
    }

    /**
     * Loads all templates from the data file.
     * @return list of all templates
     */
    public List<CourseRecoveryPlanTemplate> loadAllTemplates() {
        List<CourseRecoveryPlanTemplate> templates = new ArrayList<>();
        String content = fileManager.loadFromTextFile(TEMPLATES_FILE);

        if (content.isEmpty()) {
            return templates;
        }

        String[] lines = content.split("\n");
        boolean isFirstLine = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Skip header line
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 9) {
                CourseRecoveryPlanTemplate template = parseTemplate(parts);
                if (template != null) {
                    templates.add(template);
                }
            }
        }

        return templates;
    }

    /**
     * Updates an existing template.
     * @param template the template with updated data
     */
    public void updateTemplate(CourseRecoveryPlanTemplate template) {
        if (template == null || template.getTemplateId() == null) {
            throw new IllegalArgumentException("Template and template ID cannot be null");
        }

        List<CourseRecoveryPlanTemplate> templates = loadAllTemplates();
        StringBuilder updatedContent = new StringBuilder();

        // Write header
        updatedContent.append("templateId,courseId,instructorId,planTitle,planDescription,actionIds,isActive,createdDate,lastModifiedDate\n");

        boolean found = false;
        for (CourseRecoveryPlanTemplate existing : templates) {
            if (existing.getTemplateId().equals(template.getTemplateId())) {
                updatedContent.append(formatTemplate(template));
                found = true;
            } else {
                updatedContent.append(formatTemplate(existing));
            }
        }

        if (!found) {
            throw new IllegalStateException("Template not found: " + template.getTemplateId());
        }

        fileManager.saveToTextFile(TEMPLATES_FILE, updatedContent.toString());
    }

    /**
     * Deletes a template from the data file.
     * @param templateId the template ID to delete
     */
    public void deleteTemplate(String templateId) {
        if (templateId == null || templateId.trim().isEmpty()) {
            throw new IllegalArgumentException("Template ID cannot be null or empty");
        }

        List<CourseRecoveryPlanTemplate> templates = loadAllTemplates();
        StringBuilder updatedContent = new StringBuilder();

        // Write header
        updatedContent.append("templateId,courseId,instructorId,planTitle,planDescription,actionIds,isActive,createdDate,lastModifiedDate\n");

        boolean found = false;
        for (CourseRecoveryPlanTemplate template : templates) {
            if (!template.getTemplateId().equals(templateId)) {
                updatedContent.append(formatTemplate(template));
            } else {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("Template not found: " + templateId);
        }

        fileManager.saveToTextFile(TEMPLATES_FILE, updatedContent.toString());
    }

    /**
     * Formats a template for file storage.
     * @param template template to format
     * @return CSV formatted string
     */
    private String formatTemplate(CourseRecoveryPlanTemplate template) {
        String actionIdsStr = String.join(";", template.getActionIds());
        String createdDateStr = template.getCreatedDate() != null ? DATE_FORMAT.format(template.getCreatedDate()) : "";
        String lastModifiedDateStr = template.getLastModifiedDate() != null ? DATE_FORMAT.format(template.getLastModifiedDate()) : "";

        // Escape commas and newlines
        String title = template.getPlanTitle() != null ? template.getPlanTitle().replace("\n", "\\n").replace(",", "\\,") : "";
        String description = template.getPlanDescription() != null ? template.getPlanDescription().replace("\n", "\\n").replace(",", "\\,") : "";

        return String.format("%s%s%s%s%s%s%s%s%s%s%s%s%b%s%s%s%s\n",
                template.getTemplateId(), DELIMITER,
                template.getCourseId(), DELIMITER,
                template.getInstructorId(), DELIMITER,
                title, DELIMITER,
                description, DELIMITER,
                actionIdsStr, DELIMITER,
                template.isActive(), DELIMITER,
                createdDateStr, DELIMITER,
                lastModifiedDateStr);
    }

    /**
     * Parses a CSV line into a CourseRecoveryPlanTemplate object.
     * Format: templateId,courseId,instructorId,planTitle,planDescription,actionIds,isActive,createdDate,lastModifiedDate
     * @param parts the CSV parts
     * @return the template object
     */
    private CourseRecoveryPlanTemplate parseTemplate(String[] parts) {
        try {
            CourseRecoveryPlanTemplate template = new CourseRecoveryPlanTemplate();
            template.setTemplateId(parts[0].trim());
            template.setCourseId(parts[1].trim());
            template.setInstructorId(parts[2].trim());

            // Unescape title and description
            String title = parts.length > 3 ? parts[3].replace("\\n", "\n").replace("\\,", ",") : "";
            template.setPlanTitle(title);

            String description = parts.length > 4 ? parts[4].replace("\\n", "\n").replace("\\,", ",") : "";
            template.setPlanDescription(description);

            // Parse action IDs
            if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                List<String> actionIds = new ArrayList<>(Arrays.asList(parts[5].split(";")));
                template.setActionIds(actionIds);
            }

            // Parse isActive
            if (parts.length > 6) {
                template.setActive(Boolean.parseBoolean(parts[6].trim()));
            }

            // Parse dates
            if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                template.setCreatedDate(DATE_FORMAT.parse(parts[7]));
            }
            if (parts.length > 8 && !parts[8].trim().isEmpty()) {
                template.setLastModifiedDate(DATE_FORMAT.parse(parts[8]));
            }

            return template;
        } catch (ParseException | IllegalArgumentException e) {
            System.err.println("Error parsing template: " + e.getMessage());
            return null;
        }
    }
}
