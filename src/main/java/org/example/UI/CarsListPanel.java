package org.example.UI;

import org.example.Controller.CarController;
import org.example.Model.Car;
import org.example.Model.CarFilterRequest;

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
        JLabel title = new JLabel("Our Cars");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Brand", "Model", "Color", "Transmission", "Fuel", "Year", "Price/Day"};
        model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton refreshButton = new JButton("Refresh List");
        JButton filterButton = new JButton("Filter Cars");

        refreshButton.addActionListener(e -> {
            loadCars(carController); // Listeyi yenile
            JOptionPane.showMessageDialog(null, "Latest list listed", "Refresh", JOptionPane.PLAIN_MESSAGE);
        });


        filterButton.addActionListener(e -> {
            FilterDialog dialog = new FilterDialog((JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                filter(carController, dialog.getFilterRequest());
                JOptionPane.showMessageDialog(null, "List Filtered", "Filter", JOptionPane.PLAIN_MESSAGE);
            }
        });

        btns.add(refreshButton);
        btns.add(filterButton);

        content.add(btns, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        loadCars(carController); // Sayfa açıldığında araçları yükle
    }


    private void filter(CarController carController, CarFilterRequest request) {
        model.setRowCount(0); // Tabloyu temizle
        ArrayList<Car> cars = (ArrayList<Car>) carController.filterCars(request); // Yeni parametreli versiyon
        fillTable(model, cars);
    }



    private void loadCars(CarController carController) {
        model.setRowCount(0); // Önce tabloyu temizle
        ArrayList<Car> cars = (ArrayList<Car>) carController.getAllCars(); // Arabaları getir
        fillTable(model, cars);

    }

    private void fillTable(DefaultTableModel model, ArrayList<Car> cars) {
        for (Car car : cars) {
            Object[] row = {
                    car.getCarId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getColor(),
                    car.getTransmission(),
                    car.getFuel(),
                    car.getYear(),
                    car.getPricePerDay()
            };
            model.addRow(row);
        }
    }
}