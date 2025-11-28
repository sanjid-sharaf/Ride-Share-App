package ui;

import controllers.MainController;
import models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RiderGUI {

    private Rider rider;
    private MainController mainController;

    private JFrame frame;
    private JTable ridesTable;
    private DefaultTableModel ridesModel;
    private JLabel messageLabel;

    public RiderGUI(Rider rider, MainController mainController) {
        this.rider = rider;
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Rider Dashboard - " + rider.getName());
        frame.setSize(600, 400);
        frame.setLayout(null);

        messageLabel = new JLabel("");
        messageLabel.setBounds(20, 10, 500, 25);
        frame.add(messageLabel);

        ridesModel = new DefaultTableModel(new String[]{"ID", "Pickup", "Dropoff", "Fare"}, 0);
        ridesTable = new JTable(ridesModel);
        JScrollPane scrollPane = new JScrollPane(ridesTable);
        scrollPane.setBounds(20, 50, 550, 200);
        frame.add(scrollPane);

        JButton bookButton = new JButton("Book Ride");
        bookButton.setBounds(20, 270, 120, 30);
        frame.add(bookButton);

        JButton profileButton = new JButton("View Profile");
        profileButton.setBounds(160, 270, 140, 30);
        frame.add(profileButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(320, 270, 120, 30);
        frame.add(logoutButton);

        // Button listeners
        bookButton.addActionListener(e -> bookSelectedRide());
        profileButton.addActionListener(e -> viewProfile());
        logoutButton.addActionListener(e -> mainController.shutdown());

        // Load rides
        populateRidesTable(Database.rides);

        frame.setVisible(true);
    }

    // ---------------------------------------------
    // SAFE BOOKING — NO NULL CONTROLLERS EVER AGAIN
    // ---------------------------------------------
    private void bookSelectedRide() {
        int selectedRow = ridesTable.getSelectedRow();
        if (selectedRow < 0) {
            displayMessage("Please select a ride to book.");
            return;
        }

        int rideId = (int) ridesModel.getValueAt(selectedRow, 0);
        Ride ride = Database.rides.stream()
                .filter(r -> r.getId() == rideId)
                .findFirst()
                .orElse(null);

        if (ride == null) {
            displayMessage("Selected ride not found.");
            return;
        }

        // Create a payment
        Payment payment = new Payment();
        payment.setAmount(ride.getFareEstimate());
        payment.setTransactionStatus("Pending");

        // -------- SAFELY ACCESS CONTROLLERS THROUGH mainController --------
        if (mainController.riderController == null) {
            displayMessage("ERROR: RiderController not initialized.");
            return;
        }

        // Create booking + process payment
        mainController.riderController.makePayment(payment);
        mainController.riderController.createBooking(ride, 1, payment);

        // Send notification safely
        if (mainController.notificationController != null) {
            mainController.notificationController.sendNotification(
                rider,
                "Your ride has been booked: " + ride.getPickupLocation() + " → " + ride.getDropoffLocation()
            );
        }

        displayMessage("Ride booked successfully!");
    }

    // --------------------------
    // SAFE PROFILE VIEW
    // --------------------------
    private void viewProfile() {
        if (mainController.riderController == null) {
            displayMessage("ERROR: RiderController not initialized.");
            return;
        }

        mainController.riderController.updateView();;
    }

    // --------------------------
    // POPULATE TABLE
    // --------------------------
    private void populateRidesTable(List<Ride> rides) {
        ridesModel.setRowCount(0);
        for (Ride r : rides) {
            ridesModel.addRow(new Object[]{
                    r.getId(),
                    r.getPickupLocation(),
                    r.getDropoffLocation(),
                    r.getFareEstimate()
            });
        }
    }

    // --------------------------
    // MESSAGE DISPLAY
    // --------------------------
    public void displayMessage(String msg) {
        messageLabel.setText(msg);
    }
}
