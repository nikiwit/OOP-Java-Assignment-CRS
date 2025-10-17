package models;

import enums.NotificationType;
import java.util.Date;

/**
 * Represents an email notification in the system.
 * Manages automated email communications sent to users for various purposes
 * including account management, recovery plans, and academic reports.
 */
public class EmailNotification {
    private String emailId;
    private String recipientId;
    private String recipientEmail;
    private String subject;
    private String body;
    private Date sentDate;
    private NotificationType notificationType;

    /**
     * Sends the email notification to the recipient.
     */
    public void send() {
        // To be implemented
    }

    /**
     * Gets a formatted message for the email.
     * @return formatted email message
     */
    public String getFormattedMessage() {
        // To be implemented
        return null;
    }

    // Getters and setters to be implemented
}
