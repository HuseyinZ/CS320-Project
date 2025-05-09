package org.example.Controller;

import org.example.Model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarControllerTest {

    private CarController carController;

    @BeforeEach
    public void setUp() {
        carController = new CarController();
    }

    /**
     * Test for T-SRS-RO-011: Check if the "Our Cars" button in menu works correctly
     * This test verifies that the getAllCars method returns a non-empty list
     */
    @Test
    public void testGetAllCars() {
        List<Car> cars = carController.getAllCars();

        assertNotNull(cars, "The car list should not be null");
        assertFalse(cars.isEmpty(), "The car list should not be empty");
    }

    /**
     * Test for T-SRS-RO-002: Verify that the RO search bar is in operational condition
     */
    @Test
    public void testSearchCars_PositiveResult() {

        List<Car> cars = carController.searchCars("Toyota");

        assertNotNull(cars, "The search result should not be null");
        assertFalse(cars.isEmpty(), "The search should return at least one car");


        for (Car car : cars) {
            assertTrue(
                    car.getBrand().toLowerCase().contains("toyota") ||
                            car.getModel().toLowerCase().contains("toyota") ||
                            car.getColor().toLowerCase().contains("toyota"),
                    "Returned car should match the search term"
            );
        }
    }

    /**
     * Test for T-SRS-RO-002: Verify that the RO search bar is in operational condition (negative result)
     */
    @Test
    public void testSearchCars_NegativeResult() {

        List<Car> cars = carController.searchCars("NonExistentBrandXYZ123");

        assertNotNull(cars, "The search result should not be null even for no matches");
        assertTrue(cars.isEmpty(), "The search should return no cars for a non-existent brand");
    }

    /**
     * Test for T-SRS-RO-004: Verify that the user can filter the list of cars
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely
     */
    @Test
    public void testFilterCars_ByColor() {

        String colorToFilter = "Red";
        List<Car> cars = carController.filterCars(null, null, null, colorToFilter, null, null, null, null);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertEquals(colorToFilter, car.getColor(), "All filtered cars should have the specified color");
        }
    }

    /**
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely - by year
     */
    @Test
    public void testFilterCars_ByYear() {

        int yearToFilter = 2022;
        List<Car> cars = carController.filterCars(yearToFilter, null, null, null, null, null, null, null);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertEquals(yearToFilter, car.getYear(), "All filtered cars should be from the specified year");
        }
    }

    /**
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely - by brand
     */
    @Test
    public void testFilterCars_ByBrand() {

        String brandToFilter = "Toyota";
        List<Car> cars = carController.filterCars(null, brandToFilter, null, null, null, null, null, null);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertEquals(brandToFilter, car.getBrand(), "All filtered cars should have the specified brand");
        }
    }

    /**
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely - by price range
     */
    @Test
    public void testFilterCars_ByPriceRange() {

        double minPrice = 40.0;
        double maxPrice = 60.0;
        List<Car> cars = carController.filterCars(null, null, null, null, minPrice, maxPrice, null, null);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertTrue(
                    car.getPricePerDay() >= minPrice && car.getPricePerDay() <= maxPrice,
                    "All filtered cars should be within the specified price range"
            );
        }
    }

    /**
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely - by fuel
     */
    @Test
    public void testFilterCars_ByFuel() {

        String fuelToFilter = "electric";
        List<Car> cars = carController.filterCars(null, null, null, null, null, null, fuelToFilter, null);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertEquals(fuelToFilter, car.getFuel(), "All filtered cars should have the specified fuel type");
        }
    }

    /**
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely - by fuel
     */
    @Test
    public void testFilterCars_ByMaxKilometer() {

        int maxKilometerToFilter = 10000;
        List<Car> cars = carController.filterCars(null, null, null, null, null, null, null, maxKilometerToFilter);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertEquals(maxKilometerToFilter, car.getKilometer(), "All filtered cars should have the specified fuel type");
        }
    }

    /**
     * Test for T-SRS-RO-004.1/2: Verify that the user can filter freely - multiple criteria
     */
    @Test
    public void testFilterCars_MultipleCriteria() {

        String colorToFilter = "Red";
        String brandToFilter = "Toyota";
        List<Car> cars = carController.filterCars(null, brandToFilter, null, colorToFilter, null, null, null, null);

        assertNotNull(cars, "The filter result should not be null");

        for (Car car : cars) {
            assertEquals(colorToFilter, car.getColor(), "All filtered cars should have the specified color");
            assertEquals(brandToFilter, car.getBrand(), "All filtered cars should have the specified brand");
        }
    }

    /**
     * Test for car addition functionality
     */
    @Test
    public void testAddCar() {

        boolean result = carController.addCar("Tesla", "Model 3", 2023, "White", 75.0);

        assertTrue(result, "Adding a new car should succeed");
        List<Car> cars = carController.searchCars("Tesla Model 3");
        assertFalse(cars.isEmpty(), "The newly added car should be found in a search");
    }

    /**
     * Test for car update functionality
     */
    @Test
    public void testUpdateCar() {

        int carId = 1;
        String newColor = "Black";
        double newPrice = 65.0;

        Car carBeforeUpdate = carController.getCarDetails(carId);
        assertNotNull(carBeforeUpdate, "Car with id " + carId + " should exist");

        boolean result = carController.updateCar(
                carId,
                carBeforeUpdate.getBrand(),
                carBeforeUpdate.getModel(),
                carBeforeUpdate.getYear(),
                newColor,
                newPrice
        );

        assertTrue(result, "Updating an existing car should succeed");

        Car carAfterUpdate = carController.getCarDetails(carId);
        assertEquals(newColor, carAfterUpdate.getColor(), "The car color should be updated");
        assertEquals(newPrice, carAfterUpdate.getPricePerDay(), "The car price should be updated");
    }

    /**
     * Test for T-SRS-RO-003.1: RO makes sure that the car the user intended to reserve is not reserved by another user
     */
    @Test
    public void testUpdateCarAvailability() {
        int carId = 1;

        Car carBeforeUpdate = carController.getCarDetails(carId);
        assertNotNull(carBeforeUpdate, "Car with id " + carId + " should exist");

        boolean result = carController.updateCarAvailability(carId, "maintenance", LocalDate.of(2025, 5 ,15), LocalDate.of(2025, 5 ,17));
        assertTrue(result, "Setting a car as unavailable should succeed");

        Car carAfterUpdate = carController.getCarDetails(carId);
        assertFalse(carAfterUpdate.isAvailable(LocalDate.of(2025, 5, 16)), "The car should be marked as unavailable");

        result = carController.updateCarAvailability(carId, "maintenance", LocalDate.of(2025, 5 ,15), LocalDate.of(2025, 5 ,17));
        assertTrue(result, "Setting a car as available should succeed");

        carAfterUpdate = carController.getCarDetails(carId);
        assertTrue(carAfterUpdate.isAvailable(LocalDate.of(2025, 5, 16)), "The car should be marked as available");
    }

    /**
     * Test for car deletion functionality
     */
    @Test
    public void testDeleteCar() {
        // First, add a car to delete
        carController.addCar("Ford", "Focus", 2021, "Green", 40.0);

        // Get all cars and find the id of the newly added car
        List<Car> allCars = carController.searchCars("Ford Focus");
        assertFalse(allCars.isEmpty(), "The newly added car should be found");

        int carIdToDelete = allCars.getFirst().getCarId();

        // Delete the car
        boolean result = carController.deleteCar(carIdToDelete);
        assertTrue(result, "Deleting a car should succeed");

        // Verify the car was deleted
        Car deletedCar = carController.getCarDetails(carIdToDelete);
        assertNull(deletedCar, "The deleted car should not be found");
    }
}