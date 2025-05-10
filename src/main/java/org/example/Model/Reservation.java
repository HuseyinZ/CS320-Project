package org.example.Model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private int userId;
    private int carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCancelled;

    // Constructors
    public Reservation() {}

    public Reservation(int reservationId, int userId, int carId, LocalDate startTime, LocalDate endTime) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startTime;
        this.endDate = endTime;
        this.isCancelled = false;
    }

    // Getters and setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean isCancelled) { this.isCancelled = isCancelled; }
}