package org.example.UI;

import org.example.Controller.AuthController;
import org.example.Model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel(JPanel menuPanel, AuthController authController) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Welcome to RideOn Car Rental System " + authController.getCurrentUser().getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(welcomeLabel);

        add(contentPanel, BorderLayout.CENTER);
    }
}