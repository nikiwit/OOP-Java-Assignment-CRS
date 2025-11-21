package gui;

import dao.PasswordResetDAO;
import services.UserService;
import services.EmailNotificationService;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordFrame extends JFrame {

    private JTextField emailField;

    public ForgotPasswordFrame() {

        setTitle("Forgot Password");
        setSize(350, 200);
        setLayout(new GridLayout(3, 1, 10, 10));
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Enter your Email:", SwingConstants.CENTER);
        emailField = new JTextField();

        JButton sendButton = new JButton("Send Reset Code");

        
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendResetCode();
            }
        });

        add(label);
        add(emailField);
        add(sendButton);

        setVisible(true);
    }

    private void sendResetCode() {

        String email = emailField.getText().trim();

        UserService userService = new UserService();
        PasswordResetDAO resetDAO = new PasswordResetDAO();

        // 1. Check if email exists
        if (userService.findByEmail(email) == null) {
            JOptionPane.showMessageDialog(this, "Email not found!");
            return;
        }

        // 2. Generate random reset code
        String code = resetDAO.generateResetCode();

        // 3. Save reset request in password_reset.txt
        resetDAO.saveResetRequest(email, code);

        // 4. Send email (Singleton)
        EmailNotificationService emailer = EmailNotificationService.getInstance();
        emailer.sendPasswordResetEmail(email, code);

        JOptionPane.showMessageDialog(
                this,
                "A reset code has been sent to your email.\nPlease check your inbox."
        );

        // OPEN RESET WINDOW WITH EMAIL AUTO-FILLED
        new ResetPasswordFrame(email);
        dispose();
      
    }
}
