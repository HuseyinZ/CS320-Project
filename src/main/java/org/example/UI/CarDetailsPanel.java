package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class CarDetailsPanel extends JPanel {
    public CarDetailsPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        // Araç detayları içeriği buraya eklenecek
    }
}
