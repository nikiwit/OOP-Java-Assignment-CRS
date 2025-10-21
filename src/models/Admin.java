package models;

import enums.UserRole;

/**
 * Represents an administrator user in the Course Recovery System.
 * Admins have full system access and can manage all users, generate reports,
 * and oversee the entire academic recovery process.
 * Extends the User class demonstrating inheritance.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    private String adminName;

    /**
     * Default constructor.
     */
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    /**
     * Constructor with all fields.
     *
     * @param userId user unique identifier
     * @param email admin email address
     * @param password admin password
     * @param adminName admin full name
     */
    public Admin(String userId, String email, String password, String adminName) {
        super(userId, UserRole.ADMIN, email, password);
        this.adminName = adminName;
    }

    /**
     * Adds a new user to the system.
     * Delegates to UserDAO for persistence.
     *
     * @param user the user object to add
     */
    public void addUser(User user) {
        // Will be implemented when UserDAO is connected
        System.out.println("Admin adding user: " + user.getEmail());
    }

    /**
     * Updates an existing user's information.
     * Delegates to UserDAO for persistence.
     *
     * @param user the user object with updated information
     */
    public void updateUser(User user) {
        // Will be implemented when UserDAO is connected
        System.out.println("Admin updating user: " + user.getEmail());
    }

    /**
     * Deactivates a user account by user ID.
     *
     * @param userId the ID of the user to deactivate
     */
    public void deactivateUser(String userId) {
        // Will be implemented when UserDAO is connected
        System.out.println("Admin deactivating user: " + userId);
    }

    /**
     * Generates comprehensive system reports for administrative purposes.
     */
    public void generateSystemReports() {
        // Will be implemented when ReportGenerator is connected
        System.out.println("Admin generating system reports");
    }

    /**
     * Checks if a student is eligible for progression.
     *
     * @param student the student to check
     * @return true if eligible, false otherwise
     */
    public boolean checkEligibility(Student student) {
        // Will be implemented when EligibilityChecker is connected
        return true;
    }

    // Getters and Setters

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
