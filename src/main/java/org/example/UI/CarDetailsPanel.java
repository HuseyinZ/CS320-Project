package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class CarDetailsPanel extends JPanel {
    public CarDetailsPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Car ID:", "Brand:", "Model:", "Year:", "Color:", "License Plate:", "Kilometer:", "Fuel:", "Price/Day:", "Chassis No:"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField(20);
            if (i == 0) fields[i].setEditable(false);
            gbc.gridx = 0; gbc.gridy = i;
            content.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            content.add(fields[i], gbc);
        }
        JButton save = new JButton("Save");
        JButton back = new JButton("Back");
        JPanel bp = new JPanel(); bp.add(save); bp.add(back);
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        content.add(bp, gbc);

        add(content, BorderLayout.CENTER);
    }
}