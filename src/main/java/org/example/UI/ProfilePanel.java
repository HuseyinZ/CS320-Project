package org.example.UI;

import org.example.DAO.UserDAO;
import org.example.Model.User;
import org.example.Service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePanel extends JPanel {
    public ProfilePanel(JPanel menuPanel, User user, UserService userService) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);

        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField(20);
        userIdField.setText(String.valueOf(user.getUserId()));
        userIdField.setEditable(false);

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(20);
        nameField.setText(user.getName());

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        emailField.setText(user.getEmail());

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(40);
        addressField.setText(user.getAddress());


        gbc.gridx = 0; gbc.gridy = 0;
        content.add(usernameLabel, gbc);
        gbc.gridx = 1;
        content.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(userIdLabel, gbc);
        gbc.gridx = 1;
        content.add(userIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(nameLabel, gbc);
        gbc.gridx = 1;
        content.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        content.add(emailLabel, gbc);
        gbc.gridx = 1;
        content.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        content.add(addressLabel, gbc);
        gbc.gridx = 1;
        content.add(addressField, gbc);

        JButton saveBtn = new JButton("Save Profile");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.setName(nameField.getText());
                user.setEmail(emailField.getText());
                user.setAddress(addressField.getText());
                userService.updateProfile(user.getName(), user.getEmail(), user.getAddress());

                JOptionPane.showMessageDialog(null, "User Info Updated", "Update", JOptionPane.PLAIN_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(saveBtn, gbc);

        add(content, BorderLayout.CENTER);
    }
}
