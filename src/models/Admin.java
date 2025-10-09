package models;

import java.util.List;

/**
 * Represents an administrator user in the Course Recovery System.
 * Admins have full system access and can manage all users, generate reports,
 * and oversee the entire academic recovery process.
 * Extends the User class demonstrating inheritance.
 */
public class Admin extends User {
    private String adminId;

    /**
     * Adds a new user to the system.
     * @param user the user object to add
     */
    public void addUser(User user) {
        // To be implemented
    }

    /**
     * Updates an existing user's information.
     * @param user the user object with updated information
     */
    public void updateUser(User user) {
        // To be implemented
    }

    /**
     * Deactivates a user account by user ID.
     * @param userId the ID of the user to deactivate
     */
    public void deactivateUser(String userId) {
        // To be implemented
    }

    /**
     * Retrieves a list of all users in the system.
     * @return list of all users
     */
    public List<User> viewAllUsers() {
        // To be implemented
        return null;
    }

    /**
     * Generates comprehensive system reports for administrative purposes.
     */
    public void generateSystemReports() {
        // To be implemented
    }

    // Getters and setters to be implemented
}
