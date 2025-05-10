package org.example.DAO;

import org.example.Model.Car;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM ride_on.cars";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("id"));
                car.setBrand(rs.getString("brand"));
                car.setModel(rs.getString("model"));
                car.setYear(rs.getInt("production_year"));
                car.setColor(rs.getString("color"));
                car.setPricePerDay(rs.getDouble("price_per_day"));
                car.setChassis(rs.getString("chassis"));
                car.setKilometer(rs.getInt("kilometer"));
                car.setFuel(rs.getString("fuel"));
                car.setLicence_plate(rs.getString("license_plate"));
                car.setTransmission(rs.getString("transmission"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public Car getCarById(int carId) {
        String query = "SELECT * FROM ride_on.cars WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Car car = new Car();
                    car.setCarId(rs.getInt("car_id"));
                    car.setBrand(rs.getString("brand"));
                    car.setModel(rs.getString("model"));
                    car.setYear(rs.getInt("year"));
                    car.setColor(rs.getString("color"));
                    car.setPricePerDay(rs.getDouble("price_per_day"));
                    car.setChassis(rs.getString("chassis"));
                    car.setKilometer(rs.getInt("kilometer"));
                    car.setFuel(rs.getString("fuel"));
                    car.setLicence_plate(rs.getString("licence_plate"));
                    car.setTransmission(rs.getString("transmission"));
                    return car;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Car> searchCars(String keyword) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM ride_on.cars WHERE brand LIKE ? OR model LIKE ? OR color LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car car = new Car();
                    car.setCarId(rs.getInt("car_id"));
                    car.setBrand(rs.getString("brand"));
                    car.setModel(rs.getString("model"));
                    car.setYear(rs.getInt("year"));
                    car.setColor(rs.getString("color"));
                    car.setPricePerDay(rs.getDouble("price_per_day"));
                    car.setChassis(rs.getString("chassis"));
                    car.setKilometer(rs.getInt("kilometer"));
                    car.setFuel(rs.getString("fuel"));
                    car.setLicence_plate(rs.getString("licence_plate"));
                    car.setTransmission(rs.getString("transmission"));
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> filterCars(Integer year, String brand, String model, String color, Double minPrice, Double maxPrice, String fuel, Integer maxKilometer, String transmission) {
        List<Car> cars = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM cars WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (color != null && !color.isEmpty()) {
            queryBuilder.append(" AND color = ?");
            params.add(color);
        }

        if (year != null) {
            queryBuilder.append(" AND year = ?");
            params.add(year);
        }

        if (brand != null && !brand.isEmpty()) {
            queryBuilder.append(" AND brand = ?");
            params.add(brand);
        }

        if (model != null && !model.isEmpty()) {
            queryBuilder.append(" AND model = ?");
        }

        if (minPrice != null) {
            queryBuilder.append(" AND price_per_day >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            queryBuilder.append(" AND price_per_day <= ?");
            params.add(maxPrice);
        }

        if (fuel != null && !fuel.isEmpty()) {
            queryBuilder.append(" AND fuel = ?");
            params.add(fuel);
        }

        if (maxKilometer != null){
            queryBuilder.append(" AND max_kilometer <= ?");
        }

        if (transmission != null && !transmission.isEmpty()) {
            queryBuilder.append(" AND transmission = ?");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car car = new Car();
                    car.setCarId(rs.getInt("car_id"));
                    car.setBrand(rs.getString("brand"));
                    car.setModel(rs.getString("model"));
                    car.setYear(rs.getInt("year"));
                    car.setColor(rs.getString("color"));
                    car.setPricePerDay(rs.getDouble("price_per_day"));
                    car.setChassis(rs.getString("chassis"));
                    car.setKilometer(rs.getInt("kilometer"));
                    car.setFuel(rs.getString("fuel"));
                    car.setLicence_plate(rs.getString("licence_plate"));
                    car.setTransmission(rs.getString("transmission"));
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public boolean addCar(Car car) {
        String query = "INSERT INTO ride_on.cars (brand, model, production_year, color, price_per_day, id, license_plate, kilometer, chassis, fuel, transmission) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getColor());
            stmt.setDouble(5, car.getPricePerDay());
            stmt.setInt(6, car.getCarId());
            stmt.setString(7, car.getLicence_plate());
            stmt.setInt(8, car.getKilometer());
            stmt.setString(9, car.getChassis());
            stmt.setString(10, car.getFuel());


            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCar(Car car) {
        String query = "UPDATE ride_on.cars SET brand = ?, model = ?, production_year = ?, color = ?, " +
                "price_per_day = ?, id = ?, license_plate = ?, kilometer = ?, chassis = ?, fuel = ?, transmission = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getColor());
            stmt.setDouble(5, car.getPricePerDay());
            stmt.setInt(6, car.getCarId());
            stmt.setString(7, car.getLicence_plate());
            stmt.setInt(8, car.getKilometer());
            stmt.setString(9, car.getChassis());
            stmt.setString(10, car.getFuel());
            stmt.setString(11, car.getTransmission());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCar(int carId) {
        String query = "DELETE FROM ride_on.cars WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCarStatusAtDate(int carId, LocalDate date) {

        String status = "available";
        String query = "SELECT * FROM ride_on.reservations WHERE car_id = ? AND ? BETWEEN start_date AND end_date;";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, carId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    status = resultSet.getString("status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean updateCarAvailability(int carId, String status, LocalDate startDate, LocalDate endDate) {
        String query = "UPDATE ride_on.car_statuses SET status = ? WHERE car_id = ? AND start_date = ? AND end_date = ?";
        int rowsAffected = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, carId);
            preparedStatement.setDate(3, Date.valueOf(startDate));
            preparedStatement.setDate(4, Date.valueOf(endDate));

            rowsAffected = preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return rowsAffected > 0;
    }

    public void deleteCarStatus(int carId, LocalDate startDate, LocalDate endDate) {
        String query = "DELETE FROM ride_on.car_statuses WHERE car_id = ? AND start_date = ? AND end_date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, carId);
            stmt.setDate(2, java.sql.Date.valueOf(startDate));
            stmt.setDate(3, java.sql.Date.valueOf(endDate));

            int rowsAffected = stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
