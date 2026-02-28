package models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * LoginLog Class
 * ------------------------------
 * OOP Concepts Used:
 * 1. Encapsulation >>  private fields + getters/setters
 * 2. Abstraction >> hiding how timestamps are converted to binary
 * 3. Modularity >> this class ONLY handles login log data
 *
 * This class stores a single login session:
 * - Login time
 * - Logout time
 * - Session duration
 *
 * Now includes:
 *  - convertDateToBinary() >> converts a Date into a binary number string
 *  - convertLongToBinary() >> converts duration into a binary number string
 *
 * These methods will be used when writing login data into binary files.
 */
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String loginId;
    private String userId;
    private Date loginTimestamp;
    private Date logoutTimestamp;
    private long sessionDuration;

    // ---------------------- CONSTRUCTORS ----------------------

    public LoginLog() {
        this.loginId = UUID.randomUUID().toString();
        this.loginTimestamp = new Date();
    }

    public LoginLog(String userId) {
        this.loginId = UUID.randomUUID().toString();
        this.userId = userId;
        this.loginTimestamp = new Date();
    }

    // ---------------------- SESSION LOGIC ----------------------

    /**
     * Calculates the session duration (milliseconds).
     */
    public long calculateSessionDuration() {
        if (loginTimestamp == null) return 0L;

        Date endTime = (logoutTimestamp != null)
                ? logoutTimestamp
                : new Date();

        return endTime.getTime() - loginTimestamp.getTime();
    }

    /**
     * Records the logout time and calculates duration.
     */
    public void logout(Date logoutTime) {
        this.logoutTimestamp = logoutTime;
        this.sessionDuration = calculateSessionDuration();
    }

    /**
     * Returns true if logoutTimestamp is still empty.
     */
    public boolean isActive() {
        return logoutTimestamp == null;
    }

    // ---------------------- NEW BINARY HELPERS ----------------------

    /**
     * Converts a Date timestamp into a binary string.
     * 
     * Example:
     *   loginTime.getTime() = 1705992000000 (milliseconds)
     *   returns binary string representation of that number.
     *
     * @param date the timestamp (Date object)
     * @return binary string OR "0" if null
     */
    public String convertDateToBinary(Date date) {
        if (date == null) return "0";

        long millis = date.getTime();     // convert Date to long (milliseconds)
        return Long.toBinaryString(millis);  // convert long to binary string
    }

    /**
     * Converts a long duration value into a binary string.
     *
     * Example:
     *   duration = 3600000 (1 hour)
     *   returns binary string of that number.
     *
     * @param value long number to convert
     * @return binary number string
     */
    public String convertLongToBinary(long value) {
        return Long.toBinaryString(value);
    }

        /**
     * Converts login timestamp to a binary string.
     * If null then returns "-".
     */
    public String getBinaryLoginTimestamp() {
        if (loginTimestamp == null) {
            return "-";
        }
        long millis = loginTimestamp.getTime();  // convert date → milliseconds
        return Long.toBinaryString(millis);      // convert milliseconds → binary
    }

    /**
     * Converts logout timestamp to a binary string.
     * If null then returns "-".
     */
    public String getBinaryLogoutTimestamp() {
        if (logoutTimestamp == null) {
            return "-";
        }
        long millis = logoutTimestamp.getTime();
        return Long.toBinaryString(millis);
    }


    // ---------------------- GETTERS & SETTERS ----------------------

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
