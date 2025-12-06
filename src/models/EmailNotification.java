package models;

import enums.NotificationType;
import services.EmailNotificationService;
import java.util.Date;
import java.text.SimpleDateFormat;

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
     * Default constructor
     */
    public EmailNotification() {
        this.sentDate = new Date();
    }

    /**
     * Full constructor with all fields
     */
    public EmailNotification(String emailId, String recipientId, String recipientEmail,
                            String subject, String body, NotificationType notificationType) {
        this.emailId = emailId;
        this.recipientId = recipientId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.sentDate = new Date();
        this.notificationType = notificationType;
    }

    /**
     * Sends the email notification to the recipient.
     */
    public void send() {
        EmailNotificationService emailService = EmailNotificationService.getInstance();
        emailService.sendEmail(this);
    }

    /**
     * Gets a formatted message for the email.
     * @return formatted email message
     */
    public String getFormattedMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append("To: ").append(recipientEmail).append("\n");
        msg.append("Subject: ").append(subject).append("\n");
        msg.append("Type: ").append(notificationType).append("\n");
        msg.append("Date: ").append(sentDate).append("\n\n");
        msg.append(body);
        return msg.toString();
    }

    /**
     * Converts the email notification to CSV format for file storage
     * @return CSV string representation
     */
    public String toCSV() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.join(",",
            emailId,
            recipientId,
            recipientEmail,
            subject.replace(",", ";"),
            body.replace(",", ";").replace("\n", " "),
            sdf.format(sentDate),
            notificationType.toString()
        );
    }

    // Getters and Setters

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
