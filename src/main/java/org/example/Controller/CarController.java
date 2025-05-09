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

}
