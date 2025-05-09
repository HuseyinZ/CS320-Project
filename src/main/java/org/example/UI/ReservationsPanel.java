package org.example.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservationsPanel extends JPanel {
    public ReservationsPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout());
        JLabel title = new JLabel("My Reservations");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        String[] cols = {"Res ID", "Car ID", "Start Date", "End Date", "Status"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tm);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(5, 5, 5, 5);
        fgbc.anchor = GridBagConstraints.WEST;
        fgbc.gridx = 0; fgbc.gridy = 0;
        form.add(new JLabel("Car ID:"), fgbc);
        JTextField carIdField = new JTextField(10);
        fgbc.gridx = 1; form.add(carIdField, fgbc);
        fgbc.gridx = 0; fgbc.gridy = 1;
        form.add(new JLabel("Start (YYYY-MM-DD):"), fgbc);
        JTextField startField = new JTextField(10);
        fgbc.gridx = 1; form.add(startField, fgbc);
        fgbc.gridx = 0; fgbc.gridy = 2;
        form.add(new JLabel("End (YYYY-MM-DD):"), fgbc);
        JTextField endField = new JTextField(10);
        fgbc.gridx = 1; form.add(endField, fgbc);

        JButton reserve = new JButton("Reserve");
        fgbc.gridx = 0; fgbc.gridy = 3; fgbc.gridwidth = 2; fgbc.anchor = GridBagConstraints.CENTER;
        form.add(reserve, fgbc);

        content.add(form, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }
}
