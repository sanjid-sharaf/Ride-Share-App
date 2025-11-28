package ui;

import javax.swing.*;
import models.*;
import controllers.*;

public class DriverGUI {

    private Driver driver;
    private MainController mainController;
    private JFrame frame;

    public DriverGUI(Driver driver, MainController mainController) {
        this.driver = driver;
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Driver Dashboard - " + driver.getName());
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // ----- Buttons -----
        JButton profileBtn = new JButton("View Profile");
        profileBtn.setBounds(50, 50, 150, 30);
        profileBtn.addActionListener(e -> mainController.driverController.updateView());

        JButton addRideBtn = new JButton("Add Ride");
        addRideBtn.setBounds(50, 100, 150, 30);
        addRideBtn.addActionListener(e -> addRideDialog());

        JButton notificationsBtn = new JButton("View Notifications");
        notificationsBtn.setBounds(50, 150, 180, 30);
        notificationsBtn.addActionListener(e -> 
            mainController.notificationController.updateView(driver, mainController.driverController.getView())
        );

        frame.add(profileBtn);
        frame.add(addRideBtn);
        frame.add(notificationsBtn);

        frame.setVisible(true);
    }

    // ----- Dialog to add ride -----
    private void addRideDialog() {
        String pickup = JOptionPane.showInputDialog(frame, "Enter pickup location:");
        if (pickup == null || pickup.isEmpty()) return;

        String dropoff = JOptionPane.showInputDialog(frame, "Enter dropoff location:");
        if (dropoff == null || dropoff.isEmpty()) return;

        int seats;
        double fare;
        try {
            seats = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter number of seats:"));
            fare = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter fare:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input for seats or fare.");
            return;
        }

        Ride ride = new Ride();
        ride.setPickupLocation(pickup);
        ride.setDropoffLocation(dropoff);
        ride.setAvailableSeats(seats);
        ride.setFareEstimate(fare);
        ride.setDriver(driver);

        mainController.driverController.createRide(ride); // Save in controller
        JOptionPane.showMessageDialog(frame, "Ride added successfully!");
    }
}
