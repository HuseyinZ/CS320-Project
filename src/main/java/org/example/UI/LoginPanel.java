package org.example.UI;

import org.example.Controller.AuthController;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private Runnable loginSuccessListener;
    private final AuthController authController;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPanel(AuthController authController, CardLayout cardLayout, JPanel mainPanel) {
        this.authController = authController;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("RideOn - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register New Account");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        gbc.gridy = 4;
        add(registerButton, gbc);
    }

    public void setLoginSuccessListener(Runnable listener) {
        this.loginSuccessListener = listener;
    }

    private void notifyLoginSuccess() {
        if (loginSuccessListener != null) {
            loginSuccessListener.run();
        }
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authController.login(username, password)) {
            notifyLoginSuccess();
            usernameField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
