package org.example.Model;

import java.time.LocalDate;

public class CarFilterRequest {
    public Integer year;
    public String brand;
    public String model;
    public String color;
    public Double minPrice;
    public Double maxPrice;
    public String fuel;
    public Integer maxKilometer;
    public String transmission;
    public LocalDate startDate;
    public LocalDate endDate;

    public CarFilterRequest() {}
    public CarFilterRequest(Integer year, String brand, String model, String color, Double minPrice, Double maxPrice, String fuel, Integer maxKilometer, String transmission, LocalDate startDate, LocalDate endDate) {
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.fuel = fuel;
        this.maxKilometer = maxKilometer;
        this.transmission = transmission;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public Double getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    public Double getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    public String getFuel() {
        return fuel;
    }
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
    public Integer getMaxKilometer() {
        return maxKilometer;
    }
    public void setMaxKilometer(Integer maxKilometer) {
        this.maxKilometer = maxKilometer;
    }
    public String getTransmission() {
        return transmission;
    }
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}

