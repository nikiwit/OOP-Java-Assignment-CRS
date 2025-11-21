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
        setSize(450, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        emailField = new JTextField(emailFromForgot);
        emailField.setEditable(false);   // user cannot change email

        codeField = new JTextField();
        newPassField = new JPasswordField();
        confirmPassField = new JPasswordField();

        JButton resetBtn = new JButton("Reset Password");
        JButton clearBtn = new JButton("Clear Changes");

        // Reset button action
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPassword();
            }
        });

        // Clear button action
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                codeField.setText("");
                newPassField.setText("");
                confirmPassField.setText("");
            }
        });

        // Add UI components
        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Reset Code:"));
        add(codeField);

        add(new JLabel("New Password:"));
        add(newPassField);

        add(new JLabel("Confirm Password:"));
        add(confirmPassField);

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

        boolean ok = userService.updatePasswordByEmail(email, newPass);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Password successfully reset.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password.");
        }
    }
}
