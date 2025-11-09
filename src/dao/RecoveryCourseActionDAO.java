package dao;

import models.RecoveryCourseAction;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for RecoveryCourseAction entities.
 * Handles CRUD operations for recovery action data stored in text files.
 * Implements data persistence layer for the RecoveryCourseAction model.
 */
public class RecoveryCourseActionDAO {
    private static final String ACTIONS_FILE = "recovery_courses_actions.txt";
    private static final String DELIMITER = ",";
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public RecoveryCourseActionDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public RecoveryCourseActionDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Saves a recovery action to the data file.
     * @param action the recovery action to save
     */
    public void saveAction(RecoveryCourseAction action) {
        if (action == null || action.getId() == null) {
            throw new IllegalArgumentException("Action and action ID cannot be null");
        }

        // Check if action already exists
        RecoveryCourseAction existing = loadAction(action.getId());
        if (existing != null) {
            updateAction(action);
            return;
        }

        // Format: id,course_id,instructor_id,action_number,title,description,is_active,have_grade
        String title = action.getTitle() != null ? action.getTitle().replace(",", "\\,") : "";
        String description = action.getDescription() != null ? action.getDescription().replace(",", "\\,").replace("\n", "\\n") : "";

        String data = String.format("%s%s%s%s%s%s%d%s%s%s%s%s%b%s%b",
                action.getId(), DELIMITER,
                action.getCourseId(), DELIMITER,
                action.getInstructorId(), DELIMITER,
                action.getActionNumber(), DELIMITER,
                title, DELIMITER,
                description, DELIMITER,
                action.isActive(), DELIMITER,
                action.isHasGrade());

        fileManager.appendToFile(ACTIONS_FILE, data);
    }

    /**
     * Loads a specific action by ID.
     * @param actionId the action ID
     * @return the RecoveryCourseAction object, or null if not found
     */
    public RecoveryCourseAction loadAction(String actionId) {
        if (actionId == null || actionId.trim().isEmpty()) {
            return null;
        }

        String content = fileManager.loadFromTextFile(ACTIONS_FILE);
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
            if (parts.length >= 8 && parts[0].equals(actionId)) {
                return parseAction(parts);
            }
        }

        return null;
    }

    /**
     * Loads all recovery actions for a specific course.
     * @param courseId the course ID
     * @return list of recovery actions
     */
    public List<RecoveryCourseAction> loadActionsByCourse(String courseId) {
        List<RecoveryCourseAction> allActions = loadAllActions();
        List<RecoveryCourseAction> courseActions = new ArrayList<>();

        for (RecoveryCourseAction action : allActions) {
            if (action.getCourseId() != null && action.getCourseId().equals(courseId)) {
                courseActions.add(action);
            }
        }

        // Sort by action number
        courseActions.sort((a, b) -> Integer.compare(a.getActionNumber(), b.getActionNumber()));

        return courseActions;
    }

    /**
     * Loads all recovery actions from the data file.
     * @return list of all recovery actions
     */
    public List<RecoveryCourseAction> loadAllActions() {
        List<RecoveryCourseAction> actions = new ArrayList<>();
        String content = fileManager.loadFromTextFile(ACTIONS_FILE);

        if (content.isEmpty()) {
            return actions;
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
            if (parts.length >= 8) {
                RecoveryCourseAction action = parseAction(parts);
                if (action != null) {
                    actions.add(action);
                }
            }
        }

        return actions;
    }

    /**
     * Updates an existing recovery action.
     * @param action the action with updated data
     */
    public void updateAction(RecoveryCourseAction action) {
        if (action == null || action.getId() == null) {
            throw new IllegalArgumentException("Action and action ID cannot be null");
        }

        List<RecoveryCourseAction> actions = loadAllActions();
        StringBuilder updatedContent = new StringBuilder();

        // Write header
        updatedContent.append("id,course_id,instructor_id,action_number,title,description,is_active,have_grade\n");

        boolean found = false;
        for (RecoveryCourseAction existing : actions) {
            if (existing.getId().equals(action.getId())) {
                updatedContent.append(formatAction(action));
                found = true;
            } else {
                updatedContent.append(formatAction(existing));
            }
        }

        if (!found) {
            throw new IllegalStateException("Action not found: " + action.getId());
        }

        fileManager.saveToTextFile(ACTIONS_FILE, updatedContent.toString());
    }

    /**
     * Deletes a recovery action from the data file.
     * @param actionId the action ID to delete
     */
    public void deleteAction(String actionId) {
        if (actionId == null || actionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Action ID cannot be null or empty");
        }

        List<RecoveryCourseAction> actions = loadAllActions();
        StringBuilder updatedContent = new StringBuilder();

        // Write header
        updatedContent.append("id,course_id,instructor_id,action_number,title,description,is_active,have_grade\n");

        boolean found = false;
        for (RecoveryCourseAction action : actions) {
            if (!action.getId().equals(actionId)) {
                updatedContent.append(formatAction(action));
            } else {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("Action not found: " + actionId);
        }

        fileManager.saveToTextFile(ACTIONS_FILE, updatedContent.toString());
    }

    /**
     * Formats an action for file storage.
     * @param action action to format
     * @return CSV formatted string
     */
    private String formatAction(RecoveryCourseAction action) {
        String title = action.getTitle() != null ? action.getTitle().replace(",", "\\,") : "";
        String description = action.getDescription() != null ? action.getDescription().replace(",", "\\,").replace("\n", "\\n") : "";

        return String.format("%s%s%s%s%s%s%d%s%s%s%s%s%b%s%b\n",
                action.getId(), DELIMITER,
                action.getCourseId(), DELIMITER,
                action.getInstructorId(), DELIMITER,
                action.getActionNumber(), DELIMITER,
                title, DELIMITER,
                description, DELIMITER,
                action.isActive(), DELIMITER,
                action.isHasGrade());
    }

    /**
     * Parses a CSV line into a RecoveryCourseAction object.
     * Format: id,course_id,instructor_id,action_number,title,description,is_active,have_grade
     * @param parts the CSV parts
     * @return the action object
     */
    private RecoveryCourseAction parseAction(String[] parts) {
        try {
            RecoveryCourseAction action = new RecoveryCourseAction();
            action.setId(parts[0].trim());
            action.setCourseId(parts[1].trim());
            action.setInstructorId(parts[2].trim());
            action.setActionNumber(Integer.parseInt(parts[3].trim()));

            // Unescape title and description
            String title = parts.length > 4 ? parts[4].replace("\\,", ",") : "";
            action.setTitle(title);

            String description = parts.length > 5 ? parts[5].replace("\\,", ",").replace("\\n", "\n") : "";
            action.setDescription(description);

            if (parts.length > 6) {
                action.setActive(Boolean.parseBoolean(parts[6].trim()));
            }

            if (parts.length > 7) {
                action.setHasGrade(Boolean.parseBoolean(parts[7].trim()));
            }

            return action;
        } catch (Exception e) {
            System.err.println("Error parsing action: " + e.getMessage());
            return null;
        }
    }
}
