package dao;

import models.Student;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student entities.
 * Handles CRUD operations for student data stored in text files.
 * Implements data persistence layer for the Student model.
 */
public class StudentDAO {
    private static final String STUDENTS_FILE = "students.txt";
    private static final String DELIMITER = ",";
    private FileManager fileManager;

    /**
     * Constructor initializing FileManager.
     */
    public StudentDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Constructor with custom FileManager.
     * @param fileManager the FileManager instance
     */
    public StudentDAO(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Saves a student to the data file.
     * @param student the student to save
     */
    public void saveStudent(Student student) {
        if (student == null || student.getStudentId() == null) {
            throw new IllegalArgumentException("Student and student ID cannot be null");
        }

        // Check if student already exists
        Student existing = loadStudent(student.getStudentId());
        if (existing != null) {
            updateStudent(student);
            return;
        }

        // Format: studentId,firstName,lastName,major,email,status,year,semester,retake
        String data = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%d%s%d%s%b",
                student.getStudentId(), DELIMITER,
                student.getFirstName(), DELIMITER,
                student.getLastName(), DELIMITER,
                student.getMajor(), DELIMITER,
                student.getEmail(), DELIMITER,
                student.getStatus(), DELIMITER,
                student.getYear(), DELIMITER,
                student.getSemester(), DELIMITER,
                student.isRetake());

        fileManager.appendToFile(STUDENTS_FILE, data);
    }

    /**
     * Loads a student by ID from the data file.
     * @param studentId the student ID
     * @return the Student object, or null if not found
     */
    public Student loadStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }

        String content = fileManager.loadFromTextFile(STUDENTS_FILE);
        if (content.isEmpty()) {
            return null;
        }

        String[] lines = content.split("\n");
        boolean firstLine = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Skip header line
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 9 && parts[0].equals(studentId)) {
                return parseStudent(parts);
            }
        }

        return null;
    }

    /**
     * Loads all students from the data file.
     * @return list of all students
     */
    public List<Student> loadAllStudents() {
        List<Student> students = new ArrayList<>();
        String content = fileManager.loadFromTextFile(STUDENTS_FILE);

        if (content.isEmpty()) {
            return students;
        }

        String[] lines = content.split("\n");
        boolean firstLine = true;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Skip header line
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] parts = line.split(DELIMITER);
            if (parts.length >= 9) {
                Student student = parseStudent(parts);
                if (student != null) {
                    students.add(student);
                }
            }
        }

        return students;
    }

    /**
     * Updates an existing student's information.
     * @param student the student with updated data
     */
    public void updateStudent(Student student) {
        if (student == null || student.getStudentId() == null) {
            throw new IllegalArgumentException("Student and student ID cannot be null");
        }

        List<Student> students = loadAllStudents();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (Student existing : students) {
            if (existing.getStudentId().equals(student.getStudentId())) {
                // Update with new data
                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%d%s%d%s%b\n",
                        student.getStudentId(), DELIMITER,
                        student.getFirstName(), DELIMITER,
                        student.getLastName(), DELIMITER,
                        student.getMajor(), DELIMITER,
                        student.getEmail(), DELIMITER,
                        student.getStatus(), DELIMITER,
                        student.getYear(), DELIMITER,
                        student.getSemester(), DELIMITER,
                        student.isRetake()));
                found = true;
            } else {
                // Keep existing data
                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%d%s%d%s%b\n",
                        existing.getStudentId(), DELIMITER,
                        existing.getFirstName(), DELIMITER,
                        existing.getLastName(), DELIMITER,
                        existing.getMajor(), DELIMITER,
                        existing.getEmail(), DELIMITER,
                        existing.getStatus(), DELIMITER,
                        existing.getYear(), DELIMITER,
                        existing.getSemester(), DELIMITER,
                        existing.isRetake()));
            }
        }

        if (!found) {
            throw new IllegalStateException("Student not found: " + student.getStudentId());
        }

        fileManager.saveToTextFile(STUDENTS_FILE, updatedContent.toString());
    }

    /**
     * Deletes a student from the data file.
     * @param studentId the student ID to delete
     */
    public void deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        List<Student> students = loadAllStudents();
        StringBuilder updatedContent = new StringBuilder();

        boolean found = false;
        for (Student student : students) {
            if (!student.getStudentId().equals(studentId)) {
                updatedContent.append(String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%d%s%d%s%b\n",
                        student.getStudentId(), DELIMITER,
                        student.getFirstName(), DELIMITER,
                        student.getLastName(), DELIMITER,
                        student.getMajor(), DELIMITER,
                        student.getEmail(), DELIMITER,
                        student.getStatus(), DELIMITER,
                        student.getYear(), DELIMITER,
                        student.getSemester(), DELIMITER,
                        student.isRetake()));
            } else {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("Student not found: " + studentId);
        }

        fileManager.saveToTextFile(STUDENTS_FILE, updatedContent.toString());
    }

    /**
     * Loads students by major from the data file.
     * @param major the major to filter by
     * @return list of students in that major
     */
    public List<Student> loadStudentsByMajor(String major) {
        List<Student> allStudents = loadAllStudents();
        List<Student> filteredStudents = new ArrayList<>();

        for (Student student : allStudents) {
            if (student.getMajor() != null && student.getMajor().equalsIgnoreCase(major)) {
                filteredStudents.add(student);
            }
        }

        return filteredStudents;
    }

    /**
     * Parses a CSV line into a Student object.
     * File format: StudentID,FirstName,LastName,Major,Year,Semester,Retake,Email,Status
     * @param parts the CSV parts
     * @return the Student object
     */
    private Student parseStudent(String[] parts) {
        try {
            String studentId = parts[0].trim();
            String firstName = parts[1].trim();
            String lastName = parts[2].trim();
            String major = parts[3].trim();
            int year = Integer.parseInt(parts[4].trim());
            int semester = Integer.parseInt(parts[5].trim());
            boolean retake = parts[6].trim().equals("1");
            String email = parts[7].trim();
            String status = parts[8].trim();

            return new Student(studentId, firstName, lastName, major, email, status, year, semester, retake);
        } catch (Exception e) {
            System.err.println("Error parsing student: " + e.getMessage());
            return null;
        }
    }
}
