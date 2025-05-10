package org.example.UI;

import org.example.Model.CarFilterRequest;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FilterDialog extends JDialog {
    private CarFilterRequest filterRequest;
    private boolean confirmed = false;

    public FilterDialog(JFrame parent) {
        super(parent, "Filter Cars", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(12, 2, 5, 5));

        JTextField yearField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField minPriceField = new JTextField();
        JTextField maxPriceField = new JTextField();
        JTextField fuelField = new JTextField();
        JTextField maxKmField = new JTextField();
        JTextField transmissionField = new JTextField();
        JTextField startDateField = new JTextField("yyyy-MM-dd");
        JTextField endDateField = new JTextField("yyyy-MM-dd");

        add(new JLabel("Year:")); add(yearField);
        add(new JLabel("Brand:")); add(brandField);
        add(new JLabel("Model:")); add(modelField);
        add(new JLabel("Color:")); add(colorField);
        add(new JLabel("Min Price:")); add(minPriceField);
        add(new JLabel("Max Price:")); add(maxPriceField);
        add(new JLabel("Fuel:")); add(fuelField);
        add(new JLabel("Max Km:")); add(maxKmField);
        add(new JLabel("Transmission:")); add(transmissionField);
        add(new JLabel("Start Date:")); add(startDateField);
        add(new JLabel("End Date:")); add(endDateField);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        add(ok); add(cancel);

        ok.addActionListener(e -> {
            filterRequest = new CarFilterRequest();
            try {
                filterRequest.year = yearField.getText().isEmpty() ? null : Integer.parseInt(yearField.getText());
                filterRequest.brand = brandField.getText().trim().isEmpty() ? null : brandField.getText().trim();
                filterRequest.model = modelField.getText().trim().isEmpty() ? null : modelField.getText().trim();
                filterRequest.color = colorField.getText().trim().isEmpty() ? null : colorField.getText().trim();
                filterRequest.minPrice = minPriceField.getText().isEmpty() ? null : Double.parseDouble(minPriceField.getText());
                filterRequest.maxPrice = maxPriceField.getText().isEmpty() ? null : Double.parseDouble(maxPriceField.getText());
                filterRequest.fuel = fuelField.getText().trim().isEmpty() ? null : fuelField.getText().trim();
                filterRequest.maxKilometer = maxKmField.getText().isEmpty() ? null : Integer.parseInt(maxKmField.getText());
                filterRequest.transmission = transmissionField.getText().trim().isEmpty() ? null : transmissionField.getText().trim();
                filterRequest.startDate = startDateField.getText().isEmpty() ? null : LocalDate.parse(startDateField.getText());
                filterRequest.endDate = endDateField.getText().isEmpty() ? null : LocalDate.parse(endDateField.getText());
                confirmed = true;
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancel.addActionListener(e -> {
            filterRequest = null;
            confirmed = false;
            setVisible(false);
        });
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public CarFilterRequest getFilterRequest() {
        return filterRequest;
    }
}

