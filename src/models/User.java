package models;

import enums.UserRole;
import java.io.Serializable;
import java.util.Date;

/**
 * Base user class shared by Admin and Instructor.
 * Contains common authentication and profile fields.
 */
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private UserRole role;
    private String name;      
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
     */
    public User(String userId, UserRole role, String name, String email, String password, boolean active) {
        this.userId = userId;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = active;
        this.createdAt = new Date();
    }

    /**
     * Simple login check.
     */
    public boolean login(String email, String password) {
        return this.email.equals(email) &&
               this.password.equals(password) &&
               this.isActive;
    }

    public void logout() {
        // Session logout handled elsewhere
    }

    public void resetPassword(String newPassword) {
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.password = newPassword;
        }
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    // ============================
    // Getters & Setters
    // ============================

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

    public String getName() {          // Required by DAO + Admin + Instructor
        return name;
    }

    public void setName(String name) { // Required by DAO + Admin + Instructor
        this.name = name;
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
        this.isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
