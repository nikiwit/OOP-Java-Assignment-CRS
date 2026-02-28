package gui;

import services.UserService;
import models.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;


public class UpdateUserFrame extends JFrame {

    private AdminDashboard parent;

    private JTextField idField, nameField, emailField, roleField;
    private JCheckBox activeBox;

    private UserService userService;
    private User foundUser;

    public UpdateUserFrame(AdminDashboard parent, String userId) {
        this.parent = parent;
        this.userService = new UserService();

        setTitle("Update User");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        // ---------------- Load User ----------------
        foundUser = null;
        java.util.List<User> allUsers = userService.getAllUsers();

        for (int i = 0; i < allUsers.size(); i++) {
            User u = allUsers.get(i);
            if (u.getUserId().equals(userId)) {
                foundUser = u;
                break;
            }
        }

        if (foundUser == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            dispose();
            return;
        }

        // ---------------- Fields ----------------
        idField = new JTextField(foundUser.getUserId());
        idField.setEnabled(false);

        nameField = new JTextField(foundUser.getName());
        emailField = new JTextField(foundUser.getEmail());

        // Role shown but NOT editable
        roleField = new JTextField(foundUser.getRole().toString());
        roleField.setEnabled(false);       // greyed out, cannot edit

        activeBox = new JCheckBox("Active", foundUser.isActive());

        // ---------------- Layout ----------------
        add(new JLabel("User ID:"));
        add(idField);

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Role:"));
        add(roleField);

        add(new JLabel("Status:"));
        add(activeBox);

        // ----- BUTTONS -----
        JButton clearBtn = new JButton("Clear Changes");
        JButton updateBtn = new JButton("Update");

        // CLEAR button logic
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                emailField.setText("");
                activeBox.setSelected(foundUser.isActive());  // reset status to original
            }
        });

        // UPDATE button logic
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        // Add buttons to panel
        add(clearBtn);
        add(updateBtn);

        setVisible(true);
    }

    // ---------------- Update Logic ----------------
    private void updateUser() {

        try {
            String id = idField.getText().trim();
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            boolean active = activeBox.isSelected();

            // ---- 1) VALIDATION: Check empty fields ----
            if (newName.length() == 0 || newEmail.length() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields before updating.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;  // Stop update process
            }
            
            // ---- 2) VALIDATION: Email must end with @crs.edu ----
            if (!newEmail.toLowerCase().endsWith("@crs.edu")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Email must end with @crs.edu",
                        "Invalid Email",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // ---- 3) CALL UPDATE METHOD ----
            boolean success = userService.updateUserFull(
                    id,
                    newName,
                    newEmail,
                    foundUser.getRole().toString(), // Keep original role
                    active,
                    foundUser.getPassword()         // Keep old password
            );

            // ---- 4) SHOW RESULT MESSAGE ----
            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "User updated successfully!",
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                parent.refreshUserTable();
                dispose(); // Close window
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update user.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            // ---- 5) ERROR HANDLING ----
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while updating user:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

}
