package dao;

import enums.RecoveryStatus;
import models.RecoveryPlan;
import utils.FileManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for RecoveryPlan entities.
 * Handles CRUD operations for recovery plan data stored in text files.
 * Implements data persistence layer for the RecoveryPlan model.
 */
public class RecoveryPlanDAO {
    private static final String RECOVERY_PLANS_FILE = "recovery_plans.txt";
    private static final String DELIMITER = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public RecoveryPlanDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public RecoveryPlanDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Saves a recovery plan to the data file.
     * @param plan the recovery plan to save
     */
    public void saveRecoveryPlan(RecoveryPlan plan) {
        if (plan == null || plan.getPlanId() == null) {
            throw new IllegalArgumentException("Recovery plan and plan ID cannot be null");
        }

        // Check if plan already exists
        RecoveryPlan existing = loadRecoveryPlan(plan.getPlanId());
        if (existing != null) {
            updateRecoveryPlan(plan);
            return;
        }

        // Format: planId,studentId,courseId,instructorId,currentActionNumber,status,notes,startDate,targetEndDate,completedDate
        String startDateStr = plan.getStartDate() != null ? DATE_FORMAT.format(plan.getStartDate()) : "";
        String targetEndDateStr = plan.getTargetEndDate() != null ? DATE_FORMAT.format(plan.getTargetEndDate()) : "";
        String completedDateStr = plan.getCompletedDate() != null ? DATE_FORMAT.format(plan.getCompletedDate()) : "";
        String notes = plan.getNotes() != null ? plan.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

        String data = String.format("%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%s%s%s",
                plan.getPlanId(), DELIMITER,
                plan.getStudentId(), DELIMITER,
                plan.getCourseId(), DELIMITER,
                plan.getInstructorId(), DELIMITER,
                plan.getCurrentActionNumber(), DELIMITER,
                plan.getStatus(), DELIMITER,
                notes, DELIMITER,
                startDateStr, DELIMITER,
                targetEndDateStr, DELIMITER,
                completedDateStr);

        fileManager.appendToFile(RECOVERY_PLANS_FILE, data);
    }

