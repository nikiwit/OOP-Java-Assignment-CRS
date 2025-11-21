package services;

public class EmailNotificationService {

    private static EmailNotificationService instance;

    private EmailNotificationService() {
        // private constructor so no one can create object directly
    }

    public static EmailNotificationService getInstance() {
        if (instance == null) {
            instance = new EmailNotificationService();
        }
        return instance;
    }

    // Simulated email sending (console only)
    public void sendPasswordResetEmail(String email, String code) {
        System.out.println("=== PASSWORD RESET EMAIL SENT ===");
        System.out.println("To: " + email);
        System.out.println("Your reset code is: " + code);
    }
}


// This code from git didn't use because email section handled by another member so currently it prints the
// the reset code to console/terminal
// later can edit according to actual email sending logic

/*
package services;

import models.User;
import models.Student;
import models.EmailNotification;
import models.RecoveryPlan;
import models.AcademicReport;
import enums.NotificationType;
import java.util.List;

/**
 * Singleton service for managing email notifications throughout the system.
 * Uses JavaMail API to send automated emails for various events and alerts.
 * Implements the Singleton design pattern to ensure only one instance exists.
 */
/*
public class EmailNotificationService {
    private static EmailNotificationService instance;
    private String smtpHost;
    private int smtpPort;
    private List<EmailNotification> emailQueue;

    /**
     * Private constructor to prevent direct instantiation.
     * Part of the Singleton pattern implementation.
     */
    /*
    private EmailNotificationService() {
        // To be implemented
    }

    /**
     * Gets the single instance of EmailNotificationService.
     * Creates the instance if it doesn't exist (lazy initialization).
     * @return the singleton instance
     */
    /*
    public static EmailNotificationService getInstance() {
        // To be implemented
        return instance;
    }

    /**
     * Sends an email notification using JavaMail API.
     * @param email the EmailNotification object to send
     */
    /*
    public void sendEmail(EmailNotification email) {
        // To be implemented
    }

    /**
     * Creates and sends a notification to a user.
     * @param user the recipient user
     * @param type the notification type
     * @param content the email content/body
     */
    /*
    public void sendNotification(User user, NotificationType type, String content) {
        // To be implemented
    }

    /**
     * Sends a recovery plan notification to a student.
     * @param student the student receiving recovery plan
     * @param plan the recovery plan details
     */
    /*
    public void sendRecoveryPlanNotification(Student student, RecoveryPlan plan) {
        // To be implemented
    }

    /**
     * Sends an academic report notification to a student.
     * @param student the student
     * @param report the academic report
     */
    /*
    public void sendReportNotification(Student student, AcademicReport report) {
        // To be implemented
    }

    /**
     * Sends a password reset notification with a token.
     * @param user the user requesting password reset
     * @param token the reset token
     */
    /*
    public void sendPasswordResetNotification(User user, String token) {
        // To be implemented
    }

    // Additional helper methods to be implemented
}
*/
