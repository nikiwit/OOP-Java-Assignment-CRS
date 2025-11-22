package gui;

import javax.swing.*;
import java.awt.*;

import services.AuthenticationService;
import models.User;
import models.Admin;
import models.Instructor;

public class LoginFrame extends JFrame {

    private static final int FRAME_WIDTH = 450;
    private static final int FRAME_HEIGHT = 550;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        initializeFrame();
        initializeComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("CRS - Course Registration System");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, 100));
        panel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Course Registration System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createCenterPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---------- WELCOME ----------
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, gbc);

        // Reset grid width
        gbc.gridwidth = 1;

        // ---------- EMAIL LABEL ----------
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(emailLabel, gbc);

        // ---------- EMAIL FIELD ----------
        gbc.gridy = 2;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(300, 45));   // height increased
        panel.add(emailField, gbc);

        // ---------- PASSWORD LABEL ----------
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordLabel, gbc);

        // ---------- PASSWORD FIELD ----------
        gbc.gridy = 4;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 45)); // height increased
        panel.add(passwordField, gbc);

        // ---------- SHOW PASSWORD ----------
        gbc.gridy = 5;
        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setBackground(BACKGROUND_COLOR);

        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (showPass.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });

        panel.add(showPass, gbc);

        // ---------- LOGIN BUTTON ----------
        gbc.gridy = 6;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 45));

        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authenticate();
            }
        });

        panel.add(loginButton, gbc);

        // ---------- FORGOT PASSWORD ----------
        gbc.gridy = 7;
        JButton forgotButton = new JButton("Forgot Password?");
        forgotButton.setForeground(PRIMARY_COLOR);
        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);

        forgotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ForgotPasswordFrame();
            }
        });

        panel.add(forgotButton, gbc);

        // ---------- MESSAGE LABEL ----------
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL; // restore

        return panel;
    }

    private void authenticate() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        messageLabel.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both email and password.", Color.RED);
            return;
        }

        AuthenticationService authService = AuthenticationService.getInstance();
        User user;

        try {
            user = authService.authenticate(email, password);
        } catch (Exception e) {
            showMessage("An error occurred during login.", Color.RED);
            return;
        }

        if (user == null) {
            showMessage("Invalid email or password.", Color.RED);
            return;
        }

        if (!user.isActive()) {
            showMessage("Your account is deactivated. Please contact Admin.", Color.RED);
            return;
        }

        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            new AdminDashboard(admin.getAdminName(), admin.getUserId());
            dispose();
        } else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            new InstructorDashboard(instructor.getInstructorName(), instructor.getUserId());
            dispose();
        }
    }

    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();
            }
        });
    }
}

