package dao;

import models.LoginLog;
import utils.FileManager;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * LoginLogDAO (Data Access Object)
 * ---------------------------------
 * OOP Concepts Used:
 *  - Encapsulation  >> All file-handling is hidden inside this DAO class.
 *  - Abstraction   >>  GUI/Services do NOT know how the binary file works.
 *  - Modularity    >> Clear separation of: Model vs DAO vs Service.
 *
 * This class handles:
 *   - Saving LoginLog objects
 *   - Loading logs from binary file (login_logs.dat)
 *   - Updating logout timestamps
 *   - Filtering by user
 *
 * All logs are stored in **binary form**, as required by the assignment.
 */
public class LoginLogDAO {

    private static final String LOGIN_LOG_FILE = "login_logs.dat";
    private FileManager fileManager;

    /**
     * Constructor initializes FileManager for file operations.
     */
    public LoginLogDAO() {
        this.fileManager = new FileManager();
    }

    // =====================================================================
    //                           SAVE LOGIN LOG
    // =====================================================================

    /**
     * Saves a new login record.
     * Appends the log to the existing list in the binary file.
     *
     * @param log the LoginLog object to save
     */
    public void saveLoginLog(LoginLog log) {

        if (log == null) {
            System.err.println("saveLoginLog ERROR: log is null");
            return;
        }

        List<LoginLog> allLogs = loadAllLoginLogs();
        allLogs.add(log);

        fileManager.saveToBinaryFile(LOGIN_LOG_FILE, allLogs);
    }

    // =====================================================================
    //                       LOAD ALL LOGIN LOGS
    // =====================================================================

    /**
     * Loads ALL login logs from login_logs.dat (binary file).
     *
     * @return list of LoginLog objects (empty list if none found)
     */
    @SuppressWarnings("unchecked")
    public List<LoginLog> loadAllLoginLogs() {

        Object data = fileManager.loadFromBinaryFile(LOGIN_LOG_FILE);

        if (data instanceof List) {
            return (List<LoginLog>) data;
        }

        return new ArrayList<>();
    }

    // =====================================================================
    //                       LOAD LOG BY LOGIN ID
    // =====================================================================

    /**
     * Loads a single login log by log ID.
     *
     * @param loginId the ID of the log entry
     * @return LoginLog if found, otherwise null
     */
    public LoginLog loadLoginLog(String loginId) {

        List<LoginLog> allLogs = loadAllLoginLogs();

        for (LoginLog log : allLogs) {
            if (log.getLoginId().equals(loginId)) {
                return log;
            }
        }
        return null;
    }

    // =====================================================================
    //                       LOAD LOGS BY USER ID
    // =====================================================================

    /**
     * Loads all login logs for a specific user.
     *
     * @param userId the user's ID
     * @return List of LoginLog entries for that user
     */
    public List<LoginLog> loadLogsByUser(String userId) {

        List<LoginLog> userLogs = new ArrayList<>();
        List<LoginLog> allLogs = loadAllLoginLogs();

        for (LoginLog log : allLogs) {
            if (log.getUserId().equals(userId)) {
                userLogs.add(log);
            }
        }

        return userLogs;
    }

    // =====================================================================
    //                       UPDATE EXISTING LOG
    // =====================================================================

    /**
     * Updates an existing login log.
     * Used during logout to store logout timestamp and sessionDuration.
     *
     * @param updatedLog the updated LoginLog object
     */
    public void updateLoginLog(LoginLog updatedLog) {

        if (updatedLog == null) {
            System.err.println("updateLoginLog ERROR: log is null");
            return;
        }

        List<LoginLog> allLogs = loadAllLoginLogs();

        for (int i = 0; i < allLogs.size(); i++) {

            if (allLogs.get(i).getLoginId().equals(updatedLog.getLoginId())) {
                allLogs.set(i, updatedLog);  // Replace old entry
                break;
            }
        }

        fileManager.saveToBinaryFile(LOGIN_LOG_FILE, allLogs);
    }

    // =====================================================================
    //                     GET ACTIVE LOGIN SESSION
    // =====================================================================

    /**
     * Gets the user's most recent login log with NO logout timestamp.
     * (Meaning the user is still logged in.)
     *
     * @param userId the user's ID
     * @return LoginLog if active session exists, otherwise null
     */
    public LoginLog getActiveLoginLog(String userId) {

        List<LoginLog> logs = loadLogsByUser(userId);

        // Reverse loop >> newest entries come last
        for (int i = logs.size() - 1; i >= 0; i--) {
            if (logs.get(i).isActive()) {
                return logs.get(i);
            }
        }

        return null;
    }

}
