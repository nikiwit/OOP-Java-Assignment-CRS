package models;

import java.util.Date;

/**
 * Represents a login session record in the system.
 * Tracks user authentication events including login time, logout time,
 * and session duration. Stored in binary format as per requirements.
 * Maps to login_logs.txt data file.
 */
public class LoginLog {
    private String loginId;
    private String userId;
    private Date loginTimestamp;
    private Date logoutTimestamp;
    private long sessionDuration;

    /**
     * Calculates the duration of the session in milliseconds.
     * @return session duration as a long value
     */
    public long calculateSessionDuration() {
        if (loginTimestamp == null) return 0L;
        Date endTime = (logoutTimestamp != null) ? logoutTimestamp : new Date();
        return endTime.getTime() - loginTimestamp.getTime();
    }

    /**
     * Records the logout time and calculates session duration.
     * @param logoutTime the time the user logged out
     */
    public void logout(Date logoutTime) {
        this.logoutTimestamp = logoutTime;
        this.sessionDuration = calculateSessionDuration();
    }

    /**
     * Checks if the session is still active (no logout recorded).
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return logoutTimestamp == null;
    }

    /**
     * Gets the User associated with this login log.
     * @return the User object
     */
    public User getUser() {
        // To be implemented - will load from DAO
        return null;
    }

    // Getters and setters to be implemented
}
