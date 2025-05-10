package org.example.Service;

import org.example.DAO.CarDAO;
import org.example.Model.Car;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarService {
    private CarDAO carDAO;

    public CarService() {
        carDAO = new CarDAO();
    }

    public List<Car> getAllCars() {
        return carDAO.getAllCars();
    }

    public Car getCarById(int carId) {
        return carDAO.getCarById(carId);
    }

    public List<Car> searchCars(String keyword) {
        return carDAO.searchCars(keyword);
    }

    public List<Car> filterCars(Integer year, String brand, String model, String color, Double minPrice, Double maxPrice, String fuel, Integer maxKilometer, String transmission, LocalDate startDate, LocalDate endDate) {
        return carDAO.filterCars(year, brand, model, color, minPrice, maxPrice, fuel, maxKilometer, transmission, startDate, endDate);
    }

    public boolean addCar(String brand, String model, int year, String color, double pricePerDay, String license_plate, Integer kilometer, String chassis, String fuel, String transmission) {
        Car newCar = new Car();
        newCar.setBrand(brand);
        newCar.setModel(model);
        newCar.setYear(year);
        newCar.setColor(color);
        newCar.setPricePerDay(pricePerDay);
        newCar.setOccupiedDates(new ArrayList<LocalDate>());
        newCar.setLicence_plate(license_plate);
        newCar.setKilometer(kilometer);
        newCar.setChassis(chassis);
        newCar.setFuel(fuel);
        newCar.setTransmission(transmission);

        return carDAO.addCar(newCar);
    }

    public boolean updateCar(int carId, String brand, String model, int year, String color, double pricePerDay) {
        Car carToUpdate = carDAO.getCarById(carId);
        if (carToUpdate == null) {
            return false;
        }

        carToUpdate.setBrand(brand);
        carToUpdate.setModel(model);
        carToUpdate.setYear(year);
        carToUpdate.setColor(color);
        carToUpdate.setPricePerDay(pricePerDay);

        return carDAO.updateCar(carToUpdate);
    }

    public boolean deleteCar(int carId) {
        return carDAO.deleteCar(carId);
    }

    public boolean updateCarAvailability(int carId, String status, LocalDate startDate, LocalDate endDate) {
        return carDAO.updateCarAvailability(carId,status, startDate, endDate);
    }
}
