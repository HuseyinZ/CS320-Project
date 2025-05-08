package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Welcome to RideOn Car Rental System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(welcomeLabel);

        add(contentPanel, BorderLayout.CENTER);
    }
}
