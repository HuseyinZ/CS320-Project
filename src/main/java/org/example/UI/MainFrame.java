package org.example.UI;

import org.example.Controller.AuthController;
import org.example.Controller.CarController;
import org.example.Controller.ReservationController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private AuthController authController;
    private CarController carController;
    private ReservationController reservationController;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Sadece Login panel burada kalıyor
    private JPanel loginPanel;

    // Yeni panel nesneleri
    private RegisterPanel registerPanel;
    private DashboardPanel dashboardPanel;
    private ProfilePanel profilePanel;
    private CarsListPanel carsListPanel;
    private CarDetailsPanel carDetailsPanel;
    private ReservationsPanel reservationsPanel;
    private AdminPanel adminPanel;

    public MainFrame() {
        authController = new AuthController();
        carController = new CarController();
        reservationController = new ReservationController();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("RideOn Car Rental System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Login panel hâlâ kendi yöntemiyle
        createLoginPanel();

        // Menü paneli her defasında yeniden oluşturacağız
        // çünkü Swing'de bir bileşen sadece bir kere gösterilebilir
        registerPanel      = new RegisterPanel(authController, cardLayout, mainPanel);
        dashboardPanel     = new DashboardPanel(createMenuPanel());
        profilePanel       = new ProfilePanel(createMenuPanel());
        carsListPanel      = new CarsListPanel(createMenuPanel());
        carDetailsPanel    = new CarDetailsPanel(createMenuPanel());
        reservationsPanel  = new ReservationsPanel(createMenuPanel());
        adminPanel         = new AdminPanel(createMenuPanel());

        // Kartlara ekle
        mainPanel.add(loginPanel,     "login");
        mainPanel.add(registerPanel,  "register");
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(profilePanel,   "profile");
        mainPanel.add(carsListPanel,  "cars");
        mainPanel.add(carDetailsPanel,"carDetails");
        mainPanel.add(reservationsPanel, "reservations");
        mainPanel.add(adminPanel,     "admin");

        cardLayout.show(mainPanel, "login");
        add(mainPanel);
    }

    private void createLoginPanel() {
        // 1) loginPanel'i oluştur ve alan referansına ata
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 2) Bileşenleri yarat
        JLabel titleLabel      = new JLabel("RideOn - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel   = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel   = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton    = new JButton("Login");
        JButton registerButton = new JButton("Register New Account");

        // 3) ActionListener'ları ekle
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authController.login(username, password)) {
                cardLayout.show(mainPanel, "dashboard");
            } else {
                JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "Invalid username or password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        registerButton.addActionListener(e ->
                cardLayout.show(mainPanel, "register")
        );

        // 4) GridBagLayout ile bileşenleri panele yerleştir
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);
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
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}