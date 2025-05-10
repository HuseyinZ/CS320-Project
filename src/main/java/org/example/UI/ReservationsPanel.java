package org.example.UI;

import org.example.Controller.ReservationController;
import org.example.Model.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReservationsPanel extends JPanel {
    ReservationController reservationController = new ReservationController();
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

        List<Reservation> reservations = reservationController.getUserReservations();
        for (Reservation reservation : reservations) {
            Object[] row = {reservation.getReservationId(), reservation.getCarId(), reservation.getStartDate(), reservation.getEndDate(), reservation.isCancelled() ? "Cancelled" : "Active"};
            tm.addRow(row);
        }


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

        reserve.addActionListener(e -> handleReservation(carIdField, startField, endField, tm));

        content.add(form, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }
    private void handleReservation(JTextField carIdField, JTextField startField, JTextField endField, DefaultTableModel tm) {
        String carId = carIdField.getText();
        String startDate = startField.getText();
        String endDate = endField.getText();

        // Validate the input fields
        if (carId.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // int carId, LocalDate startTime, LocalDate endTime
        int carIdInt = Integer.parseInt(carId);
        LocalDate startTime = LocalDate.parse(startDate);
        LocalDate endTime = LocalDate.parse(endDate);

        // Check if the reservation is valid
        if (!reservationController.createReservation(carIdInt, startTime, endTime)) {
            JOptionPane.showMessageDialog(this, "Invalid reservation dates or car ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // If the reservation is valid, add it to the table and get all of them
        List<Reservation> reservations = reservationController.getUserReservations();
        tm.setRowCount(0); // Clear the table
        for (Reservation reservation : reservations) {
            Object[] row = {reservation.getReservationId(), reservation.getCarId(), reservation.getStartDate(), reservation.getEndDate(), reservation.isCancelled() ? "Cancelled" : "Active"};
            tm.addRow(row);
        }
        // Show success message
        JOptionPane.showMessageDialog(this, "Reservation created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear the input fields
        carIdField.setText("");
        startField.setText("");
        endField.setText("");

    }
}