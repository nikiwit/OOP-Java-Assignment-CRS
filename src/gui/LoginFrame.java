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

    // ------------------- WINDOW SETTINGS -------------------
    private void initializeFrame() {
        setTitle("CRS - Course Registration System");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    // ------------------- MAIN UI SETUP -------------------
    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
    }

    // ------------------- HEADER PANEL -------------------
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

    // ------------------- LOGIN FORM -------------------
        /**
     * Creates the center panel with login form.
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomeLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 30, 20);
        panel.add(welcomeLabel, gbc);
        

        // Email Label
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(TEXT_COLOR);
        panel.add(emailLabel, gbc);

        // Email Field
        gbc.gridy = 2;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(300, 35));
        panel.add(emailField, gbc);

        // Password Label
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 20, 5, 20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        panel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 20, 5, 20);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 35));
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridy = 5;
        gbc.insets = new Insets(25, 20, 10, 20);

        JButton loginButton = new JButton("Login");  
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authenticate();
            }
        });
        panel.add(loginButton, gbc);

        // "Forgot Password?" Button
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 20, 5, 20);
        JButton forgotButton = new JButton("Forgot Password?");
        forgotButton.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotButton.setForeground(PRIMARY_COLOR);
        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);
        forgotButton.setFocusPainted(false);

        forgotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ForgotPasswordFrame();
            }
        });

        panel.add(forgotButton, gbc);

        // Message Label
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 20, 10, 20);
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, gbc);

        // Press Enter to login
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authenticate();
            }
        });

        return panel;
    }


    // ------------------- AUTHENTICATION LOGIC -------------------
    private void authenticate() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Clear old message
        messageLabel.setText("");

        // Basic Validation
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

        // Incorrect Email or Password
        if (user == null) {
            showMessage("Invalid email or password.", Color.RED);
            return;
        }

        // User Exists BUT Account is Inactive
        if (!user.isActive()) {
            showMessage("Your account is deactivated. Please contact Admin.", Color.RED);
            return;
        }

        // Admin Login
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            new AdminDashboard(admin.getAdminName(), admin.getUserId());
            dispose();
        }
        // Instructor Login
        else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            new InstructorDashboard(instructor.getInstructorName(), instructor.getUserId());
            dispose();
        }
        // Unknown Role
        else {
            showMessage("Invalid user role.", Color.RED);
        }
    }

    // ------------------- UTILITY MESSAGE METHOD -------------------
    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    // ------------------- MAIN METHOD -------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            new LoginFrame();
            }
        });
    }
}
