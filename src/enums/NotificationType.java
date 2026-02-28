package enums;

/**
 * Enumeration representing the different types of email notifications
 * that can be sent by the system to users.
 * Matches assignment specification requirements.
 */
public enum NotificationType {
    ACCOUNT_CREATION,           // New account creation notifications
    PASSWORD_RESET,             // Password reset requests
    RECOVERY_PLAN_ASSIGNED,     // Recovery plan assignments
    MILESTONE_REMINDER,         // Recovery milestone reminders
    ELIGIBILITY_STATUS,         // Student eligibility notifications
    REPORT_GENERATED            // Academic performance reports
}
