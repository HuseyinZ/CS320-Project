package org.example.UI;

import org.example.Controller.AuthController;
import org.example.Controller.ReservationController;
import org.example.Model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {
    public DashboardPanel(JPanel menuPanel, AuthController authController, ReservationController reservationController) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10); // Kenar boşlukları

        // Welcome label
        gbc.gridy = 0;
        JLabel welcomeLabel = new JLabel("Welcome to RideOn Car Rental System: " + authController.getCurrentUser().getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(welcomeLabel, gbc);

        // Kullanıcının rezervasyonlarını al
        int total = 0;
        java.util.List<Reservation> reservations = reservationController.getUserReservations();
        for (Reservation reservation : reservations) {
            if (reservation.getStartDate().isAfter(LocalDate.now())) {
                total += 1;
            }
        }

        if (total == 0) {
            gbc.gridy++;
            JLabel noReservationsLabel = new JLabel("No upcoming reservations.");
            noReservationsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            contentPanel.add(noReservationsLabel, gbc);
        } else {
            gbc.gridy++;
            JLabel reservationsLabel = new JLabel("Upcoming Reservations:");
            reservationsLabel.setFont(new Font("Arial", Font.BOLD, 18));
            contentPanel.add(reservationsLabel, gbc);

            for (Reservation reservation : reservations) {
                if (!reservation.getStartDate().isAfter(LocalDate.now())) {
                    continue; // Skip past reservations
                }
                gbc.gridy++;
                JLabel reservationDetails = new JLabel(
                        "Car ID: " + reservation.getCarId() +
                                ", Start: " + reservation.getStartDate() +
                                ", End: " + reservation.getEndDate()
                );
                reservationDetails.setFont(new Font("Arial", Font.PLAIN, 16));
                contentPanel.add(reservationDetails, gbc);
            }
        }

        add(contentPanel, BorderLayout.CENTER);
    }
}