package dao;

import models.User;
import models.Admin;
import models.Instructor;
import enums.UserRole;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entities.
 * Handles CRUD operations for user data stored in text files.
 * Implements data persistence layer for the User model.
 */
public class UserDAO {
    private static final String USER_FILE = "users.txt";
    private FileManager fileManager;

    /**
     * Constructor initializes FileManager.
     */
    public UserDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Saves a user to the data file.
     * Format: userId,role,email,password,isActive,name
     *
     * @param user the user to save
     */
    public void saveUser(User user) {
        if (user == null) {
            return;
        }

        String line = userToCSV(user);
        fileManager.appendToFile(USER_FILE, line);
    }

    /**
     * Loads a user by ID from the data file.
     *
     * @param userId the user ID
     * @return the User object, or null if not found
     */
    public User loadUser(String userId) {
        List<User> users = loadAllUsers();
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Loads all users from the data file.
     *
     * @return list of all users
     */
    public List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        String content = fileManager.loadFromTextFile(USER_FILE);

        if (content.isEmpty()) {
            return users;
        }

        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            User user = csvToUser(line);
            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    /**
     * Updates an existing user's information.
     *
     * @param user the user with updated data
     */
    public void updateUser(User user) {
        deleteUser(user.getUserId());
        saveUser(user);
    }

    /**
     * Deletes a user from the data file.
     *
     * @param userId the user ID to delete
     */
    public void deleteUser(String userId) {
        List<User> users = loadAllUsers();
        StringBuilder newContent = new StringBuilder();

        for (User user : users) {
            if (!user.getUserId().equals(userId)) {
                newContent.append(userToCSV(user)).append("\n");
            }
        }

        fileManager.saveToTextFile(USER_FILE, newContent.toString());
    }

    /**
     * Finds a user by email address.
     *
     * @param email the email to search for
     * @return the User object, or null if not found
     */
    public User findByEmail(String email) {
        List<User> users = loadAllUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Converts a User object to CSV format.
     *
     * @param user the user to convert
     * @return CSV string representation
     */
    private String userToCSV(User user) {
        String name = "";
        if (user instanceof Admin) {
            name = ((Admin) user).getAdminName();
        } else if (user instanceof Instructor) {
            name = ((Instructor) user).getInstructorName();
        }

        return String.format("%s,%s,%s,%s,%b,%s",
                user.getUserId(),
                user.getRole(),
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                name);
    }

    /**
     * Converts a CSV line to a User object.
     *
     * @param line the CSV line
     * @return User object, or null if invalid
     */
    private User csvToUser(String line) {
        String[] fields = line.split(",");
        if (fields.length < 6) {
            return null;
        }

        String userId = fields[0];
        UserRole role = UserRole.valueOf(fields[1]);
        String email = fields[2];
        String password = fields[3];
        boolean isActive = Boolean.parseBoolean(fields[4]);
        String name = fields[5];

        User user;
        if (role == UserRole.ADMIN) {
            user = new Admin(userId, email, password, name);
        } else if (role == UserRole.INSTRUCTOR) {
            user = new Instructor(userId, email, password, name);
        } else {
            return null;
        }

        user.setActive(isActive);
        return user;
    }
}