    /**
     * Loads a recovery plan by plan ID.
     * @param planId the plan ID
     * @return the RecoveryPlan object, or null if not found
     */
    public RecoveryPlan loadRecoveryPlan(String planId) {
        if (planId == null || planId.trim().isEmpty()) {
            return null;
        }

        String content = fileManager.loadFromTextFile(RECOVERY_PLANS_FILE);
        if (content.isEmpty()) {
            return null;
        }

        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 10 && parts[0].equals(planId)) {
                return parseRecoveryPlan(parts);
            }
        }

        return null;
    }

    /**
     * Loads all recovery plans for a specific student.
     * @param studentId the student ID
     * @return list of recovery plans
     */
    public List<RecoveryPlan> loadPlansByStudent(String studentId) {
        List<RecoveryPlan> allPlans = loadAllRecoveryPlans();
        List<RecoveryPlan> studentPlans = new ArrayList<>();

        for (RecoveryPlan plan : allPlans) {
            if (plan.getStudentId() != null && plan.getStudentId().equals(studentId)) {
                studentPlans.add(plan);
            }
        }

        return studentPlans;
    }

    /**
     * Loads all recovery plans managed by a specific instructor.
     * @param instructorId the instructor ID
     * @return list of recovery plans
     */
    public List<RecoveryPlan> loadPlansByInstructor(String instructorId) {
        List<RecoveryPlan> allPlans = loadAllRecoveryPlans();
        List<RecoveryPlan> instructorPlans = new ArrayList<>();

        for (RecoveryPlan plan : allPlans) {
            if (plan.getInstructorId() != null && plan.getInstructorId().equals(instructorId)) {
                instructorPlans.add(plan);
            }
        }

        return instructorPlans;
    }

    /**
     * Loads all recovery plans from the data file.
     * @return list of all recovery plans
     */
    public List<RecoveryPlan> loadAllRecoveryPlans() {
        List<RecoveryPlan> plans = new ArrayList<>();
        String content = fileManager.loadFromTextFile(RECOVERY_PLANS_FILE);

        if (content.isEmpty()) {
            return plans;
        }

        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 10) {
                RecoveryPlan plan = parseRecoveryPlan(parts);
                if (plan != null) {
                    plans.add(plan);
                }
            }
        }

        return plans;
    }

    /**
     * Updates an existing recovery plan.
     * @param plan the recovery plan with updated data
     */
    public void updateRecoveryPlan(RecoveryPlan plan) {
        if (plan == null || plan.getPlanId() == null) {
            throw new IllegalArgumentException("Recovery plan and plan ID cannot be null");
        }

        List<RecoveryPlan> plans = loadAllRecoveryPlans();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (RecoveryPlan existing : plans) {
            if (existing.getPlanId().equals(plan.getPlanId())) {
                // Update with new data
                String startDateStr = plan.getStartDate() != null ? DATE_FORMAT.format(plan.getStartDate()) : "";
                String targetEndDateStr = plan.getTargetEndDate() != null ? DATE_FORMAT.format(plan.getTargetEndDate()) : "";
                String completedDateStr = plan.getCompletedDate() != null ? DATE_FORMAT.format(plan.getCompletedDate()) : "";
                String notes = plan.getNotes() != null ? plan.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%s%s%s\n",
                        plan.getPlanId(), DELIMITER,
                        plan.getStudentId(), DELIMITER,
                        plan.getCourseId(), DELIMITER,
                        plan.getInstructorId(), DELIMITER,
                        plan.getCurrentActionNumber(), DELIMITER,
                        plan.getStatus(), DELIMITER,
                        notes, DELIMITER,
                        startDateStr, DELIMITER,
                        targetEndDateStr, DELIMITER,
                        completedDateStr));
                found = true;
            } else {
                // Keep existing data
                String startDateStr = existing.getStartDate() != null ? DATE_FORMAT.format(existing.getStartDate()) : "";
                String targetEndDateStr = existing.getTargetEndDate() != null ? DATE_FORMAT.format(existing.getTargetEndDate()) : "";
                String completedDateStr = existing.getCompletedDate() != null ? DATE_FORMAT.format(existing.getCompletedDate()) : "";
                String notes = existing.getNotes() != null ? existing.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%s%s%s\n",
                        existing.getPlanId(), DELIMITER,
                        existing.getStudentId(), DELIMITER,
                        existing.getCourseId(), DELIMITER,
                        existing.getInstructorId(), DELIMITER,
                        existing.getCurrentActionNumber(), DELIMITER,
                        existing.getStatus(), DELIMITER,
                        notes, DELIMITER,
                        startDateStr, DELIMITER,
                        targetEndDateStr, DELIMITER,
                        completedDateStr));
            }
        }

        if (!found) {
            throw new IllegalStateException("Recovery plan not found: " + plan.getPlanId());
        }

        fileManager.saveToTextFile(RECOVERY_PLANS_FILE, updatedContent.toString());
    }

    /**
     * Deletes a recovery plan from the data file.
     * @param planId the plan ID to delete
     */
    public void deleteRecoveryPlan(String planId) {
        if (planId == null || planId.trim().isEmpty()) {
            throw new IllegalArgumentException("Plan ID cannot be null or empty");
        }

        List<RecoveryPlan> plans = loadAllRecoveryPlans();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (RecoveryPlan plan : plans) {
            if (!plan.getPlanId().equals(planId)) {
                String startDateStr = plan.getStartDate() != null ? DATE_FORMAT.format(plan.getStartDate()) : "";
                String targetEndDateStr = plan.getTargetEndDate() != null ? DATE_FORMAT.format(plan.getTargetEndDate()) : "";
                String completedDateStr = plan.getCompletedDate() != null ? DATE_FORMAT.format(plan.getCompletedDate()) : "";
                String notes = plan.getNotes() != null ? plan.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%s%s%s\n",
                        plan.getPlanId(), DELIMITER,
                        plan.getStudentId(), DELIMITER,
                        plan.getCourseId(), DELIMITER,
                        plan.getInstructorId(), DELIMITER,
                        plan.getCurrentActionNumber(), DELIMITER,
                        plan.getStatus(), DELIMITER,
                        notes, DELIMITER,
                        startDateStr, DELIMITER,
                        targetEndDateStr, DELIMITER,
                        completedDateStr));
            } else {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("Recovery plan not found: " + planId);
        }

        fileManager.saveToTextFile(RECOVERY_PLANS_FILE, updatedContent.toString());
    }

    /**
     * Parses a CSV line into a RecoveryPlan object.
     * @param parts the CSV parts
     * @return the RecoveryPlan object
     */
    private RecoveryPlan parseRecoveryPlan(String[] parts) {
        try {
            RecoveryPlan plan = new RecoveryPlan();
            plan.setPlanId(parts[0]);
            plan.setStudentId(parts[1]);
            plan.setCourseId(parts[2]);
            plan.setInstructorId(parts[3]);
            plan.setCurrentActionNumber(Integer.parseInt(parts[4]));
            plan.setStatus(RecoveryStatus.valueOf(parts[5]));

            // Parse notes (unescape newlines and commas)
            if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                String notes = parts[6].replace("\\n", "\n").replace("\\,", ",");
                plan.setNotes(notes);
            }

            // Parse dates
            if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                plan.setStartDate(DATE_FORMAT.parse(parts[7]));
            }
            if (parts.length > 8 && !parts[8].trim().isEmpty()) {
                plan.setTargetEndDate(DATE_FORMAT.parse(parts[8]));
            }
            if (parts.length > 9 && !parts[9].trim().isEmpty()) {
                plan.setCompletedDate(DATE_FORMAT.parse(parts[9]));
            }

            return plan;
        } catch (ParseException | IllegalArgumentException e) {
            System.err.println("Error parsing recovery plan: " + e.getMessage());
            return null;
        }
    }
}
