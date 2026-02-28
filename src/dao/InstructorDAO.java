package dao;

import models.Instructor;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Instructor records.
 *
 *      File Format (instructors.txt):
 *      InstructorID,InstructorName
 *
 * Only 2 columns.
 */
public class InstructorDAO {

    private static final String INSTRUCTORS_FILE = "instructors.txt";
    private static final String PUNCTUATION = ",";
    private FileManager fileManager;

    // Constructor 
    public InstructorDAO() {
        this.fileManager = new FileManager();
    }

    // Constructor (custom FileManager) 
    public InstructorDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void saveInstructor(Instructor instructor) {

        if (instructor == null || instructor.getUserId() == null) {
            throw new IllegalArgumentException("Instructor or Instructor ID cannot be null.");
        }

        try {
            String line = instructor.getUserId() + PUNCTUATION + instructor.getInstructorName() + "\n";
            fileManager.appendToFile(INSTRUCTORS_FILE, line);
        } catch (Exception ex) {
            System.err.println("Error saving instructor: " + ex.getMessage());
        }
    }

    public Instructor loadInstructor(String instructorId) {

        if (instructorId == null || instructorId.trim().isEmpty()) {
            return null;
        }

        try {
            String content = fileManager.loadFromTextFile(INSTRUCTORS_FILE);

            if (content.length() == 0) {
                return null;
            }

            String[] lines = content.split("\n");
            boolean isFirstLine = true;

            for (int i = 0; i < lines.length; i++) {

                String line = lines[i].trim();
                if (line.length() == 0) {
                    continue;
                }

                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(PUNCTUATION);
                if (parts.length < 2) {
                    continue;
                }

                if (parts[0].trim().equals(instructorId)) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    return new Instructor(id, "", "", name);
                }
            }

        } catch (Exception ex) {
            System.err.println("Error loading instructor: " + ex.getMessage());
        }

        return null;
    }
    public List<Instructor> loadAllInstructors() {

        List<Instructor> instructors = new ArrayList<Instructor>();

        try {
            String content = fileManager.loadFromTextFile(INSTRUCTORS_FILE);

            if (content.length() == 0) {
                return instructors;
            }

            String[] lines = content.split("\n");
            boolean isFirstLine = true;

            for (int i = 0; i < lines.length; i++) {

                String line = lines[i].trim();
                if (line.length() == 0) {
                    continue;
                }

                // skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(PUNCTUATION);
                if (parts.length >= 2) {

                    String id = parts[0].trim();
                    String name = parts[1].trim();

                    Instructor inst = new Instructor(id, "", "", name);
                    instructors.add(inst);
                }
            }

        } catch (Exception ex) {
            System.err.println("Error loading all instructors: " + ex.getMessage());
        }

        return instructors;
    }

    public void updateInstructor(Instructor instructor) {

        if (instructor == null || instructor.getUserId() == null) {
            throw new IllegalArgumentException("Instructor or ID cannot be null.");
        }

        List<Instructor> list = loadAllInstructors();
        StringBuilder builder = new StringBuilder();

        // Write header
        builder.append("InstructorID,InstructorName\n");

        for (int i = 0; i < list.size(); i++) {

            Instructor existing = list.get(i);

            if (existing.getUserId().equals(instructor.getUserId())) {
                builder.append(instructor.getUserId())
                       .append(PUNCTUATION)
                       .append(instructor.getInstructorName())
                       .append("\n");
            } else {
                builder.append(existing.getUserId())
                       .append(PUNCTUATION)
                       .append(existing.getInstructorName())
                       .append("\n");
            }
        }

        fileManager.saveToTextFile(INSTRUCTORS_FILE, builder.toString());
    }

    public void deleteInstructor(String instructorId) {

        if (instructorId == null || instructorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor ID cannot be null.");
        }

        List<Instructor> list = loadAllInstructors();
        StringBuilder builder = new StringBuilder();

        builder.append("InstructorID,InstructorName\n");

        for (int i = 0; i < list.size(); i++) {

            Instructor inst = list.get(i);

            if (!inst.getUserId().equals(instructorId)) {
                builder.append(inst.getUserId())
                       .append(PUNCTUATION)
                       .append(inst.getInstructorName())
                       .append("\n");
            }
        }

        fileManager.saveToTextFile(INSTRUCTORS_FILE, builder.toString());
    }

    public void addInstructor(String instructorId, String instructorName) {

    if (instructorId == null || instructorId.trim().isEmpty()
            || instructorName == null || instructorName.trim().isEmpty()) {
        throw new IllegalArgumentException("Instructor ID and name cannot be empty");
    }

    try {
        String content = fileManager.loadFromTextFile(INSTRUCTORS_FILE);

        if (content == null || content.trim().isEmpty()) {

            StringBuilder builder = new StringBuilder();
            builder.append("InstructorID,InstructorName\n");
            builder.append(instructorId).append(PUNCTUATION).append(instructorName);

            fileManager.saveToTextFile(INSTRUCTORS_FILE, builder.toString());
            return;
        }

        // Prevent duplicates
        List<Instructor> existing = loadAllInstructors();
        for (int i = 0; i < existing.size(); i++) {
            Instructor ins = existing.get(i);
            if (ins.getUserId().equals(instructorId)) {
                return;  
            }
        }

        // Append new instructor WITHOUT leading blank line
        StringBuilder sb = new StringBuilder();

        if (!content.endsWith("\n")) {
            sb.append("\n");
        }

        sb.append(instructorId).append(PUNCTUATION).append(instructorName);

        fileManager.appendToFile(INSTRUCTORS_FILE, sb.toString());

    } catch (Exception ex) {
        System.err.println("Error while adding instructor: " + ex.getMessage());
        ex.printStackTrace();
    }
}




}
