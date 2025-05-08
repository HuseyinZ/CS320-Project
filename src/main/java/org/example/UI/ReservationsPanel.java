package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class ReservationsPanel extends JPanel {
    public ReservationsPanel(JPanel menuPanel) {
        super(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        // Rezervasyonlar içeriği buraya eklenecek
    }
}
