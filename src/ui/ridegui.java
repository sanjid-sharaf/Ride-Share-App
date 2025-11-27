package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.*;
import controllers.*;

public class ridegui {

    private JFrame frame;
    private User loggedInUser;
    private MainController mainController;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ridegui().init());
    }

    private void init() {
        mainController = new MainController(); // Initialize all controllers and mock data
        createMainFrame();
    }

    public void initWithController(MainController controller) {
    this.mainController = controller;
    createMainFrame();
}

    // ------------------- Main Window -------------------
    private void createMainFrame() {
        frame = new JFrame("TMU Ride-Share App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome to TMU Ride-Share!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> showLoginForm());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> showRegisterForm());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ------------------- Login Form -------------------
    private void showLoginForm() {
        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        String[] userTypes = {"Rider", "Driver"};
        JComboBox<String> typeBox = new JComboBox<>(userTypes);

        Object[] message = {
            "Name:", nameField,
            "Password:", passwordField,
            "User Type:", typeBox
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String name = nameField.getText();
        String password = new String(passwordField.getPassword());
        String type = (String) typeBox.getSelectedItem();

        // Use MainController's LoginController
        boolean loggedIn = false;
        for (User u : Database.users.values()) {
            if (u.getName().equalsIgnoreCase(name) && u.getPassword().equals(password)) {
                mainController.loginController.setLoginSystemUser(u);
                loggedInUser = u;
                loggedIn = true;
                break;
            }
        }

        if (!loggedIn) {
            JOptionPane.showMessageDialog(frame, "User not found or password incorrect!");
            return;
        }

        // Redirect to menus via controller
        if (loggedInUser instanceof Rider && type.equals("Rider")) {
            showRiderMenu((Rider) loggedInUser);
        } else if (loggedInUser instanceof Driver && type.equals("Driver")) {
            showDriverMenu((Driver) loggedInUser);
        } else {
            JOptionPane.showMessageDialog(frame, "User type mismatch!");
        }
    }

    // ------------------- Register Form -------------------
    private void showRegisterForm() {
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(frame, "You are already logged in!");
            return;
        }

        String newName = JOptionPane.showInputDialog(frame, "Enter your name:");
        String newPassword = JOptionPane.showInputDialog(frame, "Enter a password:");

        if (newName == null || newName.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Registration cancelled or invalid input!");
            return;
        }

        String[] userTypes = {"Rider", "Driver"};
        String type = (String) JOptionPane.showInputDialog(frame, "Select user type:", "User Type",
                JOptionPane.PLAIN_MESSAGE, null, userTypes, userTypes[0]);
        if (type == null) return;

        User newUser;
        if (type.equals("Rider")) {
            newUser = new Rider();
            ((Rider)newUser).setWallet(new Wallet());
        } else {
            newUser = new Driver();
        }

        newUser.setName(newName);
        newUser.setPassword(newPassword);

        // Use LoginController to register
        mainController.loginController.setLoginSystemUser(newUser);
        mainController.loginController.register();
        loggedInUser = newUser;

        // Redirect to menus
        if (newUser instanceof Rider) showRiderMenu((Rider)newUser);
        else showDriverMenu((Driver)newUser);
    }

    // ------------------- Rider Menu -------------------
    private void showRiderMenu(Rider rider) {
        String[] options = {"View Profile", "Book Ride", "View Notifications", "Logout"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(frame, "Rider Menu", "Rider",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) mainController.riderController.updateView();
            else if (choice == 1) bookRide(rider);
            else if (choice == 2) mainController.notificationController.updateView(rider, mainController.riderController.getView());
            else break;
        }
    }

    private void bookRide(Rider rider) {
        List<Ride> rides = Database.rides;
        if (rides.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No rides available.");
            return;
        }

        Ride ride = rides.get(0); // For simplicity, book first ride
        Payment payment = new Payment();
        payment.setAmount(ride.getFareEstimate());
        payment.setTransactionStatus("Pending");

        // Use RiderController for booking and payment
        mainController.riderController.makePayment(payment);
        mainController.riderController.createBooking(ride, 1, payment);

        mainController.notificationController.sendNotification(rider,
                "Your ride from " + ride.getPickupLocation() + " to " + ride.getDropoffLocation() + " is booked!");
    }

    // ------------------- Driver Menu -------------------
    private void showDriverMenu(Driver driver) {
        String[] options = {"View Profile", "Add Ride", "View Notifications", "Logout"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(frame, "Driver Menu", "Driver",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) mainController.driverController.updateView();
            else if (choice == 1) addRide(driver);
            else if (choice == 2) mainController.notificationController.updateView(driver, mainController.driverController.getView());
            else break;
        }
    }

    private void addRide(Driver driver) {
        String pickup = JOptionPane.showInputDialog(frame, "Enter pickup location:");
        String dropoff = JOptionPane.showInputDialog(frame, "Enter dropoff location:");
        int seats = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter seats:"));
        double fare = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter fare:"));

        Ride ride = new Ride();
        ride.setPickupLocation(pickup);
        ride.setDropoffLocation(dropoff);
        ride.setAvailableSeats(seats);
        ride.setFareEstimate(fare);
        ride.setDriver(driver);

        mainController.driverController.createRide(ride); // Use DriverController
        JOptionPane.showMessageDialog(frame, "Ride added successfully!");
    }
}
