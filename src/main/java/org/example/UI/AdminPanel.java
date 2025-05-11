package org.example.UI;

import org.example.Controller.CarController;
import org.example.Controller.ReservationController;
import org.example.Controller.UserController;
import org.example.Model.Car;
import org.example.Model.Reservation;
import org.example.Model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AdminPanel extends JPanel {
    CarController carController = new CarController();
    UserController userController = new UserController();
    ReservationController reservationController = new ReservationController();
    public AdminPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addCarButton = new JButton("Add Car");
        JButton removeCarButton = new JButton("Remove Car");
        JButton changeCarAvailabilityButton = new JButton("Change Car Availability");
        JButton viewUsersButton = new JButton("View Users");
        JButton manageReservationsButton = new JButton("Manage Reservations");

        addCarButton.addActionListener(e -> handleAddCar());
        removeCarButton.addActionListener(e -> handleRemoveCar());
        viewUsersButton.addActionListener(e -> handleViewUsers());
        manageReservationsButton.addActionListener(e -> handleManageReservations());
        changeCarAvailabilityButton.addActionListener(e -> handleChangeCarAvailability());

        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(addCarButton, gbc);

        gbc.gridy = 1;
        content.add(removeCarButton, gbc);

        gbc.gridy = 2;
        content.add(changeCarAvailabilityButton, gbc);

        gbc.gridy = 3;
        content.add(viewUsersButton, gbc);

        gbc.gridy = 4;
        content.add(manageReservationsButton, gbc);

        add(content, BorderLayout.CENTER);
    }

    private void handleChangeCarAvailability() {
        List<Car> cars = carController.getAllCars();

        if (cars.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cars available.");
            return;
        }

        String[] carOptions = cars.stream()
                .map(car -> car.getCarId() + " - " + car.getBrand() + " " + car.getModel() + " (" + car.getLicence_plate() + ")")
                .toArray(String[]::new);

        String selectedCar = (String) JOptionPane.showInputDialog(
                this,
                "Select a car to change its availability:",
                "Select Car",
                JOptionPane.PLAIN_MESSAGE,
                null,
                carOptions,
                carOptions[0]
        );

        if (selectedCar == null) return;

        int carId = Integer.parseInt(selectedCar.split(" - ")[0]);

        String[] statusOptions = {"available", "reserved", "at_customer", "maintenance"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status for the car:",
                "Change Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                statusOptions,
                statusOptions[0]
        );

        if (newStatus == null) return;

        LocalDate startDate = null;
        LocalDate endDate = null;


        String startDateStr = JOptionPane.showInputDialog(this, "Enter start date (YYYY-MM-DD):");
        String endDateStr = JOptionPane.showInputDialog(this, "Enter end date (YYYY-MM-DD):");

        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format.");
            return;
        }


        boolean success = carController.updateCarAvailability(carId, newStatus, startDate, endDate);

        if (success) {
            JOptionPane.showMessageDialog(this, "Car availability updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update car availability.");
        }
    }

    private void handleAddCar() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField licensePlateField = new JTextField();
        JTextField kilometerField = new JTextField();
        JTextField chassisField = new JTextField();

        JComboBox<String> fuelBox = new JComboBox<>(new String[]{"gas", "diesel", "electric", "hybrid", "lpg"});
        JComboBox<String> transmissionBox = new JComboBox<>(new String[]{"manual", "automatic"});

        panel.add(new JLabel("Brand:"));
        panel.add(brandField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Color (optional):"));
        panel.add(colorField);
        panel.add(new JLabel("Price per Day:"));
        panel.add(priceField);
        panel.add(new JLabel("License Plate:"));
        panel.add(licensePlateField);
        panel.add(new JLabel("Kilometers:"));
        panel.add(kilometerField);
        panel.add(new JLabel("Chassis Number:"));
        panel.add(chassisField);
        panel.add(new JLabel("Fuel Type:"));
        panel.add(fuelBox);
        panel.add(new JLabel("Transmission:"));
        panel.add(transmissionBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Car", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String brand = brandField.getText();
                String model = modelField.getText();
                int year = Integer.parseInt(yearField.getText());
                String color = colorField.getText().isEmpty() ? null : colorField.getText();
                double pricePerDay = Double.parseDouble(priceField.getText());
                String licensePlate = licensePlateField.getText();
                Integer kilometer = Integer.parseInt(kilometerField.getText());
                String chassis = chassisField.getText();
                String fuel = (String) fuelBox.getSelectedItem();
                String transmission = (String) transmissionBox.getSelectedItem();

                boolean success = carController.addCar(brand, model, year, color, pricePerDay, licensePlate, kilometer, chassis, fuel, transmission);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Car added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add car.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for year, price, or kilometers.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage());
            }
        }
    }

    private void handleRemoveCar() {
        List<Car> cars = carController.getAllCars();

        if (cars.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no cars to remove.");
            return;
        }

        String[] columnNames = {"ID", "Brand", "Model", "Year", "License Plate"};
        Object[][] data = new Object[cars.size()][columnNames.length];
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            data[i][0] = car.getCarId();
            data[i][1] = car.getBrand();
            data[i][2] = car.getModel();
            data[i][3] = car.getYear();
            data[i][4] = car.getLicence_plate();
        }

        JTable table = new JTable(data, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Selected Car");
        panel.add(deleteButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Remove Car", true);
        dialog.getContentPane().add(panel);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int carId = (int) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete this car?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = carController.deleteCar(carId);
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Car deleted successfully.");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to delete the car.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a car to delete.");
            }
        });

        dialog.setVisible(true);
    }

    private void handleViewUsers() {
        List<User> users = userController.getAllUsers();

        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available.");
            return;
        }

        // Kullanıcıları seçim listesine dönüştür
        String[] userOptions = users.stream()
                .map(user -> user.getUserId() + " - " + user.getName() + " (" + (user.isAdmin() == true ? "Admin" : "User") + ")")
                .toArray(String[]::new);

        String selectedUserStr = (String) JOptionPane.showInputDialog(
                this,
                "Select a user:",
                "View Users",
                JOptionPane.PLAIN_MESSAGE,
                null,
                userOptions,
                userOptions[0]
        );

        if (selectedUserStr == null) return;

        int selectedUserId = Integer.parseInt(selectedUserStr.split(" - ")[0]);
        User selectedUser = users.stream().filter(u -> u.getUserId() == selectedUserId).findFirst().orElse(null);

        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "User not found.");
            return;
        }

        String[] actions = {"Delete User", "Change Admin Status"};
        String selectedAction = (String) JOptionPane.showInputDialog(
                this,
                "Choose an action for user:",
                "User Actions",
                JOptionPane.PLAIN_MESSAGE,
                null,
                actions,
                actions[0]
        );

        if (selectedAction == null) return;

        switch (selectedAction) {
            case "Delete User":
                int confirmDelete = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this user?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    boolean deleted = userController.deleteUser(selectedUser.getUserId());
                    if (deleted) {
                        JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete user.");
                    }
                }
                break;

            case "Change Admin Status":
                int newStatus = selectedUser.isAdmin() == true ? 0 : 1;
                boolean updated = userController.changeAdminStatus(selectedUser, newStatus);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "User admin status updated to " + (newStatus == 1 ? "Admin" : "User") + ".");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update admin status.");
                }
                break;
        }
    }
    private void handleManageReservations() {
        List<Reservation> reservations = reservationController.getAllReservations();

        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No reservations found.");
            return;
        }

        String[] reservationOptions = reservations.stream()
                .map(r -> "ID: " + r.getReservationId() + " | Car ID: " + r.getCarId() + " | " +
                        r.getStartDate() + " to " + r.getEndDate())
                .toArray(String[]::new);

        String selectedReservationStr = (String) JOptionPane.showInputDialog(
                this,
                "Select a reservation:",
                "Manage Reservations",
                JOptionPane.PLAIN_MESSAGE,
                null,
                reservationOptions,
                reservationOptions[0]
        );

        if (selectedReservationStr == null) return;

        int selectedReservationId = Integer.parseInt(selectedReservationStr.split(" ")[1]);
        Reservation selectedReservation = reservations.stream()
                .filter(r -> r.getReservationId() == selectedReservationId)
                .findFirst()
                .orElse(null);

        if (selectedReservation == null) {
            JOptionPane.showMessageDialog(this, "Reservation not found.");
            return;
        }

        String[] actions = {"Cancel Reservation", "Modify Reservation"};
        String selectedAction = (String) JOptionPane.showInputDialog(
                this,
                "Choose an action for the reservation:",
                "Reservation Actions",
                JOptionPane.PLAIN_MESSAGE,
                null,
                actions,
                actions[0]
        );

        if (selectedAction == null) return;

        switch (selectedAction) {
            case "Cancel Reservation":
                int confirmCancel = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this reservation?",
                        "Confirm Cancel",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmCancel == JOptionPane.YES_OPTION) {
                    boolean cancelled = reservationController.cancelReservation(selectedReservation.getReservationId());
                    if (cancelled) {
                        JOptionPane.showMessageDialog(this, "Reservation cancelled.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel reservation.");
                    }
                }
                break;

            case "Modify Reservation":
                try {
                    String carIdStr = JOptionPane.showInputDialog(this, "Enter new Car ID:", selectedReservation.getCarId());
                    String startStr = JOptionPane.showInputDialog(this, "Enter new Start Date (YYYY-MM-DD):", selectedReservation.getStartDate());
                    String endStr = JOptionPane.showInputDialog(this, "Enter new End Date (YYYY-MM-DD):", selectedReservation.getEndDate());

                    if (carIdStr == null || startStr == null || endStr == null) return;

                    int newCarId = Integer.parseInt(carIdStr.trim());
                    LocalDate newStart = LocalDate.parse(startStr.trim());
                    LocalDate newEnd = LocalDate.parse(endStr.trim());

                    boolean modified = reservationController.modifyReservation(
                            selectedReservation.getReservationId(), newCarId, newStart, newEnd
                    );

                    if (modified) {
                        JOptionPane.showMessageDialog(this, "Reservation modified successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to modify reservation.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Modification aborted.");
                }
                break;
        }
    }

}
