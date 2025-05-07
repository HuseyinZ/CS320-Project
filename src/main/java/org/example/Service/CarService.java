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

    public List<Car> filterCars(Integer year, String brand, String model, String color, Double minPrice, Double maxPrice, String fuel, Integer maxKilometer) {
        return carDAO.filterCars(year, brand, model, color, minPrice, maxPrice, fuel, maxKilometer);
    }

    public boolean addCar(String brand, String model, int year, String color, double pricePerDay) {
        Car newCar = new Car();
        newCar.setBrand(brand);
        newCar.setModel(model);
        newCar.setYear(year);
        newCar.setColor(color);
        newCar.setPricePerDay(pricePerDay);
        newCar.setOccupiedDates(new ArrayList<LocalDate>());

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
