package models;

import enums.EmailStatus;
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
    private EmailStatus status;
    private NotificationType notificationType;

    /**
     * Sends the email notification to the recipient.
     */
    public void send() {
        // To be implemented
    }

    /**
     * Marks the email as successfully sent.
     */
    public void markAsSent() {
        // To be implemented
    }

    /**
     * Marks the email as failed to send.
     */
    public void markAsFailed() {
        // To be implemented
    }

    /**
     * Gets the User object who will receive this email.
     * @return the recipient User object
     */
    public User getRecipient() {
        // To be implemented
        return null;
    }

    // Getters and setters to be implemented
}
