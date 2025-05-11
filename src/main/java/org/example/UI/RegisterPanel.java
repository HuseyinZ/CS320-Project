package org.example.UI;

import org.example.Controller.AuthController;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private AuthController authController;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField dobField;
    private JTextField addressField;

    public RegisterPanel(AuthController authController, CardLayout cardLayout, JPanel mainPanel) {
        super(new GridBagLayout());
        this.authController = authController;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        int y = 1;

        // Username
        addLabelAndField("Username:", usernameField = new JTextField(20), gbc, y++);

        // Password
        addLabelAndField("Password:", passwordField = new JPasswordField(20), gbc, y++);

        // Confirm Password
        addLabelAndField("Confirm Password:", confirmPasswordField = new JPasswordField(20), gbc, y++);

        // Full Name
        addLabelAndField("Full Name:", fullNameField = new JTextField(20), gbc, y++);

        // Email
        addLabelAndField("Email:", emailField = new JTextField(20), gbc, y++);

        // Date of Birth
        addLabelAndField("Date of Birth:", dobField = new JTextField(20), gbc, y++);

        // Address
        addLabelAndField("Address:", addressField = new JTextField(20), gbc, y++);

        // Buttons
        JButton registerBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Back to Login");
        JPanel btnPanel = new JPanel();
        btnPanel.add(registerBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        add(btnPanel, gbc);

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
                boolean success = authController.register(username, password, fullName, email, address, dateOfBirth);
                if (success) {
                    usernameField.setText("");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    fullNameField.setText("");
                    emailField.setText("");
                    addressField.setText("");
                    dobField.setText("");
                    JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
    }

    private void addLabelAndField(String labelText, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }
}
