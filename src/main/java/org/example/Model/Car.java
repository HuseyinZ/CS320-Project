package org.example.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Car {
    private int carId;
    private String brand;
    private String model;
    private int year;
    private String color;
    private String licence_plate;
    private int kilometer;
    private String fuel;
    private double pricePerDay;
    private String chassis;
    private ArrayList<LocalDate> occupiedDates;
    private String transmission;

    // Constructors
    public Car() {}

    public Car(int carId, String brand, String model, int year, String color, double pricePerDay, String licence_plate, int kilometer, String fuel, String chassis, ArrayList<LocalDate> occupiedDates, String transmission) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.pricePerDay = pricePerDay;
        this.kilometer = kilometer;
        this.fuel = fuel;
        this.licence_plate = licence_plate;
        this.chassis = chassis;
        this.occupiedDates = occupiedDates;
        this.transmission = transmission;
    }

    // Getters and setters
    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    public String getLicence_plate() { return licence_plate; }
    public void setLicence_plate(String licence_plate){
        this.licence_plate = licence_plate;
    }

    public int getKilometer() { return kilometer; }
    public void setKilometer(int kilometer) {this.kilometer = kilometer; }

    public String getFuel() { return fuel; }
    public void setFuel(String fuel) { this.fuel = fuel; }

    public String getChassis() { return chassis; }
    public void setChassis(String chassis) { this.chassis = chassis; }

    public ArrayList<LocalDate> getOccupiedDates() { return occupiedDates; }
    public void setOccupiedDates(ArrayList<LocalDate> occupiedDates) {
        this.occupiedDates = occupiedDates;
    }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    @Override
    public String toString() {
        return year + " " + brand + " " + model + " (" + color + ")";
    }

    public boolean isAvailable(LocalDate of) {
        return occupiedDates.contains(of);
    }
}