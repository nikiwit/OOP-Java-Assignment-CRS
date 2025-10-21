package models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a login session record in the system.
 * Tracks user authentication events including login time, logout time,
 * and session duration. Stored in binary format as per requirements.
 * Maps to login_logs.dat binary file.
 */
public class LoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String loginId;
    private String userId;
    private Date loginTimestamp;
    private Date logoutTimestamp;
    private long sessionDuration;

    /**
     * Default constructor.
     */
    public LoginLog() {
        this.loginId = UUID.randomUUID().toString();
        this.loginTimestamp = new Date();
    }

    /**
     * Constructor with userId.
     *
     * @param userId the ID of the user logging in
     */
    public LoginLog(String userId) {
        this.loginId = UUID.randomUUID().toString();
        this.userId = userId;
        this.loginTimestamp = new Date();
    }

    /**
     * Calculates the duration of the session in milliseconds.
     *
     * @return session duration as a long value
     */
    public long calculateSessionDuration() {
        if (loginTimestamp == null) return 0L;
        Date endTime = (logoutTimestamp != null) ? logoutTimestamp : new Date();
        return endTime.getTime() - loginTimestamp.getTime();
    }

    /**
     * Records the logout time and calculates session duration.
     *
     * @param logoutTime the time the user logged out
     */
    public void logout(Date logoutTime) {
        this.logoutTimestamp = logoutTime;
        this.sessionDuration = calculateSessionDuration();
    }

    /**
     * Checks if the session is still active (no logout recorded).
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return logoutTimestamp == null;
    }

    /**
     * Gets the User associated with this login log.
     *
     * @return the User object
     */
    public User getUser() {
        // Will be implemented when needed - loads user from UserDAO
        return null;
    }

    // Getters and Setters

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(Date loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public Date getLogoutTimestamp() {
        return logoutTimestamp;
    }

    public void setLogoutTimestamp(Date logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }

    public long getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }
}
