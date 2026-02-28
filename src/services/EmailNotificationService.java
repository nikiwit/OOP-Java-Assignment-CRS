package services;

import models.EmailNotification;
import models.User;
import models.Student;
import dao.EmailNotificationDAO;
import enums.NotificationType;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Singleton service for managing email notifications throughout the system.
 * Uses JavaMail API to send automated emails for various events and alerts.
 */
public class EmailNotificationService {

    private static EmailNotificationService instance;
    private Properties emailConfig;
    private EmailNotificationDAO emailDAO;

    private EmailNotificationService() {
        emailDAO = new EmailNotificationDAO();
        loadEmailConfig();
    }

    public static EmailNotificationService getInstance() {
        if (instance == null) {
            instance = new EmailNotificationService();
        }
        return instance;
    }

    /**
     * Load email configuration from email.properties file
     */
    private void loadEmailConfig() {
        emailConfig = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config/email.properties");
            emailConfig.load(fis);
            fis.close();
            System.out.println("Email configuration loaded successfully");
        } catch (IOException e) {
            System.err.println("Warning: Could not load email config file. Using defaults.");
            setDefaultConfig();
        }
    }

    /**
     * Set default configuration if config file not found
     */
    private void setDefaultConfig() {
        emailConfig.setProperty("smtp.host", "smtp.gmail.com");
        emailConfig.setProperty("smtp.port", "587");
        emailConfig.setProperty("smtp.username", "nikiwit7@gmail.com");
        emailConfig.setProperty("smtp.password", "lpeo ojqt mnou gent");
        emailConfig.setProperty("smtp.auth", "true");
        emailConfig.setProperty("smtp.starttls.enable", "true");
        emailConfig.setProperty("email.from", "nikiwit7@gmail.com");
        emailConfig.setProperty("email.from.name", "CRS System");
    }

    /**
     * Main method to send any email using JavaMail API
     * @param email the EmailNotification object to send
     */
    public void sendEmail(EmailNotification email) {
        try {
            // Setup mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.host", emailConfig.getProperty("smtp.host"));
            props.put("mail.smtp.port", emailConfig.getProperty("smtp.port"));
            props.put("mail.smtp.auth", emailConfig.getProperty("smtp.auth"));
            props.put("mail.smtp.starttls.enable", emailConfig.getProperty("smtp.starttls.enable"));

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        emailConfig.getProperty("smtp.username"),
                        emailConfig.getProperty("smtp.password")
                    );
                }
            });

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(
                emailConfig.getProperty("email.from"),
                emailConfig.getProperty("email.from.name")
            ));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email.getRecipientEmail()));
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            message.setSentDate(new Date());

            // Send it
            Transport.send(message);

            // Save to database
            emailDAO.saveEmail(email);

            System.out.println("Email sent successfully to: " + email.getRecipientEmail());
            System.out.println("Subject: " + email.getSubject());

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Send account creation notification
     * @param user the newly created user
     */
    public void sendAccountCreationEmail(User user) {
        String emailId = emailDAO.generateNextEmailId();
        String subject = "Welcome to Course Recovery System";
        String body = "Dear " + user.getName() + ",\n\n" +
                      "Your account has been successfully created in the Course Recovery System.\n\n" +
                      "Account Details:\n" +
                      "- User ID: " + user.getUserId() + "\n" +
                      "- Email: " + user.getEmail() + "\n" +
                      "- Role: " + user.getRole() + "\n\n" +
                      "You can now log in to the system using your credentials.\n\n" +
                      "If you did not request this account, please contact the administrator immediately.\n\n" +
                      "Best regards,\n" +
                      "CRS Admin Team";

        EmailNotification email = new EmailNotification(
            emailId, user.getUserId(), user.getEmail(), subject, body,
            NotificationType.ACCOUNT_CREATION
        );
        sendEmail(email);
    }

    /**
     * Send password reset notification
     * @param recipientEmail email address of the user
     * @param resetCode the password reset code
     */
    public void sendPasswordResetEmail(String recipientEmail, String resetCode) {
        String emailId = emailDAO.generateNextEmailId();
        String subject = "Password Reset Request - CRS";
        String body = "Dear User,\n\n" +
                      "You have requested to reset your password for the Course Recovery System.\n\n" +
                      "Your password reset code is: " + resetCode + "\n\n" +
                      "Please enter this code in the reset password window to continue.\n\n" +
                      "This code will expire in 30 minutes.\n\n" +
                      "If you did not request this reset, please ignore this email and contact support.\n\n" +
                      "Best regards,\n" +
                      "CRS Security Team";

        EmailNotification email = new EmailNotification(
            emailId, "UNKNOWN", recipientEmail, subject, body,
            NotificationType.PASSWORD_RESET
        );
        sendEmail(email);
    }

    /**
     * Send recovery plan assigned notification
     * @param student the student receiving the plan
     * @param courseId the course ID
     * @param courseName the course name
     */
    public void sendRecoveryPlanAssignedEmail(Student student, String courseId, String courseName) {
        String emailId = emailDAO.generateNextEmailId();
        String subject = "Recovery Plan Assigned - " + courseName;
        String body = "Dear " + student.getFullName() + ",\n\n" +
                      "A recovery plan has been assigned to you for the following course:\n\n" +
                      "Course: " + courseName + " (" + courseId + ")\n\n" +
                      "This plan is designed to help you improve your performance and successfully complete the course.\n\n" +
                      "Your instructor will provide you with the recovery plan details and action steps. " +
                      "Please contact them to get started.\n\n" +
                      "Important: Follow the plan carefully and complete all assigned tasks on time.\n\n" +
                      "If you have any questions, please contact your instructor.\n\n" +
                      "Good luck!\n\n" +
                      "Best regards,\n" +
                      "CRS Academic Support Team";

        EmailNotification email = new EmailNotification(
            emailId, student.getStudentId(), student.getEmail(), subject, body,
            NotificationType.RECOVERY_PLAN_ASSIGNED
        );
        sendEmail(email);
    }

    /**
     * Send milestone reminder notification
     * @param student the student
     * @param courseId the course ID
     * @param courseName the course name
     * @param actionNumber the action/task number
     * @param actionDescription description of the task
     */
    public void sendMilestoneReminderEmail(Student student, String courseId, String courseName,
                                          int actionNumber, String actionDescription) {
        String emailId = emailDAO.generateNextEmailId();
        String subject = "Reminder: Recovery Task Due - " + courseName;
        String body = "Dear " + student.getFullName() + ",\n\n" +
                      "This is a reminder about your upcoming recovery task:\n\n" +
                      "Course: " + courseName + " (" + courseId + ")\n" +
                      "Task: Action " + actionNumber + " - " + actionDescription + "\n\n" +
                      "Please complete this task as soon as possible to stay on track with your recovery plan.\n\n" +
                      "Contact your instructor for task details and to submit your work.\n\n" +
                      "Stay focused and keep up the good work!\n\n" +
                      "Best regards,\n" +
                      "CRS Academic Support Team";

        EmailNotification email = new EmailNotification(
            emailId, student.getStudentId(), student.getEmail(), subject, body,
            NotificationType.MILESTONE_REMINDER
        );
        sendEmail(email);
    }

    /**
     * Send eligibility status notification
     * @param student the student
     * @param failedCount number of failed courses
     * @param status eligibility status description
     */
    public void sendEligibilityStatusEmail(Student student, int failedCount, String status) {
        String emailId = emailDAO.generateNextEmailId();
        String subject = "Academic Eligibility Status Update";
        String body = "Dear " + student.getFullName() + ",\n\n" +
                      "This is an important notification regarding your academic eligibility status.\n\n" +
                      "Current Status: " + status + "\n" +
                      "Number of Failed Courses: " + failedCount + "\n\n";

        if (failedCount >= 2) {
            body += "IMPORTANT: You have failed " + failedCount + " or more courses.\n" +
                    "This affects your academic standing.\n\n" +
                    "Please take the following actions:\n" +
                    "1. Meet with your academic advisor\n" +
                    "2. Review and complete all assigned recovery plans\n" +
                    "3. Utilize academic support resources available\n\n";
        } else if (failedCount == 1) {
            body += "You currently have 1 failed course. Please focus on your recovery plan.\n\n";
        } else {
            body += "Congratulations! You are in good academic standing.\n\n";
        }

        body += "For more information, please contact your academic advisor.\n\n" +
                "Best regards,\n" +
                "CRS Academic Affairs";

        EmailNotification email = new EmailNotification(
            emailId, student.getStudentId(), student.getEmail(), subject, body,
            NotificationType.ELIGIBILITY_STATUS
        );
        sendEmail(email);
    }

    /**
     * Send report generated notification
     * @param student the student
     * @param reportType type of report
     * @param semesterInfo semester information
     */
    public void sendReportGeneratedEmail(Student student, String reportType, String semesterInfo) {
        String emailId = emailDAO.generateNextEmailId();
        String subject = "Academic Report Generated - " + semesterInfo;
        String body = "Dear " + student.getFullName() + ",\n\n" +
                      "Your academic performance report has been generated.\n\n" +
                      "Report Type: " + reportType + "\n" +
                      "Period: " + semesterInfo + "\n\n" +
                      "Your academic advisor will provide you with a copy of your complete report.\n\n" +
                      "This report includes:\n" +
                      "- Course grades and results\n" +
                      "- Recovery plan progress\n" +
                      "- Academic standing information\n" +
                      "- Recommendations for improvement\n\n" +
                      "Please contact your advisor to review your report and discuss any concerns.\n\n" +
                      "Best regards,\n" +
                      "CRS Reporting Team";

        EmailNotification email = new EmailNotification(
            emailId, student.getStudentId(), student.getEmail(), subject, body,
            NotificationType.REPORT_GENERATED
        );
        sendEmail(email);
    }
}
