package ui;

import javax.swing.*;
import models.*;
import controllers.*;

public class DriverGUI {

    private Driver driver;
    private MainController mainController;

    private JFrame frame;
    private JTextArea outputArea;

    public DriverGUI(Driver driver, MainController mainController) {
        this.driver = driver;
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Driver Dashboard - " + driver.getName());
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---------------- Buttons ----------------
        JButton createRideBtn = new JButton("Create Ride");
        createRideBtn.setBounds(50, 50, 150, 30);
        createRideBtn.addActionListener(e -> createRideDemo());

        JButton viewProfileBtn = new JButton("View Profile");
        viewProfileBtn.setBounds(220, 50, 150, 30);
        viewProfileBtn.addActionListener(e -> updateDriverProfile());

        JButton notificationsBtn = new JButton("Notifications");
        notificationsBtn.setBounds(50, 90, 150, 30);
        notificationsBtn.addActionListener(e -> showNotifications());

        // ---------------- Output Area ----------------
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBounds(50, 130, 380, 200);

        // ---------------- Add Components ----------------
        frame.add(createRideBtn);
        frame.add(viewProfileBtn);
        frame.add(notificationsBtn);
        frame.add(scroll);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------------- Methods ----------------

    private void createRideDemo() {
        if (mainController.driverController == null) {
            displayMessage("DriverController not initialized.");
            return;
        }

        Ride ride = new Ride();
        ride.setId(Database.rides.size() + 1);
        ride.setDriver(driver);
        ride.setPickupLocation("A");
        ride.setDropoffLocation("B");
        ride.setAvailableSeats(4);
        ride.setFareEstimate(20);

        mainController.driverController.createRide(ride);
        displayMessage("Ride created: ID " + ride.getId() + ", " + ride.getPickupLocation() + " -> " + ride.getDropoffLocation());
    }

    private void updateDriverProfile() {
        if (mainController.driverController == null) {
            displayMessage("DriverController not initialized.");
            return;
        }

        mainController.driverController.updateView();
    }

    public void displayMessage(String msg) {
        if (outputArea != null) {
            outputArea.append(msg + "\n");
        }
    }

    public void displayDriverInfo(Driver driver) {
        displayMessage("Driver: " + driver.getName() + ", Vehicle: " + driver.getVehicleDetails());
        displayMessage("License: " + driver.getLicenseNumber());
        displayMessage("Wallet Balance: $" + driver.getWallet().getBalance());
        displayMessage("Ride History Count: " + driver.getRideHistory().size());
    }

    private void showNotifications() {
        if (mainController.notificationController == null) {
            displayMessage("NotificationController not initialized.");
            return;
        }

        var notifications = Database.notifications.stream()
            .filter(n -> n.getStatus().equals("Unread"))
            .toList();

        if (notifications.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No new notifications.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Notification n : notifications) {
            sb.append("ID: ").append(n.getId())
              .append(" - ").append(n.getMessage())
              .append("\n");
            n.setStatus("Read");
        }

        JOptionPane.showMessageDialog(frame, sb.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }
}
