package models;

import enums.UserRole;

/**
 * Represents an administrator user in the Course Recovery System.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    private String adminName;

    // Default constructor
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    // Constructor that matches User.java
    public Admin(String userId, String email, String password, String adminName) {
        super(userId, UserRole.ADMIN, adminName, email, password, true);
        this.adminName = adminName;
    }
    

    // Getter/Setter
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
        this.setName(adminName); // keep base class name in sync
    }
}
