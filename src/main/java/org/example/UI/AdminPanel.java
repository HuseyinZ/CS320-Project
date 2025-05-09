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

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new JButton("Add Car"), gbc);
        gbc.gridy = 1;
        content.add(new JButton("Remove Car"), gbc);
        gbc.gridy = 2;
        content.add(new JButton("View Users"), gbc);
        gbc.gridy = 3;
        content.add(new JButton("Manage Reservations"), gbc);

        add(content, BorderLayout.CENTER);
    }
}