package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.*;
import views.LoginView;
import controllers.*;

public class ridegui {

    private JFrame frame;
    private JTextArea outputArea;
    private User loggedInUser;
    private MainController mainController;

    public void initWithController(MainController controller) {
    this.mainController = controller;  // use the existing MainController
    createMainFrame();                 // build the GUI
}
    private void createMainFrame() {
        // Main window
        frame = new JFrame("TMU Ride-Share App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // ------------------- Text Output Area -------------------
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // ------------------- Top Buttons -------------------
        JPanel topPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        topPanel.add(loginBtn);
        topPanel.add(registerBtn);
        frame.add(topPanel, BorderLayout.NORTH);

        // ------------------- Button Actions -------------------
        loginBtn.addActionListener(e -> showLogin());
        registerBtn.addActionListener(e -> showRegister());

        frame.setVisible(true);
    }

    // ------------------- Show Login -------------------
    private void showLogin() {
        LoginView loginView = new LoginView();
        loginView.setGUI(this); // allow LoginView to print to outputArea

        String name = JOptionPane.showInputDialog(frame, "Enter your name:");
        String password = JOptionPane.showInputDialog(frame, "Enter your password:");

        if (name == null || password == null) {
            displayText("Login cancelled.");
            return;
        }
        
        boolean success = false;
        
        // Check in database
        for (User u : Database.users.values()) {
            if (u.getName().equalsIgnoreCase(name) && u.getPassword().equals(password)) {
                loggedInUser = u;
                success = true;
                break;
            }
        }

        loginView.displayLoginResult(success);

        if (success) {
            if (loggedInUser instanceof Rider) showRiderMenu((Rider) loggedInUser);
            else if (loggedInUser instanceof Driver) showDriverMenu((Driver) loggedInUser);
        }
    }

    // ------------------- Show Register -------------------
    private void showRegister() {
        if (loggedInUser != null) {
            displayText("Already logged in!");
            return;
        }

        String name = JOptionPane.showInputDialog(frame, "Enter your name:");
        String password = JOptionPane.showInputDialog(frame, "Enter a password:");

        if (name == null || password == null || name.isEmpty() || password.isEmpty()) {
            displayText("Registration cancelled or invalid input.");
            return;
        }

        String[] userTypes = {"Rider", "Driver"};
        String type = (String) JOptionPane.showInputDialog(
                frame, "Select user type:", "User Type",
                JOptionPane.PLAIN_MESSAGE, null, userTypes, userTypes[0]
        );

        if (type == null) return;

        User newUser;
        if (type.equals("Rider")) {
            newUser = new Rider();
            ((Rider)newUser).setWallet(new Wallet());
        } else {
            newUser = new Driver();
        }

        newUser.setName(name);
        newUser.setPassword(password);

        Database.users.put(newUser.getId(), newUser);
        loggedInUser = newUser;
        displayText("Registration successful!");

        if (newUser instanceof Rider) showRiderMenu((Rider) newUser);
        else showDriverMenu((Driver) newUser);
    }

    // ------------------- Rider Menu -------------------
    private void showRiderMenu(Rider rider) {
        String[] options = {"View Profile", "Book Ride", "View Notifications", "Logout"};
        boolean running = true;

        while (running) {
            int choice = JOptionPane.showOptionDialog(
                    frame, "Rider Menu", "Rider",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]
            );

            if (choice == 0) {
                mainController.riderController.updateView();
            } else if (choice == 1) {
                bookRide(rider);
            } else if (choice == 2) {
                mainController.notificationController.updateView(rider, mainController.riderController.getView());
            } else {
                running = false;
            }
        }
    }

    private void bookRide(Rider rider) {
        List<Ride> rides = Database.rides;
        if (rides.isEmpty()) {
            displayText("No rides available.");
            return;
        }

        Ride ride = rides.get(0); // For simplicity, pick first ride
        Payment payment = new Payment();
        payment.setAmount(ride.getFareEstimate());
        payment.setTransactionStatus("Pending");

        mainController.riderController.makePayment(payment);
        mainController.riderController.createBooking(ride, 1, payment);

        mainController.notificationController.sendNotification(rider,
                "Your ride from " + ride.getPickupLocation() + " to " + ride.getDropoffLocation() + " is booked!");
        displayText("Ride booked successfully!");
    }

    // ------------------- Driver Menu -------------------
    private void showDriverMenu(Driver driver) {
        String[] options = {"View Profile", "Add Ride", "View Notifications", "Logout"};
        boolean running = true;

        while (running) {
            int choice = JOptionPane.showOptionDialog(
                    frame, "Driver Menu", "Driver",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]
            );

            if (choice == 0) {
                mainController.driverController.updateView();
            } else if (choice == 1) {
                addRide(driver);
            } else if (choice == 2) {
                mainController.notificationController.updateView(driver, mainController.driverController.getView());
            } else {
                running = false;
            }
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

        mainController.driverController.createRide(ride);
        displayText("Ride added successfully!");
    }

    // ------------------- Display Text in GUI -------------------
    public void displayText(String msg) {
        outputArea.append(msg + "\n");
    }
}

