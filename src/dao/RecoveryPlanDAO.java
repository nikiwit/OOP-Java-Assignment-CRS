package dao;

import enums.RecoveryStatus;
import models.RecoveryPlan;
import utils.FileManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for RecoveryPlan entities.
public class RecoveryPlanDAO {
    private static final String RECOVERY_PLANS_FILE = "recovery_plans.txt";
    private static final String PUNCTUATION = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FileManager fileManager;

    public RecoveryPlanDAO() {
        this.fileManager = new FileManager();
    }

    public RecoveryPlanDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    //  Saves a recovery plan to the data file.
    public void saveRecoveryPlan(RecoveryPlan plan) {
        if (plan == null || plan.getPlanId() == null) {
            throw new IllegalArgumentException("Recovery plan and plan ID cannot be null");
        }

        RecoveryPlan existing = loadRecoveryPlan(plan.getPlanId());
        if (existing != null) {
            updateRecoveryPlan(plan);
            return;
        }

        String startDateStr = plan.getStartDate() != null ? DATE_FORMAT.format(plan.getStartDate()) : "";
        String targetEndDateStr = plan.getTargetEndDate() != null ? DATE_FORMAT.format(plan.getTargetEndDate()) : "";
        String completedDateStr = plan.getCompletedDate() != null ? DATE_FORMAT.format(plan.getCompletedDate()) : "";
        String notes = plan.getNotes() != null ? plan.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

        StringBuilder sb = new StringBuilder();
        sb.append(plan.getPlanId()).append(PUNCTUATION);
        sb.append(plan.getStudentId()).append(PUNCTUATION);
        sb.append(plan.getCourseId()).append(PUNCTUATION);
        sb.append(plan.getInstructorId()).append(PUNCTUATION);

        sb.append(plan.getCurrentActionNumber()).append(PUNCTUATION);
        sb.append(plan.getStatus()).append(PUNCTUATION);

        String safeNotes = notes != null
                ? notes.replace(",", "\\,").replace("\n", " ").trim()
                : "";
        sb.append(safeNotes).append(PUNCTUATION);

        sb.append(startDateStr).append(PUNCTUATION);
        sb.append(targetEndDateStr).append(PUNCTUATION);
        sb.append(completedDateStr);
        String data = sb.toString();
        fileManager.appendToFile(RECOVERY_PLANS_FILE, data);
    }

    
    // Loads a recovery plan by plan ID.
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

