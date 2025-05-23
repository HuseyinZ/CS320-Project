package org.example.Controller;

import org.example.Model.Reservation;
import org.example.Service.ReservationService;

import java.time.LocalDate;
import java.util.List;

public class ReservationController {
    private ReservationService reservationService;

    public ReservationController() {
        reservationService = new ReservationService();
    }

    public List<Reservation> getUserReservations() {
        return reservationService.getUserReservations();
    }

    public boolean createReservation(int carId, LocalDate startTime, LocalDate endTime) {
        return reservationService.createReservation(carId, startTime, endTime);
    }

    public boolean cancelReservation(int reservationId) {
        return reservationService.cancelReservation(reservationId);
    }

    public boolean modifyReservation(int reservationId, int carId, LocalDate startTime, LocalDate endTime) {
        return reservationService.modifyReservation(reservationId, carId, startTime, endTime);
    }

    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
}
