package org.example.Controller;

import org.example.Model.Car;
import org.example.Model.CarFilterRequest;
import org.example.Service.CarService;

import java.time.LocalDate;
import java.util.List;

public class CarController {
    CarService carService;

    public CarController() {
        carService = new CarService();
    }

    public List<Car> getAllCars() {
        return carService.getAllCars();
    }
    public List<Car> getAvailableCars() {
        return carService.getAvailableCars();
    }

    public Car getCarDetails(int carId) {
        return carService.getCarById(carId);
    }

    public List<Car> searchCars(String keyword) {
        return carService.searchCars(keyword);
    }

    public List<Car> filterCars(Integer year, String brand, String model, String color, Double minPrice, Double maxPrice, String fuel, Integer maxKilometer, String transmission, LocalDate startDate, LocalDate endDate) {
        return carService.filterCars(year, brand, model, color, minPrice, maxPrice, fuel, maxKilometer, transmission, startDate, endDate);
    }

    public List<Car> filterCars(CarFilterRequest request) {
        return carService.filterCars(
                request.year, request.brand, request.model, request.color, request.minPrice, request.maxPrice,
                request.fuel, request.maxKilometer, request.transmission, request.startDate, request.endDate
        );
    }

    public boolean addCar(String brand, String model, int year, String color, double pricePerDay, String license_plate, Integer kilometer, String chassis, String fuel, String transmission) {
        return carService.addCar(brand, model, year, color, pricePerDay, license_plate, kilometer, chassis, fuel, transmission);
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