            String[] parts = line.split(PUNCTUATION);
            if (parts.length >= 10 && parts[0].equals(planId)) {
                return parseRecoveryPlan(parts);
            }
        }
        return null;
    }

    // Loads all recovery plans for a specific student.
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

    // Loads all recovery plans managed by a specific instructor.
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

     // Loads all recovery plans from the data file.
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

            String[] parts = line.split(PUNCTUATION);
            if (parts.length >= 10) {
                RecoveryPlan plan = parseRecoveryPlan(parts);
                if (plan != null) {
                    plans.add(plan);
                }
            }
        }

        return plans;
    }

    // Updates an existing recovery plan.
    public void updateRecoveryPlan(RecoveryPlan plan) {
        if (plan == null || plan.getPlanId() == null) {
            throw new IllegalArgumentException("Recovery plan and plan ID cannot be null");
        }

        List<RecoveryPlan> plans = loadAllRecoveryPlans();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (RecoveryPlan existing : plans) {
            if (existing.getPlanId().equals(plan.getPlanId())) {
                String startDateStr = plan.getStartDate() != null ? DATE_FORMAT.format(plan.getStartDate()) : "";
                String targetEndDateStr = plan.getTargetEndDate() != null ? DATE_FORMAT.format(plan.getTargetEndDate()) : "";
                String completedDateStr = plan.getCompletedDate() != null ? DATE_FORMAT.format(plan.getCompletedDate()) : "";
                String notes = plan.getNotes() != null ? plan.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%s%s%s\n",
                        plan.getPlanId(), PUNCTUATION,
                        plan.getStudentId(), PUNCTUATION,
                        plan.getCourseId(), PUNCTUATION,
                        plan.getInstructorId(), PUNCTUATION,
                        plan.getCurrentActionNumber(), PUNCTUATION,
                        plan.getStatus(), PUNCTUATION,
                        notes, PUNCTUATION,
                        startDateStr, PUNCTUATION,
                        targetEndDateStr, PUNCTUATION,
                        completedDateStr));
                found = true;
            } else {
                String startDateStr = existing.getStartDate() != null ? DATE_FORMAT.format(existing.getStartDate()) : "";
                String targetEndDateStr = existing.getTargetEndDate() != null ? DATE_FORMAT.format(existing.getTargetEndDate()) : "";
                String completedDateStr = existing.getCompletedDate() != null ? DATE_FORMAT.format(existing.getCompletedDate()) : "";
                String notes = existing.getNotes() != null ? existing.getNotes().replace("\n", "\\n").replace(",", "\\,") : "";

                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%s%s%s\n",
                        existing.getPlanId(), PUNCTUATION,
                        existing.getStudentId(), PUNCTUATION,
                        existing.getCourseId(), PUNCTUATION,
                        existing.getInstructorId(), PUNCTUATION,
                        existing.getCurrentActionNumber(), PUNCTUATION,
                        existing.getStatus(), PUNCTUATION,
                        notes, PUNCTUATION,
                        startDateStr, PUNCTUATION,
                        targetEndDateStr, PUNCTUATION,
                        completedDateStr));
            }
        }

        if (!found) {
            throw new IllegalStateException("Recovery plan not found: " + plan.getPlanId());
        }

        fileManager.saveToTextFile(RECOVERY_PLANS_FILE, updatedContent.toString());
    }

     // Deletes a recovery plan from the data file.
    public void deleteRecoveryPlan(String planId) {
        if (planId == null || planId.trim().isEmpty()) {
            throw new IllegalArgumentException("Plan ID cannot be null or empty");
        }

        List<RecoveryPlan> plans = loadAllRecoveryPlans();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;

        for (RecoveryPlan plan : plans) {

            // If this is NOT the plan we want to delete/update
            if (!plan.getPlanId().equals(planId)) {

                StringBuilder row = new StringBuilder();

                // Convert dates to strings safely
                String startDateStr = plan.getStartDate() != null ? DATE_FORMAT.format(plan.getStartDate()) : "";
                String targetEndDateStr = plan.getTargetEndDate() != null ? DATE_FORMAT.format(plan.getTargetEndDate()) : "";
                String completedDateStr = plan.getCompletedDate() != null ? DATE_FORMAT.format(plan.getCompletedDate()) : "";

                // Escape notes (avoid CSV breaking)
                String notes = plan.getNotes() != null
                        ? plan.getNotes().replace("\n", "\\n").replace(",", "\\,")
                        : "";

                // Build line step-by-step
                row.append(plan.getPlanId()).append(PUNCTUATION);
                row.append(plan.getStudentId()).append(PUNCTUATION);
                row.append(plan.getCourseId()).append(PUNCTUATION);
                row.append(plan.getInstructorId()).append(PUNCTUATION);

                row.append(plan.getCurrentActionNumber()).append(PUNCTUATION);
                row.append(plan.getStatus()).append(PUNCTUATION);
                row.append(notes).append(PUNCTUATION);

                row.append(startDateStr).append(PUNCTUATION);
                row.append(targetEndDateStr).append(PUNCTUATION);
                row.append(completedDateStr);

                // Add newline at the end
                updatedContent.append(row).append("\n");

            } else {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("Recovery plan not found: " + planId);
        }

        fileManager.saveToTextFile(RECOVERY_PLANS_FILE, updatedContent.toString());
    }

    private RecoveryPlan parseRecoveryPlan(String[] parts) {
        try {
            RecoveryPlan plan = new RecoveryPlan();
            plan.setPlanId(parts[0]);
            plan.setStudentId(parts[1]);
            plan.setCourseId(parts[2]);
            plan.setInstructorId(parts[3]);
            plan.setCurrentActionNumber(Integer.parseInt(parts[4]));
            plan.setStatus(RecoveryStatus.valueOf(parts[5]));

            if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                String notes = parts[6].replace("\\n", "\n").replace("\\,", ",");
                plan.setNotes(notes);
            }

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
