package org.example.UI;

import org.example.Controller.AuthController;
import org.example.Controller.CarController;
import org.example.Controller.ReservationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {
    private AuthController authController;
    private CarController carController;
    private ReservationController reservationController;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Login/Register panels
    private JPanel loginPanel;
    private JPanel registerPanel;

    // Main app panels
    private JPanel dashboardPanel;
    private JPanel profilePanel;
    private JPanel carsListPanel;
    private JPanel carDetailsPanel;
    private JPanel reservationsPanel;
    private JPanel adminPanel;

    // Main menu components
    private JButton dashboardBtn;
    private JButton profileBtn;
    private JButton carsBtn;
    private JButton reservationsBtn;
    private JButton adminBtn;
    private JButton logoutBtn;

    public MainFrame() {
        authController = new AuthController();
        carController = new CarController();
        reservationController = new ReservationController();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("RideOn Car Rental System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize all panels
        createLoginPanel();
        createRegisterPanel();
        createDashboardPanel();
        createProfilePanel();
        createCarsListPanel();
        createCarDetailsPanel();
        createReservationsPanel();
        createAdminPanel();

        // Add panels to main panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(carsListPanel, "cars");
        mainPanel.add(carDetailsPanel, "carDetails");
        mainPanel.add(reservationsPanel, "reservations");
        mainPanel.add(adminPanel, "admin");

        // Show login panel first
        cardLayout.show(mainPanel, "login");

        // Add main panel to frame
        add(mainPanel);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("RideOn - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register New Account");

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authController.login(username, password)) {
                    showDashboard();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("RideOn - Register New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(20);

        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        JTextField dobField = new JTextField(20);

        JButton registerButton = new JButton("Create Account");
        JButton backButton = new JButton("Back to Login");

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                String dob = dobField.getText();

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() ||
                        email.isEmpty() || address.isEmpty() || dob.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "All fields are required", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (authController.register(username, password, name, email, address, dob)) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Registration successful. Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Username or email already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(titleLabel, gbc);

        // Username
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(passwordField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(emailField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(addressField, gbc);

        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(dobLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(dobField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(registerButton, gbc);

        gbc.gridy = 8;
        registerPanel.add(backButton, gbc);
    }

    // Other UI creation methods would follow a similar pattern
    // For brevity, I'm only showing method signatures

    private void createDashboardPanel() {
        // Implementation would include main menu and dashboard elements
        dashboardPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        dashboardPanel.add(menuPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Welcome to RideOn Car Rental System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(welcomeLabel);

        dashboardPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void createProfilePanel() {
        // Implementation would include profile display and edit functionality
        profilePanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        profilePanel.add(menuPanel, BorderLayout.WEST);

        // Profile content would be added here
    }

    private void createCarsListPanel() {
        // Implementation would include car search, filter, and listing functionality
        carsListPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        carsListPanel.add(menuPanel, BorderLayout.WEST);

        // Cars list content would be added here
    }

    private void createCarDetailsPanel() {
        // Implementation would include detailed car info and reservation functionality
        carDetailsPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        carDetailsPanel.add(menuPanel, BorderLayout.WEST);

        // Car details content would be added here
    }

    private void createReservationsPanel() {
        // Implementation would include user's reservations list and management
        reservationsPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        reservationsPanel.add(menuPanel, BorderLayout.WEST);

        // Reservations content would be added here
    }

    private void createAdminPanel() {
        // Implementation would include admin functions like car and user management
        adminPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();
        adminPanel.add(menuPanel, BorderLayout.WEST);

        // Admin content would be added here
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(200, 600));
        menuPanel.setBorder(BorderFactory.createEtchedBorder());

        // Create menu buttons
        dashboardBtn = new JButton("Dashboard");
        profileBtn = new JButton("My Profile");
        carsBtn = new JButton("Our Cars");
        reservationsBtn = new JButton("My Reservations");
        adminBtn = new JButton("Admin Panel");
        logoutBtn = new JButton("Logout");

        // Add action listeners
        dashboardBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        profileBtn.addActionListener(e -> cardLayout.show(mainPanel, "profile"));
        carsBtn.addActionListener(e -> cardLayout.show(mainPanel, "cars"));
        reservationsBtn.addActionListener(e -> cardLayout.show(mainPanel, "reservations"));
        adminBtn.addActionListener(e -> cardLayout.show(mainPanel, "admin"));
        logoutBtn.addActionListener(e -> logout());

        // Add buttons to menu
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(dashboardBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(profileBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(carsBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(reservationsBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Only show admin button if user is admin
        if (authController.isAdmin()) {
            menuPanel.add(adminBtn);
            menuPanel.add(Box.createVerticalStrut(10));
        }

        menuPanel.add(logoutBtn);
        menuPanel.add(Box.createVerticalGlue());

        return menuPanel;
    }

    private void showDashboard() {
        // Update and show the dashboard
        cardLayout.show(mainPanel, "dashboard");
    }

    private void logout() {
        authController.logout();
        cardLayout.show(mainPanel, "login");
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
