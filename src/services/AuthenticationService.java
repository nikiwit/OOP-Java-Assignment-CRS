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
 * AuthenticationService
 * ----------------------
 * This class handles:
 *  - User login
 *  - Checking password
 *  - Checking ACTIVE / INACTIVE status
 *  - Creating login logs
 *  - Logout handling
 *
 * It uses the Singleton pattern → only ONE instance is ever created.
 * Beginner-friendly design: no lambdas, simple logic, clear comments.
 */
public class AuthenticationService {

    // ---------- Singleton Instance ----------
    private static AuthenticationService instance;

    // ---------- Data Access Objects ----------
    private UserDAO userDAO;               // Reads/writes users.txt
    private LoginLogDAO loginLogDAO;       // Reads/writes login_logs.txt

    // Stores currently logged-in sessions
    private Map<String, User> activeSessions;

    /**
     * PRIVATE constructor (Singleton pattern)
     * Prevents other classes from creating an object.
     */
    private AuthenticationService() {
        this.userDAO = new UserDAO();
        this.loginLogDAO = new LoginLogDAO();
        this.activeSessions = new HashMap<>();
    }

    /**
     * Returns the SINGLE instance (lazy initialization).
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }


    // =========================================================================
    //                       USER AUTHENTICATION LOGIC
    // =========================================================================

    /**
     * Authenticates a user by email and password.
     * Returns:
     *   - null  =  email not found OR wrong password
     *   - User object (active) = successful login
     *   - User object (inactive) = LoginFrame will detect inactive status
     */

    public User authenticate(String email, String password) {

        // ----------- Basic input validation -----------
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }

        // ----------- Find user in file -----------
        User user = userDAO.findByEmail(email);

        if (user == null) {
            // No user with this email
            return null;
        }

        // ----------- Check inactive status -----------
        if (!user.isActive()) {
            // Returns user as-is; LoginFrame will show "account deactivated"
            return user;
        }

        // ----------- Check password correctness -----------
        if (!user.login(email, password)) {
            return null;   // wrong password
        }

        // ----------- Successful Login -----------
        activeSessions.put(user.getUserId(), user);

        // Record login log
        LoginLog log = new LoginLog(user.getUserId());
        loginLogDAO.saveLoginLog(log);

        return user;
    }



    // =========================================================================
    //                               LOGOUT
    // =========================================================================

    /**
     * Logs out the user and updates login log with logout timestamp.
     */
    public void logout(String userId) {

        if (userId == null) {
            return;
        }

        // Get latest active login record
        LoginLog activeLog = loginLogDAO.getActiveLoginLog(userId);

        if (activeLog != null) {
            activeLog.logout(new Date());           // record logout time
            loginLogDAO.updateLoginLog(activeLog);  // save updated log
        }

        // Remove from active session map
        activeSessions.remove(userId);
    }


    // =========================================================================
    //                     HELPER / SESSION MANAGEMENT
    // =========================================================================

    /**
     * Checks if a user currently has an active session.
     */
    public boolean validateSession(String userId) {
        return activeSessions.containsKey(userId);
    }

    /**
     * Returns login history of the user.
     */
    public List<LoginLog> getLoginHistory(String userId) {
        return loginLogDAO.loadLogsByUser(userId);
    }

    /**
     * Returns the user object stored in active session.
     */
    public User getLoggedInUser(String userId) {
        return activeSessions.get(userId);
    }

}
