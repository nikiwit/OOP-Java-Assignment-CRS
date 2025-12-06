package dao;

import models.EmailNotification;
import enums.NotificationType;
import utils.FileManager;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data Access Object for EmailNotification entities.
 * Handles CRUD operations for email notification data stored in text files.
 * Implements data persistence layer for the EmailNotification model.
 */
public class EmailNotificationDAO {
    private FileManager fileManager;
    private static final String EMAIL_FILE = "email.txt";

    /**
     * Constructor initializes FileManager
     */
    public EmailNotificationDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Saves an email notification to the data file.
     * @param email the email notification to save
     */
    public void saveEmail(EmailNotification email) {
        String csvLine = email.toCSV();
        fileManager.appendToFile(EMAIL_FILE, csvLine);
    }

    /**
     * Loads an email notification by ID from the data file.
     * @param emailId the email ID
     * @return the EmailNotification object, or null if not found
     */
    public EmailNotification loadEmail(String emailId) {
        String content = fileManager.loadFromTextFile(EMAIL_FILE);
        if (content == null || content.isEmpty()) {
            return null;
        }

        String[] lines = content.split("\n");

        for (int i = 1; i < lines.length; i++) { // Skip header
            String[] fields = parseCSVLine(lines[i]);
            if (fields.length >= 7 && fields[0].equals(emailId)) {
                return parseEmailFromCSV(fields);
            }
        }
        return null;
    }

    /**
     * Loads all email notifications sent to a specific recipient.
     * @param recipientId the recipient's user ID
     * @return list of email notifications
     */
    public List<EmailNotification> loadEmailsByRecipient(String recipientId) {
        List<EmailNotification> emails = new ArrayList<>();
        String content = fileManager.loadFromTextFile(EMAIL_FILE);
        if (content == null || content.isEmpty()) {
            return emails;
        }

        String[] lines = content.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] fields = parseCSVLine(lines[i]);
            if (fields.length >= 7 && fields[1].equals(recipientId)) {
                emails.add(parseEmailFromCSV(fields));
            }
        }
        return emails;
    }

    /**
     * Loads all email notifications from the data file.
     * @return list of all email notifications
     */
    public List<EmailNotification> loadAllEmails() {
        List<EmailNotification> emails = new ArrayList<>();
        String content = fileManager.loadFromTextFile(EMAIL_FILE);
        if (content == null || content.isEmpty()) {
            return emails;
        }

        String[] lines = content.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] fields = parseCSVLine(lines[i]);
            if (fields.length >= 7) {
                emails.add(parseEmailFromCSV(fields));
            }
        }
        return emails;
    }

    /**
     * Parses a CSV line into fields
     * @param line CSV line to parse
     * @return array of fields
     */
    private String[] parseCSVLine(String line) {
        return fileManager.parseCSVLine(line);
    }

    /**
     * Parses CSV fields into an EmailNotification object
     * @param fields CSV fields
     * @return EmailNotification object
     */
    private EmailNotification parseEmailFromCSV(String[] fields) {
        EmailNotification email = new EmailNotification();
        email.setEmailId(fields[0]);
        email.setRecipientId(fields[1]);
        email.setRecipientEmail(fields[2]);
        email.setSubject(fields[3]);
        email.setBody(fields[4]);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            email.setSentDate(sdf.parse(fields[5]));
        } catch (Exception e) {
            email.setSentDate(new Date());
        }

        try {
            email.setNotificationType(NotificationType.valueOf(fields[6]));
        } catch (IllegalArgumentException e) {
            email.setNotificationType(NotificationType.ACCOUNT_CREATION);
        }

        return email;
    }

    /**
     * Generates the next email ID in sequence (E001, E002, etc.)
     * @return next email ID
     */
    public String generateNextEmailId() {
        List<EmailNotification> allEmails = loadAllEmails();
        if (allEmails.isEmpty()) {
            return "E001";
        }

        int maxNum = 0;
        for (EmailNotification email : allEmails) {
            String id = email.getEmailId();
            if (id != null && id.startsWith("E")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException e) {
                    // skip invalid IDs
                }
            }
        }
        return String.format("E%03d", maxNum + 1);
    }
}
