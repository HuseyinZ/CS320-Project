package org.example.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarsListPanel extends JPanel {
    public CarsListPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Available Cars");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Brand", "Model", "Year", "Plate", "Price/Day"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel();
        btns.add(new JButton("View Details"));
        btns.add(new JButton("Refresh List"));
        content.add(btns, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }
}
