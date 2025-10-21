package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Login frame for user authentication.
 * Provides GUI interface for users to log into the Course Recovery System.
 * Supports role-based login for Admin and Instructor users.
 */
public class LoginFrame extends JFrame {

    private static final int FRAME_WIDTH = 450;
    private static final int FRAME_HEIGHT = 550;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    /**
     * Constructor to initialize the login frame.
     */
    public LoginFrame() {
        initializeFrame();
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the main frame settings.
     */
    private void initializeFrame() {
        setTitle("CRS - Course Registration System");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    /**
     * Initializes and arranges GUI components.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Login Form
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the header panel with title.
     */
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
        loginButton.addActionListener(e -> authenticate());
        panel.add(loginButton, gbc);

        // Message Label
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 20, 10, 20);
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, gbc);

        // Add Enter key listener for password field
        passwordField.addActionListener(e -> authenticate());

        return panel;
    }

    /**
     * Validates user credentials and authenticates the user.
     */
    private void authenticate() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Clear previous messages
        messageLabel.setText("");

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both email and password.", Color.RED);
            return;
        }

        // Call AuthenticationService to authenticate
        services.AuthenticationService authService = services.AuthenticationService.getInstance();
        models.User user = authService.authenticate(email, password);

        if (user == null) {
            showMessage("Invalid email or password.", Color.RED);
            return;
        }

        // Check if user is active
        if (!user.isActive()) {
            showMessage("Your account has been deactivated.", Color.RED);
            return;
        }

        // Open appropriate dashboard based on user role
        if (user instanceof models.Admin) {
            models.Admin admin = (models.Admin) user;
            new AdminDashboard(admin.getAdminName(), admin.getUserId());
            dispose();
        } else if (user instanceof models.Instructor) {
            models.Instructor instructor = (models.Instructor) user;
            new InstructorDashboard(instructor.getInstructorName(), instructor.getUserId());
            dispose();
        } else {
            showMessage("Invalid user role.", Color.RED);
        }
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     * @param color the color of the message
     */
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    /**
     * Main method to run the login frame.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
