package org.example.DAO;

import org.example.Model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM ride_on.reservations WHERE user_id = ? AND is_cancelled = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setCarId(rs.getInt("car_id"));
                    reservation.setStartDate(LocalDate.from(rs.getTimestamp("start_date").toLocalDateTime()));
                    reservation.setEndDate(LocalDate.from(rs.getTimestamp("end_date").toLocalDateTime()));
                    reservation.setCancelled(rs.getBoolean("is_cancelled"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public boolean createReservation(Reservation reservation) {
        // First check if car is available for the requested time period
        if (!isCarAvailable(reservation.getCarId(), reservation.getStartDate(), reservation.getEndDate())) {
            return false;
        }

        // Also check if user already has an active reservation
        if (hasActiveReservation(reservation.getUserId())) {
            return false;
        }

        String query = "INSERT INTO ride_on.reservations (user_id, car_id, start_date, end_date, is_cancelled) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getCarId());
            stmt.setDate(3, Date.valueOf(reservation.getStartDate()));
            stmt.setDate(4, Date.valueOf(reservation.getEndDate()));
            stmt.setBoolean(5, reservation.isCancelled());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Update car availability
                new CarDAO().updateCarAvailability(reservation.getCarId(), "reserved", reservation.getStartDate(), reservation.getEndDate());
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelReservation(int reservationId) {
        String query = "UPDATE ride_on.reservations SET is_cancelled = true WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Get the car ID to update its availability
                Reservation reservation = getReservationById(reservationId);
                if (reservation != null) {
                    new CarDAO().deleteCarStatus(reservation.getCarId(), reservation.getStartDate(), reservation.getEndDate());
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifyReservation(Reservation oldReservation, Reservation newReservation) {
        // First cancel the existing reservation
        boolean cancelResult = cancelReservation(oldReservation.getReservationId());

        if (cancelResult) {
            // Then create a new one with updated details
            return createReservation(newReservation);
        }
        return false;
    }

    public Reservation getReservationById(int reservationId) {
        String query = "SELECT * FROM ride_on.reservations WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setCarId(rs.getInt("car_id"));
                    reservation.setStartDate(LocalDate.from(rs.getTimestamp("start_date").toLocalDateTime()));
                    reservation.setEndDate(LocalDate.from(rs.getTimestamp("end_date").toLocalDateTime()));
                    reservation.setCancelled(rs.getBoolean("is_cancelled"));
                    return reservation;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isCarAvailable(int carId, LocalDate startTime, LocalDate endTime) {
        String query = "SELECT COUNT(*) FROM ride_on.reservations " +
                "WHERE car_id = ? AND is_cancelled = false " +
                "AND ((start_date <= ? AND end_date >= ?) " +
                "OR (start_date <= ? AND end_date >= ?) " +
                "OR (start_date >= ? AND end_date <= ?))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);
            stmt.setTimestamp(2, Timestamp.valueOf(startTime.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(startTime.atStartOfDay()));
            stmt.setTimestamp(4, Timestamp.valueOf(endTime.atStartOfDay()));
            stmt.setTimestamp(5, Timestamp.valueOf(endTime.atStartOfDay()));
            stmt.setTimestamp(6, Timestamp.valueOf(startTime.atStartOfDay()));
            stmt.setTimestamp(7, Timestamp.valueOf(endTime.atStartOfDay()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasActiveReservation(int userId) {
        String query = "SELECT COUNT(*) FROM ride_on.reservations " +
                "WHERE user_id = ? AND is_cancelled = false " +
                "AND end_date > NOW()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM ride_on.reservations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("id"));
                reservation.setUserId(rs.getInt("user_id"));
                reservation.setCarId(rs.getInt("car_id"));
                reservation.setStartDate(LocalDate.from(rs.getTimestamp("start_date").toLocalDateTime()));
                reservation.setEndDate(LocalDate.from(rs.getTimestamp("end_date").toLocalDateTime()));
                reservation.setCancelled(rs.getBoolean("is_cancelled"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public boolean deleteReservation(int reservationId) {

        String query = "DELETE FROM ride_on.reservations WHERE id = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, reservationId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
