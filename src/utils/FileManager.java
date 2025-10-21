package utils;

import java.io.*;

/**
 * Utility class for managing file I/O operations.
 * Handles both text and binary file storage as required by the assignment.
 * Provides methods for reading, writing, and parsing data files.
 */
public class FileManager {
    private static final String DEFAULT_DATA_DIRECTORY = "data/";
    private String dataDirectory;

    /**
     * Default constructor using default data directory.
     */
    public FileManager() {
        this.dataDirectory = DEFAULT_DATA_DIRECTORY;
        createDataDirectoryIfNotExists();
    }

    /**
     * Constructor with custom data directory.
     *
     * @param dataDirectory the custom data directory path
     */
    public FileManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        createDataDirectoryIfNotExists();
    }

    /**
     * Creates the data directory if it doesn't exist.
     */
    private void createDataDirectoryIfNotExists() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves data to a text file.
     *
     * @param fileName the name of the file
     * @param data the data to save
     */
    public void saveToTextFile(String fileName, String data) {
        String filePath = dataDirectory + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("Error saving to text file: " + e.getMessage());
        }
    }

    /**
     * Loads data from a text file.
     *
     * @param fileName the name of the file
     * @return the file contents as a string, or empty string if file doesn't exist
     */
    public String loadFromTextFile(String fileName) {
        String filePath = dataDirectory + fileName;
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            return "";
        } catch (IOException e) {
            System.err.println("Error reading text file: " + e.getMessage());
            return "";
        }

        return content.toString();
    }

    /**
     * Saves an object to a binary file.
     *
     * @param fileName the name of the file
     * @param data the object to save
     */
    public void saveToBinaryFile(String fileName, Object data) {
        String filePath = dataDirectory + fileName;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving to binary file: " + e.getMessage());
        }
    }

    /**
     * Loads an object from a binary file.
     *
     * @param fileName the name of the file
     * @return the deserialized object, or null if error occurs
     */
    public Object loadFromBinaryFile(String fileName) {
        String filePath = dataDirectory + fileName;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Binary file not found: " + filePath);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading binary file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Appends data to an existing file.
     *
     * @param fileName the name of the file
     * @param data the data to append
     */
    public void appendToFile(String fileName, String data) {
        String filePath = dataDirectory + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
        }
    }

    /**
     * Checks if a file exists.
     *
     * @param fileName the name of the file
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String fileName) {
        String filePath = dataDirectory + fileName;
        return new File(filePath).exists();
    }

    /**
     * Parses a CSV line into an array of values.
     *
     * @param line the CSV line to parse
     * @return array of string values
     */
    public String[] parseCSVLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        return line.split(",");
    }

    /**
     * Gets the data directory path.
     *
     * @return the data directory path
     */
    public String getDataDirectory() {
        return dataDirectory;
    }

    /**
     * Sets the data directory path.
     *
     * @param dataDirectory the new data directory path
     */
    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        createDataDirectoryIfNotExists();
    }
}
