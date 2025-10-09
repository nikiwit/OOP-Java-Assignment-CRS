package dao;

import models.User;
import utils.FileManager;
import java.util.List;

/**
 * Data Access Object for User entities.
 * Handles CRUD operations for user data stored in text files.
 * Implements data persistence layer for the User model.
 */
public class UserDAO {
    private FileManager fileManager;

    /**
     * Saves a user to the data file.
     * @param user the user to save
     */
    public void saveUser(User user) {
        // To be implemented
    }

    /**
     * Loads a user by ID from the data file.
     * @param userId the user ID
     * @return the User object, or null if not found
     */
    public User loadUser(String userId) {
        // To be implemented
        return null;
    }

    /**
     * Loads all users from the data file.
     * @return list of all users
     */
    public List<User> loadAllUsers() {
        // To be implemented
        return null;
    }

    /**
     * Updates an existing user's information.
     * @param user the user with updated data
     */
    public void updateUser(User user) {
        // To be implemented
    }

    /**
     * Deletes a user from the data file.
     * @param userId the user ID to delete
     */
    public void deleteUser(String userId) {
        // To be implemented
    }

    /**
     * Finds a user by email address.
     * @param email the email to search for
     * @return the User object, or null if not found
     */
    public User findByEmail(String email) {
        // To be implemented
        return null;
    }

    // Additional helper methods to be implemented
}
