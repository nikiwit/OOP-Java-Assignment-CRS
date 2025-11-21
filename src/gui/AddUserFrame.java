package gui;

import services.UserService;
import dao.UserDAO;
import enums.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUserFrame extends JFrame {

    private AdminDashboard parent;
    private JTextField idField, nameField, emailField, passwordField;
    private JComboBox<UserRole> roleBox;
    private UserService userService;

    public AddUserFrame(AdminDashboard parent) {

        this.parent = parent;
        this.userService = new UserService();

        setTitle("Add User");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));


        // ---- USER ID FIELD (blurred style) ----
        idField = new JTextField();
        idField.setEditable(false);                          // cannot edit
        idField.setEnabled(false);                           // show blurred style
        idField.setDisabledTextColor(Color.DARK_GRAY);       // text readable but dim
        idField.setBackground(new Color(240, 240, 240));     // light gray background



        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JTextField();

        // ---- ROLE DROPDOWN ----
        // Only INSTRUCTOR allowed (ADMIN removed)
        roleBox = new JComboBox<UserRole>();
        roleBox.addItem(UserRole.INSTRUCTOR);

        // ---- AUTO-GENERATE USER ID ----
        UserDAO userDAO = new UserDAO();
        String nextId = userDAO.generateNextUserId(UserRole.INSTRUCTOR);
        idField.setText(nextId);

        // ---- Update ID if role changes (even though only INSTRUCTOR exists) ----
        roleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserRole selectedRole = (UserRole) roleBox.getSelectedItem();
                UserDAO dao = new UserDAO();
                String newId = dao.generateNextUserId(selectedRole);
                idField.setText(newId);
            }
        });

        // Add UI components
        add(new JLabel("User ID:"));
        add(idField);

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(new JLabel("Role:"));
        add(roleBox);

        // ---- EMPTY ROW FOR SPACING ----
        add(new JLabel(""));
        add(new JLabel(""));

        // ---- BUTTONS ----
        JButton clearBtn = new JButton("Clear Changes");
        JButton saveBtn = new JButton("Save");

        // CLEAR BUTTON LOGIC
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
            }
        });

        // SAVE BUTTON LOGIC
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveUser();
            }
        });

        add(clearBtn);
        add(saveBtn);

        setVisible(true);
    }

    
    // ---------------- SAVE USER LOGIC ----------------
    private void saveUser() {

    String id = idField.getText().trim();
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText().trim();
    UserRole role = (UserRole) roleBox.getSelectedItem();

    // ---- 1) CHECK FOR EMPTY FIELDS ----
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

    // ---- 2) EMAIL MUST END WITH @crs.edu ----
    if (!email.endsWith("@crs.edu")) {
        JOptionPane.showMessageDialog(
                this,
                "Email must end with @crs.edu",
                "Invalid Email",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // ---- 3) CREATE USER ----
    boolean success = userService.createUser(id, email, password, name, role);

    if (success) {
        JOptionPane.showMessageDialog(this,
                "User added successfully!");
        parent.refreshUserTable();
        dispose();
    } else {
        JOptionPane.showMessageDialog(this,
                "Failed to add user.");
    }
}

}
