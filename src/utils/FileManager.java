package utils;

/**
 * Utility class for managing file I/O operations.
 * Handles both text and binary file storage as required by the assignment.
 * Provides methods for reading, writing, and parsing data files.
 */
public class FileManager {
    private String dataDirectory;

    /**
     * Saves data to a text file.
     * @param fileName the name of the file
     * @param data the data to save
     */
    public void saveToTextFile(String fileName, String data) {
        // To be implemented
    }

    /**
     * Loads data from a text file.
     * @param fileName the name of the file
     * @return the file contents as a string
     */
    public String loadFromTextFile(String fileName) {
        // To be implemented
        return null;
    }

    /**
     * Saves an object to a binary file.
     * @param fileName the name of the file
     * @param data the object to save
     */
    public void saveToBinaryFile(String fileName, Object data) {
        // To be implemented
    }

    /**
     * Loads an object from a binary file.
     * @param fileName the name of the file
     * @return the deserialized object
     */
    public Object loadFromBinaryFile(String fileName) {
        // To be implemented
        return null;
    }

    /**
     * Appends data to an existing file.
     * @param fileName the name of the file
     * @param data the data to append
     */
    public void appendToFile(String fileName, String data) {
        // To be implemented
    }

    /**
     * Checks if a file exists.
     * @param fileName the name of the file
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String fileName) {
        // To be implemented
        return false;
    }

    /**
     * Parses a CSV line into an array of values.
     * @param line the CSV line to parse
     * @return array of string values
     */
    public String[] parseCSVLine(String line) {
        // To be implemented
        return null;
    }

    // Getters and setters to be implemented
}
