package gui;

import dao.PasswordResetDAO;
import services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * ResetPasswordFrame
 * ------------------
 * OOP Concepts:
 *  - Encapsulation: fields are private.
 *  - Modularity: separate window only for resetting password.
 *  - Abstraction: uses DAO + Service for logic.
 */
public class ResetPasswordFrame extends JFrame {

    private JTextField emailField;
    private JTextField codeField;
    private JPasswordField newPassField;
    private JPasswordField confirmPassField;

    public ResetPasswordFrame(String emailFromForgot) {

    setTitle("Reset Password");
    setSize(450, 280);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(6, 2, 10, 10));

    emailField = new JTextField(emailFromForgot);
    emailField.setEditable(false);

    codeField = new JTextField();
    newPassField = new JPasswordField();
    confirmPassField = new JPasswordField();

    // ---- Show Password Checkbox ----
    JCheckBox showPass = new JCheckBox("Show Password");

    showPass.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (showPass.isSelected()) {
                newPassField.setEchoChar((char) 0);
                confirmPassField.setEchoChar((char) 0);
            } else {
                newPassField.setEchoChar('•');
                confirmPassField.setEchoChar('•');
            }
        }
    });

    JButton resetBtn = new JButton("Reset Password");
    JButton clearBtn = new JButton("Clear Changes");

    // Reset button
    resetBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            resetPassword();
        }
    });

    // Clear button
    clearBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            codeField.setText("");
            newPassField.setText("");
            confirmPassField.setText("");
        }
    });

    // ---- Add UI Components ----
    add(new JLabel("Email:"));
    add(emailField);

    add(new JLabel("Reset Code:"));
    add(codeField);

    add(new JLabel("New Password:"));
    add(newPassField);

    add(new JLabel("Confirm Password:"));
    add(confirmPassField);

    add(showPass);        
    add(new JLabel(""));  // empty cell

    add(clearBtn);
    add(resetBtn);

    setVisible(true);
}

        

   

    // Method to Clear all input fields in Reset Password Frame
    private void clearFields() {
    codeField.setText("");
    newPassField.setText("");
    confirmPassField.setText("");
}


    /**
     * This Method validates the password strength.
     * Requirements:
     *  - At least 8 characters
     *  - At least 1 uppercase letter
     *  - At least 1 lowercase letter
     *  - At least 1 number
     *  - At least 1 special symbol
     */
    private boolean isStrongPassword(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;  // any non-letter/digit
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }


    /**
     * Validates code and updates user password.
     */
    private void resetPassword() {

        String email = emailField.getText().trim();
        String code = codeField.getText().trim();
        String newPass = new String(newPassField.getPassword());
        String confirm = new String(confirmPassField.getPassword());

        if (email.length() == 0 || code.length() == 0 ||
                newPass.length() == 0 || confirm.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        PasswordResetDAO resetDAO = new PasswordResetDAO();
        UserService userService = new UserService();

        // Check email exists
        if (userService.findByEmail(email) == null) {
            JOptionPane.showMessageDialog(this, "No user found with this email.");
            return;
        }

        // Check reset code
        if (!resetDAO.validateCode(email, code)) {
            JOptionPane.showMessageDialog(this, "Invalid or incorrect reset code.");
            return;
        }

        // Check password match
        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        // Password strength validation
        if (!isStrongPassword(newPass)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain:\n" +
                            "- At least 8 characters\n" +
                            "- One uppercase letter\n" +
                            "- One lowercase letter\n" +
                            "- One number\n" +
                            "- One special symbol",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean ok = userService.updatePasswordByEmail(email, newPass);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Password successfully reset.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password.");
        }
    }
}
