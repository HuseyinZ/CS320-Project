package org.example.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import org.example.Controller.AuthController;
import org.example.Controller.CarController;
import org.example.Controller.ReservationController;

/**
 * Panel for user registration
 */
public class RegisterPanel extends JPanel {

    private AuthController authController;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // text fields for all the inputs
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField dobField;

    public RegisterPanel(AuthController authController, CardLayout cardLayout, JPanel mainPanel) {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Confirm Password:"), gbc);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Full Name:"), gbc);
        JTextField fullNameField = new JTextField(20);
        gbc.gridx = 1;
        add(fullNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Email:"), gbc);
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Address:"), gbc);
        addressField = new JTextField(20); // assign to class field
        gbc.gridx = 1;
        add(addressField, gbc);

        // Date of Birth
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        dobField = new JTextField(20); // assign to class field
        gbc.gridx = 1;
        add(dobField, gbc);

        // Buttons
        JButton registerBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Back to Login");
        JPanel btnPanel = new JPanel();
        btnPanel.add(registerBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnPanel, gbc);

        // Action Listeners
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String dateOfBirth = dobField.getText();
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean success = authController.register(username, password, fullName, email,address, dateOfBirth);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
    }
}