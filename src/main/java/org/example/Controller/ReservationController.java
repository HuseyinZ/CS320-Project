package org.example.Controller;

import org.example.Model.Reservation;
import org.example.Service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationController {
    private ReservationService reservationService;

    public ReservationController() {
        reservationService = new ReservationService();
    }

    public List<Reservation> getUserReservations() {
        return reservationService.getUserReservations();
    }

    public boolean createReservation(int carId, LocalDateTime startTime, LocalDateTime endTime) {
        return reservationService.createReservation(carId, startTime, endTime);
    }

    public boolean cancelReservation(int reservationId) {
        return reservationService.cancelReservation(reservationId);
    }

    public boolean modifyReservation(int reservationId, int carId, LocalDateTime startTime, LocalDateTime endTime) {
        return reservationService.modifyReservation(reservationId, carId, startTime, endTime);
    }

    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
}
