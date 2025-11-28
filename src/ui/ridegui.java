package ui;

import javax.swing.*;
import java.awt.*;
import models.*;
import controllers.*;

public class RideGUI {

    private Ride ride;
    private MainController mainController;
    private JFrame frame;
    private JTextArea outputArea;
    private JTextArea rideDetailsArea;

    public RideGUI(Ride ride, MainController mainController) {
        this.ride = ride;
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Ride Details - Ride #" + ride.getId());
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Ride details
        rideDetailsArea = new JTextArea();
        rideDetailsArea.setEditable(false);
        rideDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        rideDetailsArea.setBounds(20, 20, 440, 150);
        updateRideDetails();

        // Book ride button
        JButton bookBtn = new JButton("Book Ride");
        bookBtn.setBounds(20, 180, 150, 30);
        bookBtn.addActionListener(e -> bookRide());

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBounds(20, 220, 440, 120);

        frame.add(rideDetailsArea);
        frame.add(bookBtn);
        frame.add(scroll);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------------- Book Ride ----------------
    private void bookRide() {
        if (mainController.rideController == null) {
            displayMessage("RideController not initialized.");
            return;
        }

        mainController.rideController.bookRide(1);
        updateRideDetails();
    }

    // ---------------- Update Ride Details ----------------
    public void updateRideDetails() {
        rideDetailsArea.setText(
            "Ride ID: " + ride.getId() + "\n" +
            "Driver: " + (ride.getDriver() != null ? ride.getDriver().getName() : "TBD") + "\n" +
            "Pickup: " + ride.getPickupLocation() + "\n" +
            "Dropoff: " + ride.getDropoffLocation() + "\n" +
            "Available Seats: " + ride.getAvailableSeats() + "\n" +
            "Fare Estimate: $" + ride.getFareEstimate() + "\n" +
            "Date & Time: " + ride.getDateTime()
        );
    }

    public void displayMessage(String msg) {
        if (outputArea != null) {
            outputArea.append(msg + "\n");
        }
    }
}
