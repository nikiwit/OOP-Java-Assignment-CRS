package services;

import models.User;
import models.LoginLog;
import dao.UserDAO;
import dao.LoginLogDAO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton service for handling user authentication and session management.
 * Manages login/logout operations, session validation, and login history tracking.
 * Implements the Singleton design pattern to ensure only one instance exists.
 */
public class AuthenticationService {
    private static AuthenticationService instance;
    private UserDAO userDAO;
    private LoginLogDAO loginLogDAO;
    private Map<String, User> activeSessions;

    /**
     * Private constructor to prevent direct instantiation.
     * Part of the Singleton pattern implementation.
     */
    private AuthenticationService() {
        this.userDAO = new UserDAO();
        this.loginLogDAO = new LoginLogDAO();
        this.activeSessions = new HashMap<>();
    }

    /**
     * Gets the single instance of AuthenticationService.
     * Creates the instance if it doesn't exist (lazy initialization).
     *
     * @return the singleton instance
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Authenticates a user with email and password credentials.
     * Creates a login log entry for successful authentication.
     *
     * @param email the user's email
     * @param password the user's password
     * @return the authenticated User object, or null if authentication fails
     */
    public User authenticate(String email, String password) {
        // Validate input
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }

        // Find user by email
        User user = userDAO.findByEmail(email);

        // Check if user exists and credentials match
        if (user != null && user.login(email, password)) {
            // Create session
            activeSessions.put(user.getUserId(), user);

            // Create login log entry
            LoginLog loginLog = new LoginLog(user.getUserId());
            loginLogDAO.saveLoginLog(loginLog);

            return user;
        }

        return null;
    }

    /**
     * Creates a new login log entry for a user session.
     *
     * @param user the user who logged in
     * @return the created LoginLog object
     */
    public LoginLog createLoginLog(User user) {
        LoginLog loginLog = new LoginLog(user.getUserId());
        loginLogDAO.saveLoginLog(loginLog);
        return loginLog;
    }

    /**
     * Logs out a user and ends their session.
     * Updates the active login log with logout timestamp.
     *
     * @param userId the user ID to logout
     */
    public void logout(String userId) {
        if (userId != null) {
            // Find active login log and update it with logout timestamp
            LoginLog activeLog = loginLogDAO.getActiveLoginLog(userId);
            if (activeLog != null) {
                activeLog.logout(new Date());
                loginLogDAO.updateLoginLog(activeLog);
            }

            // Remove from active sessions
            activeSessions.remove(userId);
        }
    }

    /**
     * Validates if a user's session is still active.
     *
     * @param userId the user ID to validate
     * @return true if session is valid, false otherwise
     */
    public boolean validateSession(String userId) {
        return activeSessions.containsKey(userId);
    }

    /**
     * Retrieves the login history for a specific user.
     *
     * @param userId the user ID
     * @return list of LoginLog objects
     */
    public List<LoginLog> getLoginHistory(String userId) {
        return loginLogDAO.loadLogsByUser(userId);
    }

    /**
     * Gets the currently logged in user by user ID.
     *
     * @param userId the user ID
     * @return the User object if logged in, null otherwise
     */
    public User getLoggedInUser(String userId) {
        return activeSessions.get(userId);
    }
}
