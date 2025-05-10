package org.example.UI;

import org.example.Controller.CarController;
import org.example.Model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CarsListPanel extends JPanel {

    private DefaultTableModel model;

    public CarsListPanel(JPanel menuPanel, CarController carController) {
        super(new BorderLayout());

        add(menuPanel, BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Available Cars");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Brand", "Model", "Transmission", "Year", "Plate", "Price/Day"};
        model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton refreshButton = new JButton("Refresh List");
        //JButton filterButton = new JButton("Sort by Date");

        refreshButton.addActionListener(e -> {
            loadCars(carController); // Listeyi yenile
            JOptionPane.showMessageDialog(null, "Latest list listed", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });

        /*
        filterButton.addActionListener(e -> {
            filter(carController);
            JOptionPane.showMessageDialog(null, "List Filtered", "Filter", JOptionPane.PLAIN_MESSAGE);
        });

         */

        btns.add(refreshButton);
        //btns.add(filterButton);
        //add other upcoming buttons

        content.add(btns, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        loadCars(carController); // Sayfa açıldığında araçları yükle
    }

    /*
    private void filter(CarController carController) {
        model.setRowCount(0); // Önce tabloyu temizle


        ArrayList<Car> cars = (ArrayList<Car>) carController.filterCars(); // Arabaları getir

        for (Car car : cars) {
            Object[] row = {
                    car.getCarId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getTransmission(),
                    car.getYear(),
                    car.getLicence_plate(),
                    car.getPricePerDay()
            };
            model.addRow(row);
        }
    }

     */

    private void loadCars(CarController carController) {
        model.setRowCount(0); // Önce tabloyu temizle
        ArrayList<Car> cars = (ArrayList<Car>) carController.getAllCars(); // Arabaları getir

        for (Car car : cars) {
            Object[] row = {
                    car.getCarId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getTransmission(),
                    car.getYear(),
                    car.getLicence_plate(),
                    car.getPricePerDay()
            };
            model.addRow(row);
        }
    }
}