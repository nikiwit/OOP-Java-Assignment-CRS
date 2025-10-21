package dao;

import models.LoginLog;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for LoginLog entities.
 * Handles CRUD operations for login log data stored in binary files.
 * Implements data persistence layer for the LoginLog model.
 * Note: Uses binary file format as required by the assignment.
 * Data source: login_logs.dat (binary format)
 */
public class LoginLogDAO {
    private static final String LOGIN_LOG_FILE = "login_logs.dat";
    private FileManager fileManager;

    /**
     * Constructor initializes FileManager.
     */
    public LoginLogDAO() {
        this.fileManager = new FileManager();
    }

    /**
     * Saves a login log to the binary data file.
     * Appends to existing list of login logs.
     *
     * @param log the login log to save
     */
    public void saveLoginLog(LoginLog log) {
        if (log == null) {
            return;
        }

        List<LoginLog> allLogs = loadAllLoginLogs();
        allLogs.add(log);
        fileManager.saveToBinaryFile(LOGIN_LOG_FILE, allLogs);
    }

    /**
     * Loads a login log by ID from the binary data file.
     *
     * @param loginId the login ID
     * @return the LoginLog object, or null if not found
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

    /**
     * Loads all login logs from the binary data file.
     *
     * @return list of all login logs
     */
    @SuppressWarnings("unchecked")
    public List<LoginLog> loadAllLoginLogs() {
        Object data = fileManager.loadFromBinaryFile(LOGIN_LOG_FILE);
        if (data instanceof List) {
            return (List<LoginLog>) data;
        }
        return new ArrayList<>();
    }

    /**
     * Loads all login logs for a specific user.
     *
     * @param userId the user ID
     * @return list of login logs for the user
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

    /**
     * Updates an existing login log record.
     * Replaces the old log with the updated one.
     *
     * @param log the login log with updated data
     */
    public void updateLoginLog(LoginLog log) {
        if (log == null) {
            return;
        }

        List<LoginLog> allLogs = loadAllLoginLogs();
        for (int i = 0; i < allLogs.size(); i++) {
            if (allLogs.get(i).getLoginId().equals(log.getLoginId())) {
                allLogs.set(i, log);
                break;
            }
        }

        fileManager.saveToBinaryFile(LOGIN_LOG_FILE, allLogs);
    }

    /**
     * Gets the most recent active login log for a user.
     *
     * @param userId the user ID
     * @return the active LoginLog, or null if none found
     */
    public LoginLog getActiveLoginLog(String userId) {
        List<LoginLog> userLogs = loadLogsByUser(userId);
        for (int i = userLogs.size() - 1; i >= 0; i--) {
            if (userLogs.get(i).isActive()) {
                return userLogs.get(i);
            }
        }
        return null;
    }
}
