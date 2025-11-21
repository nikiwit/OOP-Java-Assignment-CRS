package dao;

import models.User;
import models.Admin;
import models.Instructor;
import enums.UserRole;
import utils.FileManager;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String USER_FILE = "users.txt";
    private static final String HEADER = "UserID,Role,Email,Password,Active,Name";

    private FileManager fileManager;

    public UserDAO() {
        this.fileManager = new FileManager();
        ensureHeaderExists();
    }

    // Make sure header is present in users.txt
    private void ensureHeaderExists() {
        String content = fileManager.loadFromTextFile(USER_FILE);

        if (!content.startsWith("UserID")) {
            // Add header at the top
            fileManager.saveToTextFile(USER_FILE, HEADER + "\n" + content);
        }
    }

    // Save a new user and keep the users.txt file sorted:

    // 1. Active users come first
    // 2. Within each group, users are sorted by UserID (A001, A002, I001, etc.)

    public void saveUser(User user) {
        if (user == null) return;

        java.util.List<User> users = loadAllUsers();
        users.add(user);

        // ---- Sorting logic ----
        for (int i = 0; i < users.size() - 1; i++) {
            for (int j = i + 1; j < users.size(); j++) {

                User a = users.get(i);
                User b = users.get(j);

                // Active users first
                if (!a.isActive() && b.isActive()) {
                    User temp = a;
                    users.set(i, b);
                    users.set(j, temp);
                }
                // If both are same active/inactive, sort by UserID
                else if (a.isActive() == b.isActive()) {
                    if (a.getUserId().compareTo(b.getUserId()) > 0) {
                        User temp = a;
                        users.set(i, b);
                        users.set(j, temp);
                    }
                }
            }
        }

        // Save sorted list back to file
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            builder.append(userToCSV(users.get(i))).append("\n");
        }

        fileManager.saveToTextFile(USER_FILE, builder.toString());
    }

   
    // Load a single user by ID
    public User loadUser(String userId) {
        List<User> users = loadAllUsers();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    // Load all users
    public List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        String content = fileManager.loadFromTextFile(USER_FILE);

        if (content.isEmpty()) {
            return users;
        }

        String[] lines = content.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Skip empty lines
            if (line.isEmpty()) continue;

            // Skip header line
            if (line.startsWith("UserID")) continue;

            User u = csvToUser(line);
            if (u != null) {
                users.add(u);
            }
        }

        return users;
    }

    // Update user info
    public void updateUser(User updatedUser) {
    java.util.List<User> users = loadAllUsers();

    // Replace existing record
    java.util.List<User> newList = new java.util.ArrayList<User>();
    for (int i = 0; i < users.size(); i++) {
        User u = users.get(i);
        if (!u.getUserId().equals(updatedUser.getUserId())) {
            newList.add(u);
        }
    }
    newList.add(updatedUser);

    // In "Add User" Button, To get the UserID automatically generated:
    // ---------------- SORT: Active users first, then by User ID ----------------------
    for (int i = 0; i < newList.size() - 1; i++) {
        for (int j = i + 1; j < newList.size(); j++) {

            User a = newList.get(i);
            User b = newList.get(j);

            // Active first
            if (!a.isActive() && b.isActive()) {
                User temp = a;
                newList.set(i, b);
                newList.set(j, temp);
            }

            // If both same status then sort by ID
            else if (a.isActive() == b.isActive()) {
                if (a.getUserId().compareTo(b.getUserId()) > 0) {
                    User temp = a;
                    newList.set(i, b);
                    newList.set(j, temp);
                }
            }
        }
    }

    // Save sorted list
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < newList.size(); i++) {
        builder.append(userToCSV(newList.get(i))).append("\n");
    }

    fileManager.saveToTextFile(USER_FILE, builder.toString());
}
  

    // Delete a user (rewrites file)
    public void deleteUser(String userId) {
        List<User> users = loadAllUsers();
        StringBuilder content = new StringBuilder();

        // Always write header first
        content.append(HEADER).append("\n");

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (!u.getUserId().equals(userId)) {
                content.append(userToCSV(u)).append("\n");
            }
        }

        fileManager.saveToTextFile(USER_FILE, content.toString());
    }

    // Convert User object to CSV
    private String userToCSV(User user) {
        return String.format("%s,%s,%s,%s,%b,%s",
                user.getUserId(),
                user.getRole(),
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                user.getName()
        );
    }

    // Convert CSV string to User object
    private User csvToUser(String line) {
        String[] f = line.split(",");

        if (f.length < 6) return null;

        String id = f[0];
        UserRole role = UserRole.valueOf(f[1]);
        String email = f[2];
        String password = f[3];
        boolean active = Boolean.parseBoolean(f[4]);
        String name = f[5];

        User user;

        if (role == UserRole.ADMIN) {
            user = new Admin(id, email, password, name);
        } else if (role == UserRole.INSTRUCTOR) {
            user = new Instructor(id, email, password, name);
        } else {
            return null;
        }

        user.setActive(active);
        return user;
    }

    // Find user by email
    public User findByEmail(String email) {
        List<User> users = loadAllUsers();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public String generateNextUserId(UserRole role) {

    List<User> users = loadAllUsers();

    // Determining prefix manually 
    String prefix;
    if (role == UserRole.ADMIN) {
        prefix = "A";
    } else {
        prefix = "I";
    }

    int max = 0;

    // Loop through all users to find the highest ID number for this role
    for (int i = 0; i < users.size(); i++) {
        User u = users.get(i);

        // Check prefix (e.g., "A" or "I")
        if (u.getUserId().startsWith(prefix)) {

            // Extract numeric part: A003 >> 003
            String numberPart = u.getUserId().substring(1);

            try {
                int value = Integer.parseInt(numberPart);

                if (value > max) {
                    max = value;   // update max
                }

            } catch (NumberFormatException ex) {
                // ignore invalid user IDs
            }
        }
    }

    // Next ID = max + 1  
    int next = max + 1;

    // Format to 3 digits (001, 002, 045...)
    return prefix + String.format("%03d", next);
}

}
