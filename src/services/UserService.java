package services;

import dao.UserDAO;
import enums.UserRole;
import models.Admin;
import models.Instructor;
import models.User;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // ------------------ CREATE USER ------------------
    public boolean createUser(String id, String email, String password, String name, UserRole role) {

        User user;

        if (role == UserRole.ADMIN) {
            user = new Admin(id, email, password, name);
        }
        else if (role == UserRole.INSTRUCTOR) {
            user = new Instructor(id, email, password, name);
        }
        else {
            return false;
        }

        userDAO.saveUser(user);
        return true;
    }

    // ------------------ SIMPLE UPDATE ------------------ //
    // --------- Only Name and Active status ------------ //
    public boolean updateUser(String id, String newName, boolean active) {
        User user = userDAO.loadUser(id);
        if (user == null) {
            return false;
        }

        user.setName(newName);
        user.setActive(active);

        userDAO.updateUser(user);
        return true;
    }

    // --------------- FULL UPDATE (NO ROLE CHANGE) ---------------- //
    // ---------- Name, Email, Active status, Password ------------ //
    public boolean updateUserFull(String id,
                                  String name,
                                  String email,
                                  String role,
                                  boolean active,
                                  String password) {

        // Load all users to find the one to update
        java.util.List<User> users = userDAO.loadAllUsers();
        User updatedUser = null;
        
        // Find the matching user by ID
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);

            if (u.getUserId().equals(id)) {
                updatedUser = u;
                break;
            }
        }

    
        // If user not found, return false
        if (updatedUser == null) {
            return false;
        }

        // Apply updates
        updatedUser.setName(name);
        updatedUser.setEmail(email);
        updatedUser.setPassword(password);
        updatedUser.setActive(active);

        //  Role will stay the same — no changes allowed

        // Save updates to file
        userDAO.updateUser(updatedUser);
        return true;
    }

    // ------------------ DELETE USER ------------------
    public boolean deleteUser(String id) {
        userDAO.deleteUser(id);
        return true;
    }

    // ------------------ FIND BY EMAIL ------------------
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    // ------------------ LOAD ALL USERS ------------------
    public java.util.List<User> getAllUsers() {
        return userDAO.loadAllUsers();
    }

      // ==========================================================
    //      NEW METHODS FOR PASSWORD RESET FEATURE
    // ==========================================================

    /**
     * Updates password by email (for reset feature).
     *
     * OOP Concepts:
     *  - Encapsulation: modifying User object via setters.
     *  - Abstraction: GUI does not know file structure, it calls service only.
     */
    public boolean updatePasswordByEmail(String email, String newPassword) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            return false;
        }

        user.setPassword(newPassword);
        userDAO.updateUser(user);
        return true;
    }

    /**
     * Checks if a user exists with the given email. (maybe i will use later)
     */
    public boolean userExistsByEmail(String email) {
        return userDAO.findByEmail(email) != null;
    }
}

