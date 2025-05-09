package org.example.Controller;

import org.example.Model.Car;
import org.example.Model.Reservation;
import org.example.Service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarController {
    CarService carService;

    public CarController() {
        carService = new CarService();
    }

    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    public Car getCarDetails(int carId) {
        return carService.getCarById(carId);
    }

    public List<Car> searchCars(String keyword) {
        return carService.searchCars(keyword);
    }

    public List<Car> filterCars(Integer year, String brand, String model, String color, Double minPrice, Double maxPrice, String fuel, Integer maxKilometer) {
        return carService.filterCars(year, brand, model, color, minPrice, maxPrice, fuel, maxKilometer);
    }

    public boolean addCar(String brand, String model, int year, String color, double pricePerDay) {
        return carService.addCar(brand, model, year, color, pricePerDay);
    }

    public boolean updateCar(int carId, String brand, String model, int year, String color, double pricePerDay) {
        return carService.updateCar(carId, brand, model, year, color, pricePerDay);
    }

    public boolean deleteCar(int carId) {
        return carService.deleteCar(carId);
    }

    public boolean updateCarAvailability(int carId, String status, LocalDate startDate, LocalDate endDate) {
        return carService.updateCarAvailability(carId, status, startDate, endDate);
    }

    public static class ReservationControllerTest {

        private ReservationController reservationController;
        private AuthController authController;
        private CarController carController;

        @BeforeEach
        public void setUp() {
            reservationController = new ReservationController();
            authController = new AuthController();
            carController = new CarController();

            // Login as a test user for most tests
            authController.login("testuser", "password123");
        }

        /**
         * Test for T-SRS-RO-003: Verify that the RO reservation functionality is operational
         */
        @Test
        public void testCreateReservation_Success() {
            // Find an available car
            List<Car> availableCars = carController.getAllCars();
            LocalDate testDate = LocalDate.of(2025, 3, 15);
            assertTrue(availableCars.stream().anyMatch(car -> car.isAvailable(testDate)),
                    "At least one car should be available for reservation");

            // Get the first available car
            int carId = availableCars.stream()
                    .filter(car -> car.isAvailable(testDate))
                    .findFirst()
                    .get()
                    .getCarId();

            // Create reservation for tomorrow
            LocalDate startTime = LocalDate.now().plusDays(1);
            LocalDate endTime = startTime.plusDays(1);

            boolean result = reservationController.createReservation(carId, startTime, endTime);
            assertTrue(result, "Creating a reservation for an available car should succeed");
        }

        /**
         * Test for T-SRS-RO-003: Verify reservation for unavailable car fails
         */
        @Test
        public void testCreateReservation_UnavailableCar() {
            // Find a car and make it unavailable
            List<Car> allCars = carController.getAllCars();
            assertFalse(allCars.isEmpty(), "There should be cars in the database");

            LocalDate testDateStart = LocalDate.of(2025, 3, 15);
            LocalDate testDateEnd = LocalDate.now().plusDays(4);

            int carId = allCars.getFirst().getCarId();
            carController.updateCarAvailability(carId, "unavaliable", testDateStart, testDateEnd);

            boolean result = reservationController.createReservation(carId, testDateStart, testDateStart.plusDays(1));
            assertFalse(result, "Creating a reservation for an unavailable car should fail");
        }

        /**
         * Test for T-SRS-RO-003.3: RO must prevent the same user from reserving multiple cars simultaneously
         */
        @Test
        public void testCreateMultipleReservations_SameTimeSlot() {
            // Find two available cars
            List<Car> availableCars = carController.getAllCars().stream()
                    .filter(car -> car.isAvailable(LocalDate.now()))
                    .toList();

            assertTrue(availableCars.size() >= 2,
                    "At least two cars should be available for this test");

            int carId1 = availableCars.get(0).getCarId();
            int carId2 = availableCars.get(1).getCarId();

            // Create reservation for the first car
            LocalDate startTime = LocalDate.now().plusDays(1);
            LocalDate endTime = startTime.plusDays(1);

            boolean result1 = reservationController.createReservation(carId1, startTime, endTime);
            assertTrue(result1, "First reservation should succeed");

            // Try to create reservation for the second car in the same time slot
            boolean result2 = reservationController.createReservation(carId2, startTime, endTime);
            assertFalse(result2, "Second reservation for the same time slot should fail");
        }

        /**
         * Test for T-SRS-RO-009: RO shall enable users to view their existing reservations
         */
        @Test
        public void testGetUserReservations() {
            // First create a reservation
            // Find an available car
            List<Car> availableCars = carController.getAllCars().stream()
                    .filter(car -> car.isAvailable(LocalDate.now()))
                    .toList();

            if (!availableCars.isEmpty()) {
                int carId = availableCars.getFirst().getCarId();
                LocalDate startTime = LocalDate.now().plusDays(1);
                LocalDate endTime = startTime.plusDays(24);
                reservationController.createReservation(carId, startTime, endTime);
            }

            // Now get user reservations
            List<Reservation> userReservations = reservationController.getUserReservations();
            assertNotNull(userReservations, "User reservations list should not be null");
        }

        /**
         * Test for T-SRS-RO-010: RO shall enable users to cancel existing reservations
         */
        @Test
        public void testCancelReservation() {
            // First create a reservation
            List<Car> availableCars = carController.getAllCars().stream()
                    .filter(car -> car.isAvailable(LocalDate.now()))
                    .toList();

            if (availableCars.isEmpty()) {
                fail("No available cars to create reservation for cancellation test");
            }

            int carId = availableCars.getFirst().getCarId();
            LocalDate startTime = LocalDate.now().plusDays(1);
            LocalDate endTime = startTime.plusDays(24);
            reservationController.createReservation(carId, startTime, endTime);

            // Get the reservation id
            List<Reservation> userReservations = reservationController.getUserReservations();
            assertFalse(userReservations.isEmpty(), "User should have at least one reservation");

            int reservationId = userReservations.getFirst().getReservationId();

            // Cancel the reservation
            boolean result = reservationController.cancelReservation(reservationId);
            assertTrue(result, "Cancelling a reservation should succeed");

            // Verify the reservation is cancelled
            List<Reservation> updatedReservations = reservationController.getUserReservations();
            for (Reservation res : updatedReservations) {
                if (res.getReservationId() == reservationId) {
                    assertTrue(res.isCancelled(), "The reservation should be marked as cancelled");
                }
            }
        }

        /**
         * Test for T-SRS-RO-010: RO shall enable users to modify existing reservations
         */
        @Test
        public void testModifyReservation() {
            // First create a reservation
            List<Car> availableCars = carController.getAllCars().stream()
                    .filter(car -> car.isAvailable(LocalDate.now()))
                    .toList();

            if (availableCars.isEmpty()) {
                fail("No available cars to create reservation for modification test");
            }

            int carId = availableCars.getFirst().getCarId();
            LocalDate startTime = LocalDate.now().plusDays(1);
            LocalDate endTime = startTime.plusDays(24);
            reservationController.createReservation(carId, startTime, endTime);

            // Get the reservation id
            List<Reservation> userReservations = reservationController.getUserReservations();
            assertFalse(userReservations.isEmpty(), "User should have at least one reservation");

            int reservationId = userReservations.getFirst().getReservationId();

            // Modify the reservation
            LocalDate newStartTime = LocalDate.now().plusDays(2);
            LocalDate newEndTime = newStartTime.plusDays(24);

            boolean result = reservationController.modifyReservation(reservationId, carId, newStartTime, newEndTime);
            assertTrue(result, "Modifying a reservation should succeed");

            // Verify the reservation is modified
            List<Reservation> updatedReservations = reservationController.getUserReservations();
            boolean foundModified = false;
            for (Reservation res : updatedReservations) {
                if (res.getReservationId() == reservationId) {
                    assertEquals(newStartTime, res.getStartDate(), "Start time should be updated");
                    assertEquals(newEndTime, res.getEndDate(), "End time should be updated");
                    foundModified = true;
                    break;
                }
            }
            assertTrue(foundModified, "The modified reservation should be found");
        }

        /**
         * Test for T-SRS-RO-003.4: RO stores all reservations in the database
         */
        @Test
        public void testAllReservationsStored() {
            // Get all reservations as admin
            authController.logout();
            authController.login("adminuser", "adminpass");

            List<Reservation> allReservations = reservationController.getAllReservations();
            assertNotNull(allReservations, "All reservations list should not be null");

            // Create a new reservation
            List<Car> availableCars = carController.getAllCars().stream()
                    .filter(car -> car.isAvailable(LocalDate.now()))
                    .toList();

            if (!availableCars.isEmpty()) {
                int carId = availableCars.getFirst().getCarId();
                LocalDate startTime = LocalDate.now().plusDays(1);
                LocalDate endTime = startTime.plusDays(24);
                reservationController.createReservation(carId, startTime, endTime);

                // Get all reservations again
                List<Reservation> updatedReservations = reservationController.getAllReservations();
                assertEquals(allReservations.size() + 1, updatedReservations.size(),
                        "A new reservation should be added to the database");
            }
        }

        /**
         * Test for T-SRS-RO-006: Admin can change reservation availability
         */
        @Test
        public void testAdminUpdateReservation() {
            // Login as admin
            authController.logout();
            authController.login("adminuser", "adminpass");

            // Assuming there is at least one reservation in the system
            List<Reservation> allReservations = reservationController.getAllReservations();
            if (allReservations.isEmpty()) {
                // Create a reservation if none exists
                List<Car> availableCars = carController.getAllCars().stream()
                        .filter(car -> car.isAvailable(LocalDate.now()))
                        .toList();

                if (!availableCars.isEmpty()) {
                    int carId = availableCars.getFirst().getCarId();
                    LocalDate startTime = LocalDate.now().plusDays(1);
                    LocalDate endTime = startTime.plusDays(24);
                    reservationController.createReservation(carId, startTime, endTime);
                    allReservations = reservationController.getAllReservations();
                }
            }

            assertFalse(allReservations.isEmpty(), "There should be at least one reservation");

            // Modify a reservation (this is an admin function as described in T-SRS-RO-006)
            Reservation reservation = allReservations.getFirst();
            int reservationId = reservation.getReservationId();
            int carId = reservation.getCarId();

            // Change the end time of the reservation
            LocalDate newEndTime = reservation.getEndDate().plusDays(2);
            boolean result = reservationController.modifyReservation(
                    reservationId, carId, reservation.getStartDate(), newEndTime
            );

            assertTrue(result, "Admin should be able to modify reservations");
        }

        /**
         * Test for T-SRS-RO-007: Admin can delete a reservation
         */
        @Test
        public void testAdminDeleteReservation() {
            // Login as admin
            authController.logout();
            authController.login("adminuser", "adminpass");

            // Create a reservation to delete
            List<Car> availableCars = carController.getAllCars().stream()
                    .filter(car -> car.isAvailable(LocalDate.now()))
                    .toList();

            if (availableCars.isEmpty()) {
                fail("No available cars to create reservation for deletion test");
            }

            int carId = availableCars.getFirst().getCarId();
            LocalDate startTime = LocalDate.now().plusDays(1);
            LocalDate endTime = startTime.plusDays(24);
            reservationController.createReservation(carId, startTime, endTime);

            // Get the newly created reservation
            List<Reservation> allReservations = reservationController.getAllReservations();
            Reservation createdReservation = null;
            for (Reservation res : allReservations) {
                if (res.getCarId() == carId && res.getStartDate().equals(startTime)) {
                    createdReservation = res;
                    break;
                }
            }

            assertNotNull(createdReservation, "The created reservation should be found");

            // Cancel (delete) the reservation
            boolean result = reservationController.cancelReservation(createdReservation.getReservationId());
            assertTrue(result, "Admin should be able to cancel reservations");

            // Verify the reservation is cancelled
            List<Reservation> updatedReservations = reservationController.getAllReservations();
            boolean foundCancelled = false;
            for (Reservation res : updatedReservations) {
                if (res.getReservationId() == createdReservation.getReservationId()) {
                    assertTrue(res.isCancelled(), "The reservation should be marked as cancelled");
                    foundCancelled = true;
                    break;
                }
            }
            assertTrue(foundCancelled, "The cancelled reservation should still be in the database");
        }
    }
}
