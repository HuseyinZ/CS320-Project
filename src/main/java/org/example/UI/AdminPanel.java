package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    public AdminPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addCarButton = new JButton("Add Car");
        JButton removeCarButton = new JButton("Remove Car");
        JButton viewUsersButton = new JButton("View Users");
        JButton manageReservationsButton = new JButton("Manage Reservations");


        gbc.gridx = 0; gbc.gridy = 0;
        content.add(addCarButton, gbc);
        gbc.gridy = 1;
        content.add(removeCarButton, gbc);
        gbc.gridy = 2;
        content.add(viewUsersButton, gbc);
        gbc.gridy = 3;
        content.add(manageReservationsButton, gbc);

        addCarButton.addActionListener(e -> handleAddCar());
        removeCarButton.addActionListener(e -> handleRemoveCar());
        viewUsersButton.addActionListener(e -> handleViewUsers());
        manageReservationsButton.addActionListener(e -> handleManageReservations());


        add(content, BorderLayout.CENTER);
    }
    private void handleAddCar() {
        // Logic to add a car
        JOptionPane.showMessageDialog(this, "Add Car functionality not implemented yet.");
    }
    private void handleRemoveCar() {
        // Logic to remove a car
        JOptionPane.showMessageDialog(this, "Remove Car functionality not implemented yet.");
    }
    private void handleViewUsers() {
        // Logic to view users
        JOptionPane.showMessageDialog(this, "View Users functionality not implemented yet.");
    }
    private void handleManageReservations() {
        // Logic to manage reservations
        JOptionPane.showMessageDialog(this, "Manage Reservations functionality not implemented yet.");
    }

}