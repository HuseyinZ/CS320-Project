package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    public ProfilePanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setEditable(false);
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(usernameLabel, gbc);
        gbc.gridx = 1; content.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; content.add(nameLabel, gbc);
        gbc.gridx = 1; content.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; content.add(emailLabel, gbc);
        gbc.gridx = 1; content.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; content.add(phoneLabel, gbc);
        gbc.gridx = 1; content.add(phoneField, gbc);

        JButton saveBtn = new JButton("Save Profile");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        content.add(saveBtn, gbc);

        add(content, BorderLayout.CENTER);
    }
}
