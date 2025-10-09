package models;

import enums.UserRole;
import java.util.Date;

/**
 * Abstract base class representing a user in the Course Recovery System.
 * Provides common attributes and functionality for all user types including
 * authentication, profile management, and account status control.
 * This class demonstrates inheritance and abstraction principles.
 */
public abstract class User {
    private String userId;
    private UserRole role;
    private String roleId;
    private String email;
    private String password;
    private boolean isActive;
    private Date createdAt;

    /**
     * Authenticates a user with email and password credentials.
     * @param email the user's email address
     * @param password the user's password
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(String email, String password) {
        // To be implemented
        return false;
    }

    /**
     * Logs out the current user and ends their session.
     */
    public void logout() {
        // To be implemented
    }

    /**
     * Resets the user's password to a new value.
     * @param newPassword the new password to set
     */
    public void resetPassword(String newPassword) {
        // To be implemented
    }

    /**
     * Updates the user's profile information.
     */
    public void updateProfile() {
        // To be implemented
    }

    /**
     * Activates the user account, allowing login and system access.
     */
    public void activate() {
        // To be implemented
    }

    /**
     * Deactivates the user account, preventing login and system access.
     */
    public void deactivate() {
        // To be implemented
    }

    // Getters and setters to be implemented
}
