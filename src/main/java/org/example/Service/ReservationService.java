package org.example.Service;


import org.example.DAO.ReservationDAO;
import org.example.Model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final UserService userService;

    public ReservationService() {
        reservationDAO = new ReservationDAO();
        userService = new UserService();
    }

    public List<Reservation> getUserReservations() {
        if (!userService.isLoggedIn()) {
            return null;
        }

        return reservationDAO.getReservationsByUserId(userService.getCurrentUser().getUserId());
    }

    public boolean createReservation(int carId, LocalDate startTime, LocalDate endTime) {
        if (!userService.isLoggedIn()) {
            return false;
        }

        Reservation newReservation = new Reservation();
        newReservation.setUserId(userService.getCurrentUser().getUserId());
        newReservation.setCarId(carId);
        newReservation.setStartDate(startTime);
        newReservation.setEndDate(endTime);
        newReservation.setCancelled(false);

        return reservationDAO.createReservation(newReservation);
    }

    public boolean cancelReservation(int reservationId) {
        if (!userService.isLoggedIn()) {
            return false;
        }

        // Check if the reservation belongs to the current user or user is admin
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }

        if (reservation.getUserId() != userService.getCurrentUser().getUserId() && !userService.isAdmin()) {
            return false;
        }

        return reservationDAO.cancelReservation(reservationId);
    }

    public boolean modifyReservation(int reservationId, int carId, LocalDate startTime, LocalDate endTime) {
        if (!userService.isLoggedIn()) {
            return false;
        }

        // Check if the reservation belongs to the current user or user is admin
        Reservation existingReservation = reservationDAO.getReservationById(reservationId);
        if (existingReservation == null) {
            return false;
        }

        if (existingReservation.getUserId() != userService.getCurrentUser().getUserId() && !userService.isAdmin()) {
            return false;
        }

        Reservation updatedReservation = new Reservation();
        updatedReservation.setReservationId(reservationId);
        updatedReservation.setUserId(existingReservation.getUserId());
        updatedReservation.setCarId(carId);
        updatedReservation.setStartDate(LocalDate.from(startTime));
        updatedReservation.setEndDate(LocalDate.from(endTime));
        updatedReservation.setCancelled(false);

        return reservationDAO.modifyReservation(updatedReservation);
    }

    public List<Reservation> getAllReservations() {
        if (!userService.isAdmin()) {
            return null;
        }

        return reservationDAO.getAllReservations();
    }

    public boolean deleteReservation(int reservationId) {
        if (!userService.isLoggedIn()) {
            return false;
        }
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null || (reservation.getUserId() != userService.getCurrentUser().getUserId() && !userService.isAdmin())) {
            return false;
        }

        return reservationDAO.deleteReservation(reservationId);
    }
}
