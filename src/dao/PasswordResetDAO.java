package dao;

import utils.FileManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PasswordResetDAO
 * ----------------
 * OOP Concepts:
 *  - Encapsulation: private fields and helper methods.
 *  - Abstraction: hides file handling details from GUI.
 *  - Modularity: separate class only for password-reset data.
 *
 * Stores password reset codes in a simple text file:
 *   password_reset.txt
 *
 * Format (CSV):
 *   Email,ResetCode,CreatedAt
 */
public class PasswordResetDAO {

    private static final String RESET_FILE = "password_reset.txt";
    private static final String HEADER = "Email,ResetCode,CreatedAt";

    private FileManager fileManager;

    public PasswordResetDAO() {
        this.fileManager = new FileManager();
        ensureFileHeader();
    }

    /**
     * Makes sure the file has a header line.
     */
    private void ensureFileHeader() {
        String content = fileManager.loadFromTextFile(RESET_FILE);
        if (content == null || content.trim().length() == 0) {
            fileManager.saveToTextFile(RESET_FILE, HEADER + "\n");
        }
    }

    /**
     * Generates a random reset code using UUID.
     * Example: "a3f9b21c"
     */
    public String generateResetCode() {
        String full = java.util.UUID.randomUUID().toString();
        String code = full.replace("-", "");
        if (code.length() > 8) {
            code = code.substring(0, 8);
        }
        return code.toUpperCase();
    }

    /**
     * Saves a password reset request to the text file.
     *
     * @param email user's email
     * @param code  generated reset code
     */
    public void saveResetRequest(String email, String code) {
        if (email == null || email.trim().length() == 0) {
            return;
        }
        if (code == null || code.trim().length() == 0) {
            return;
        }

        String content = fileManager.loadFromTextFile(RESET_FILE);
        if (content == null || content.trim().length() == 0) {
            content = HEADER + "\n";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());

        StringBuilder builder = new StringBuilder();
        builder.append(content);
        builder.append(email).append(",")
               .append(code).append(",")
               .append(now).append("\n");

        fileManager.saveToTextFile(RESET_FILE, builder.toString());
    }

    /**
     * Validates that a reset code exists for the given email.
     *
     * @param email user's email
     * @param code  input reset code
     * @return true if exists, otherwise false
     */
    public boolean validateCode(String email, String code) {
        String content = fileManager.loadFromTextFile(RESET_FILE);
        if (content == null || content.trim().length() == 0) {
            return false;
        }

        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("Email,")) {
                continue; // skip header
            }

            String[] parts = line.split(",");
            if (parts.length < 3) {
                continue;
            }

            String fileEmail = parts[0].trim();
            String fileCode = parts[1].trim();

            if (fileEmail.equalsIgnoreCase(email) && fileCode.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
