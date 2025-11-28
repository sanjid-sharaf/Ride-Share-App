package ui;

import javax.swing.*;
import models.*;
import controllers.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

        JButton profileBtn = new JButton("View Profile");
        profileBtn.setBounds(50, 50, 150, 30);
        profileBtn.addActionListener(e -> showProfileGUI());

        JButton addRideBtn = new JButton("Add Ride");
        addRideBtn.setBounds(50, 100, 150, 30);
        addRideBtn.addActionListener(e -> addRideDialog());

        JButton rideHistoryBtn = new JButton("Ride History");
        rideHistoryBtn.setBounds(50, 150, 150, 30);
        rideHistoryBtn.addActionListener(e -> showRideHistory());

        JButton cancelRideBtn = new JButton("Cancel Ride");
        cancelRideBtn.setBounds(50, 200, 150, 30);
        cancelRideBtn.addActionListener(e -> cancelRide());

        JButton notificationsBtn = new JButton("View Notifications");
        notificationsBtn.setBounds(50, 250, 180, 30);
        notificationsBtn.addActionListener(e ->
                mainController.notificationController.updateView(driver, mainController.driverController.getView())
        );

        JButton logoutBtn = new JButton("Logout");
logoutBtn.setBounds(50, 300, 150, 30);
logoutBtn.addActionListener(e -> {
    Database.saveAll();
    frame.dispose(); // Close DriverGUI
    RideGUI rideGUI = new RideGUI(); // create a new instance of RideGUI
    rideGUI.init(); // initialize the login page
});

        frame.add(profileBtn);
        frame.add(addRideBtn);
        frame.add(rideHistoryBtn);
        frame.add(cancelRideBtn);
        frame.add(notificationsBtn);
        frame.add(logoutBtn);

        frame.setVisible(true);
    }

    // ---------------- Show Profile GUI ----------------
    private void showProfileGUI() {
        JFrame profileFrame = new JFrame(driver.getName() + " - Profile");
        profileFrame.setSize(400, 300);
        profileFrame.setLayout(new GridLayout(0, 1, 5, 5));
        profileFrame.setLocationRelativeTo(frame);

        profileFrame.add(new JLabel("ID: " + driver.getId()));
        profileFrame.add(new JLabel("Name: " + driver.getName()));
        profileFrame.add(new JLabel("Email: " + (driver.getEmail() != null ? driver.getEmail() : "N/A")));
        profileFrame.add(new JLabel("Phone: " + (driver.getPhoneNumber() != null ? driver.getPhoneNumber() : "N/A")));
        profileFrame.add(new JLabel("Address: " + (driver.getAddress() != null ? driver.getAddress() : "N/A")));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> profileFrame.dispose());
        profileFrame.add(backBtn);

        profileFrame.setVisible(true);
    }

    // ---------------- Add Ride ----------------
    private void addRideDialog() {
        JFrame addRideFrame = new JFrame("Add Ride");
        addRideFrame.setSize(400, 300);
        addRideFrame.setLayout(new GridLayout(0, 2, 5, 5));
        addRideFrame.setLocationRelativeTo(frame);

        JTextField pickupField = new JTextField();
        JTextField dropoffField = new JTextField();
        JTextField seatsField = new JTextField();
        JTextField fareField = new JTextField();

        addRideFrame.add(new JLabel("Pickup Location:"));
        addRideFrame.add(pickupField);
        addRideFrame.add(new JLabel("Dropoff Location:"));
        addRideFrame.add(dropoffField);
        addRideFrame.add(new JLabel("Number of Seats:"));
        addRideFrame.add(seatsField);
        addRideFrame.add(new JLabel("Fare:"));
        addRideFrame.add(fareField);

        JButton addBtn = new JButton("Add Ride");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> {
            String pickup = pickupField.getText().trim();
            String dropoff = dropoffField.getText().trim();
            int seats;
            double fare;

            if (pickup.isEmpty() || dropoff.isEmpty()) {
                JOptionPane.showMessageDialog(addRideFrame, "Pickup and Dropoff cannot be empty.");
                return;
            }

            try {
                seats = Integer.parseInt(seatsField.getText().trim());
                fare = Double.parseDouble(fareField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addRideFrame, "Invalid input for seats or fare.");
                return;
            }

            Ride ride = new Ride();
            ride.setPickupLocation(pickup);
            ride.setDropoffLocation(dropoff);
            ride.setAvailableSeats(seats);
            ride.setFareEstimate(fare);
            ride.setDriver(driver);

            mainController.driverController.createRide(ride);
            JOptionPane.showMessageDialog(addRideFrame, "Ride added successfully!");
            addRideFrame.dispose();
        });

        backBtn.addActionListener(e -> addRideFrame.dispose());

        addRideFrame.add(addBtn);
        addRideFrame.add(backBtn);

        addRideFrame.setVisible(true);
        Database.saveAll();
    }

    // ---------------- Ride History ----------------
    private void showRideHistory() {
        List<Ride> rides = new ArrayList<>();
        for (Ride r : Database.rides) {
            if (r.getDriver() != null && r.getDriver().equals(driver)) rides.add(r);
        }

        if (rides.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No rides available.");
            return;
        }

        JFrame historyFrame = new JFrame("Ride History");
        historyFrame.setSize(500, 400);
        historyFrame.setLayout(new GridLayout(0, 1, 5, 5));
        historyFrame.setLocationRelativeTo(frame);

        for (Ride r : rides) {
            historyFrame.add(new JLabel(
                    "Ride ID: " + r.getId() +
                            " | From: " + r.getPickupLocation() +
                            " | To: " + r.getDropoffLocation() +
                            " | Seats: " + r.getAvailableSeats() +
                            " | Fare: " + r.getFareEstimate()
            ));
        }

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> historyFrame.dispose());
        historyFrame.add(backBtn);

        historyFrame.setVisible(true);
    }

    // ---------------- Cancel Ride ----------------
    private void cancelRide() {
        List<Ride> rides = new ArrayList<>();
        for (Ride r : Database.rides) {
            if (r.getDriver() != null && r.getDriver().equals(driver)) rides.add(r);
        }

        if (rides.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No rides available to cancel.");
            return;
        }

        String[] options = new String[rides.size()];
        for (int i = 0; i < rides.size(); i++) {
            Ride r = rides.get(i);
            options[i] = "Ride ID: " + r.getId() +
                    " | From: " + r.getPickupLocation() +
                    " | To: " + r.getDropoffLocation();
        }

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Select the ride you want to cancel:",
                "Cancel Ride",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected == null) return;

        int selectedIndex = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selected)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) {
            Ride toCancel = rides.get(selectedIndex);
            Database.rides.remove(toCancel);
            JOptionPane.showMessageDialog(frame, "Ride ID " + toCancel.getId() + " cancelled successfully.");
        }
        Database.saveAll();
    }
}
