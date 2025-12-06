package gui;

import services.UserService;
import services.EmailNotificationService;
import dao.UserDAO;
import enums.UserRole;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUserFrame extends JFrame {

    private AdminDashboard parent;
    private JTextField idField, nameField, emailField;
    private JPasswordField passwordField;   // changed to JPasswordField
    private JComboBox<UserRole> roleBox;
    private UserService userService;

    public AddUserFrame(AdminDashboard parent) {

        this.parent = parent;
        this.userService = new UserService();

        setTitle("Add User");
        setSize(430, 380);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10));

        // ---- USER ID FIELD (non-editable style) ----
        idField = new JTextField();
        idField.setEditable(false);
        idField.setEnabled(false);
        idField.setDisabledTextColor(Color.DARK_GRAY);
        idField.setBackground(new Color(240, 240, 240));

        nameField = new JTextField();
        emailField = new JTextField();

        // ---- PASSWORD FIELD (masked) ----
        passwordField = new JPasswordField();

        // ---- ROLE DROPDOWN ----
        roleBox = new JComboBox<UserRole>();
        roleBox.addItem(UserRole.INSTRUCTOR);

        // ---- AUTO-GENERATE USER ID ----
        UserDAO userDAO = new UserDAO();
        String nextId = userDAO.generateNextUserId(UserRole.INSTRUCTOR);
        idField.setText(nextId);

        // ---- Update ID when role changes (future-proof) ----
        roleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserRole selectedRole = (UserRole) roleBox.getSelectedItem();
                UserDAO dao = new UserDAO();
                String newId = dao.generateNextUserId(selectedRole);
                idField.setText(newId);
            }
        });

        // ----------------------------
        // ADD UI COMPONENTS TO FRAME
        // ----------------------------

        add(new JLabel("User ID:"));
        add(idField);

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Password:"));
        add(passwordField);

        // ----------------------------
        // SHOW PASSWORD CHECKBOX
        // ----------------------------
        add(new JLabel("Show Password:"));

        JCheckBox showPass = new JCheckBox();
        showPass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPass.isSelected()) {
                    passwordField.setEchoChar((char) 0);  // Show characters
                } else {
                    passwordField.setEchoChar('•');      // Mask characters
                }
            }
        });
        add(showPass);

        // ----------------------------
        // ROLE DROPDOWN
        // ----------------------------
        add(new JLabel("Role:"));
        add(roleBox);

        // ----- SPACING ROW -----
        add(new JLabel(""));
        add(new JLabel(""));

        // ----------------------------
        // CLEAR BUTTON
        // ----------------------------
        JButton clearBtn = new JButton("Clear Changes");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
            }
        });

        // ----------------------------
        // SAVE BUTTON
        // ----------------------------
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveUser();
            }
        });

        add(clearBtn);
        add(saveBtn);

        setVisible(true);
    }

    // ============================================================
    // SAVE USER LOGIC (with password validation + email rules)
    // ============================================================
    private void saveUser() {

    String id = idField.getText().trim();
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText().trim();
    UserRole role = (UserRole) roleBox.getSelectedItem();

    if (id.length() == 0 || name.length() == 0 ||
            email.length() == 0 || password.length() == 0) {

        JOptionPane.showMessageDialog(
                this,
                "Please fill all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (!email.endsWith("@crs.edu")) {
        JOptionPane.showMessageDialog(
                this,
                "Email must end with @crs.edu",
                "Invalid Email",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // ---- PASSWORD CHECK ----
    if (!isPasswordStrong(password)) {
        showWeakPasswordPopup();
        return;
    }

    boolean success = userService.createUser(id, email, password, name, role);

    if (success) {
        // Send welcome email notification
        try {
            EmailNotificationService emailService = EmailNotificationService.getInstance();
            User newUser = userService.findByEmail(email);
            if (newUser != null) {
                emailService.sendAccountCreationEmail(newUser);
            }
        } catch (Exception ex) {
            System.err.println("Warning: Failed to send welcome email - " + ex.getMessage());
        }

        JOptionPane.showMessageDialog(this, "User added successfully! Welcome email sent.");
        parent.refreshUserTable();
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Failed to add user.");
    }
}


    // ---------------- PASSWORD VALIDATION ----------------
    private boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;  // uppercase
        if (!password.matches(".*[a-z].*")) return false;  // lowercase
        if (!password.matches(".*\\d.*")) return false;     // number
        if (!password.matches(".*[@#$%^&+=!?.*_-].*")) return false; // special char
        return true;
    }

    // POPUP MESSAGE FOR WEAK PASSWORD
    private void showWeakPasswordPopup() {
        JOptionPane.showMessageDialog(
                this,
                "Password must contain:\n"
                + "- At least 8 characters\n"
                + "- One uppercase letter\n"
                + "- One lowercase letter\n"
                + "- One number\n"
                + "- One special symbol",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE
        );
    }

}
