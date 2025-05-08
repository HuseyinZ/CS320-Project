package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class CarsListPanel extends JPanel {
    public CarsListPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        // Araç listesi içeriği buraya eklenecek
    }
}
