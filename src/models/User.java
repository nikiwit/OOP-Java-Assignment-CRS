package models;

import enums.UserRole;
import java.io.Serializable;
import java.util.Date;

/**
 * Abstract base class representing a user in the Course Recovery System.
 * Provides common attributes and functionality for all user types including
 * authentication, profile management, and account status control.
 * This class demonstrates inheritance and abstraction principles.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private UserRole role;
    private String email;
    private String password;
    private boolean isActive;
    private Date createdAt;

    /**
     * Default constructor.
     */
    public User() {
        this.isActive = true;
        this.createdAt = new Date();
    }

    /**
     * Constructor with basic fields.
     *
     * @param userId user unique identifier
     * @param role user role (ADMIN or INSTRUCTOR)
     * @param email user email address
     * @param password user password
     */
    public User(String userId, UserRole role, String email, String password) {
        this.userId = userId;
        this.role = role;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.createdAt = new Date();
    }

    /**
     * Authenticates a user with email and password credentials.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password) && this.isActive;
    }

    /**
     * Logs out the current user and ends their session.
     */
    public void logout() {
        // Logout logic handled by AuthenticationService
    }

    /**
     * Resets the user's password to a new value.
     *
     * @param newPassword the new password to set
     */
    public void resetPassword(String newPassword) {
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.password = newPassword;
        }
    }

    /**
     * Updates the user's profile information.
     */
    public void updateProfile() {
        // Override in subclasses for specific profile update logic
    }

    /**
     * Activates the user account, allowing login and system access.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates the user account, preventing login and system access.
     */
    public void deactivate() {
        this.isActive = false;
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
