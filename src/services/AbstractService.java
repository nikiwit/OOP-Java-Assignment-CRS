package services;

/**
 * Abstract base class for all service layer components.
 * Provides common validation and logging functionality.
 */
public abstract class AbstractService {

    /**
     * Validates that an object is not null.
     * @param obj the object to validate
     * @param paramName the parameter name for error messages
     * @throws IllegalArgumentException if object is null
     */
    protected void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }

    /**
     * Validates that a string is not null or empty.
     * @param str the string to validate
     * @param paramName the parameter name for error messages
     * @throws IllegalArgumentException if string is null or empty
     */
    protected void validateNotEmpty(String str, String paramName) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be null or empty");
        }
    }

    /**
     * Logs an informational message.
     * @param message the message to log
     */
    protected void logInfo(String message) {
        System.out.println("[" + getServiceName() + "] " + message);
    }

    /**
     * Logs an error message.
     * @param message the error message to log
     */
    protected void logError(String message) {
        System.err.println("[" + getServiceName() + " ERROR] " + message);
    }

    /**
     * Gets the name of this service for logging purposes.
     * Must be implemented by concrete services.
     * @return the service name
     */
    protected abstract String getServiceName();
}
