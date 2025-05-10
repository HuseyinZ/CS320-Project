package org.example.UI;

import org.example.Controller.AuthController;
import org.example.Controller.CarController;
import org.example.Controller.ReservationController;
import org.example.DAO.UserDAO;
import org.example.Service.UserService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private AuthController authController;
    private CarController carController;
    private ReservationController reservationController;
    private UserService userService;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Sadece Login panel burada kalıyor
    private LoginPanel loginPanel;

    // Yeni panel nesneleri
    private RegisterPanel registerPanel;
    private DashboardPanel dashboardPanel;
    private ProfilePanel profilePanel;
    private CarsListPanel carsListPanel;
    private CarDetailsPanel carDetailsPanel;
    private ReservationsPanel reservationsPanel;
    private AdminPanel adminPanel;

    public MainFrame() throws InterruptedException {
        authController = new AuthController();
        carController = new CarController();
        reservationController = new ReservationController();

        initializeUI();
    }

    private void initializeUI() throws InterruptedException {
        setTitle("RideOn Car Rental System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Sadece login paneli oluşturuluyor
        createLoginPanel();
        mainPanel.add(loginPanel, "login");

        cardLayout.show(mainPanel, "login");
        add(mainPanel);
    }


    private void initializePostLoginPanels() {
        userService = new UserService();

        registerPanel      = new RegisterPanel(authController, cardLayout, mainPanel);
        dashboardPanel     = new DashboardPanel(createMenuPanel());
        profilePanel       = new ProfilePanel(createMenuPanel(), authController.getCurrentUser(), userService);
        carsListPanel      = new CarsListPanel(createMenuPanel());
        carDetailsPanel    = new CarDetailsPanel(createMenuPanel());
        reservationsPanel  = new ReservationsPanel(createMenuPanel());
        adminPanel         = new AdminPanel(createMenuPanel());

        mainPanel.add(registerPanel,  "register");
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(profilePanel,   "profile");
        mainPanel.add(carsListPanel,  "cars");
        mainPanel.add(carDetailsPanel,"carDetails");
        mainPanel.add(reservationsPanel, "reservations");
        mainPanel.add(adminPanel,     "admin");
    }

    private void createLoginPanel() {
        loginPanel = new LoginPanel(authController, cardLayout, mainPanel);
        loginPanel.setLoginSuccessListener(() -> {
            initializePostLoginPanels();
            cardLayout.show(mainPanel, "dashboard");
        });
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(200, 600));
        menuPanel.setBorder(BorderFactory.createEtchedBorder());

        JButton dashboardBtn    = new JButton("Dashboard");
        JButton profileBtn      = new JButton("My Profile");
        JButton carsBtn         = new JButton("Our Cars");
        JButton reservationsBtn = new JButton("My Reservations");
        JButton adminBtn        = new JButton("Admin Panel");
        JButton logoutBtn       = new JButton("Logout");

        dashboardBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        profileBtn.addActionListener(e   -> cardLayout.show(mainPanel, "profile"));
        carsBtn.addActionListener(e      -> cardLayout.show(mainPanel, "cars"));
        reservationsBtn.addActionListener(e-> cardLayout.show(mainPanel, "reservations"));
        adminBtn.addActionListener(e     -> cardLayout.show(mainPanel, "admin"));
        logoutBtn.addActionListener(e    -> {
            authController.logout();
            cardLayout.show(mainPanel, "login");
        });

        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(dashboardBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(profileBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(carsBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(reservationsBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        if (authController.isAdmin()) {
            menuPanel.add(adminBtn);
            menuPanel.add(Box.createVerticalStrut(10));
        }
        menuPanel.add(logoutBtn);
        menuPanel.add(Box.createVerticalGlue());

        return menuPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame().setVisible(true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}