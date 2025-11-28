package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;
import controllers.*;
import java.awt.*;
import java.util.List;

public class RiderGUI {

    private Rider rider;
    private MainController mainController;

    private NotificationController notificationController;

    private JFrame frame;
    private JTextArea outputArea;
    private JTable ridesTable;
    private DefaultTableModel ridesModel;

    public RiderGUI(Rider rider, MainController mainController) {
        this.rider = rider;
        this.mainController = mainController;

        // Access notification controller safely
        this.notificationController = mainController.notificationController;
    }

    public void showFrame() {
        frame = new JFrame("Rider Dashboard - " + rider.getName());
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // ---------------- Profile Button ----------------
        JButton profileBtn = new JButton("View Profile");
        profileBtn.setBounds(20, 20, 120, 30);
        profileBtn.addActionListener(e -> {
            if (mainController.riderController != null) {
                mainController.riderController.updateView();
            } else {
                displayMessage("RiderController not initialized yet.");
            }
        });

        // ---------------- Notifications Button ----------------
        JButton notificationsBtn = new JButton("Notifications");
        notificationsBtn.setBounds(160, 20, 140, 30);
        notificationsBtn.addActionListener(e -> showNotifications());

        // ---------------- Rides Table ----------------
        ridesModel = new DefaultTableModel(new Object[]{"Ride ID", "Pickup", "Dropoff", "Seats", "Fare"}, 0);
        ridesTable = new JTable(ridesModel);
        JScrollPane ridesScroll = new JScrollPane(ridesTable);
        ridesScroll.setBounds(20, 70, 640, 200);
        populateRidesTable(Database.rides);

        // ---------------- Book Ride Button ----------------
        JButton bookRideBtn = new JButton("Book Selected Ride");
        bookRideBtn.setBounds(20, 280, 200, 30);
        bookRideBtn.addActionListener(e -> bookSelectedRide());

        // ---------------- Output Area ----------------
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBounds(20, 320, 640, 120);

        // ---------------- Add Components ----------------
        frame.add(profileBtn);
        frame.add(notificationsBtn);
        frame.add(ridesScroll);
        frame.add(bookRideBtn);
        frame.add(outputScroll);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------------- Display Methods ----------------
    public void displayRiderInfo(Rider rider) {
        displayMessage("Rider Profile:");
        displayMessage("Name: " + rider.getName());
        displayMessage("Phone: " + rider.getPhoneNumber());
        displayMessage("Address: " + rider.getAddress());
        displayMessage("Wallet Balance: $" + rider.getWallet().getBalance());
        displayMessage("Ride History Count: " + rider.getRideHistory().size());
    }

    public void displayMessage(String msg) {
        if (outputArea != null) {
            outputArea.append(msg + "\n");
        }
    }

    public void clearMessages() {
        if (outputArea != null) outputArea.setText("");
    }

    // ---------------- Table Methods ----------------
    private void populateRidesTable(List<Ride> rides) {
        ridesModel.setRowCount(0);
        for (Ride ride : rides) {
            ridesModel.addRow(new Object[]{
                ride.getId(),
                ride.getPickupLocation(),
                ride.getDropoffLocation(),
                ride.getAvailableSeats(),
                ride.getFareEstimate()
            });
        }
    }

    private void bookSelectedRide() {
        int selectedRow = ridesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a ride to book.");
            return;
        }

        int rideId = (int) ridesModel.getValueAt(selectedRow, 0);
        Ride ride = Database.rides.stream().filter(r -> r.getId() == rideId).findFirst().orElse(null);
        if (ride == null) {
            displayMessage("Selected ride not found.");
            return;
        }

        Payment payment = new Payment();
        payment.setAmount(ride.getFareEstimate());
        payment.setTransactionStatus("Pending");

        if (mainController.riderController != null) {
            mainController.riderController.makePayment(payment);
            mainController.riderController.createBooking(ride, 1, payment);
        } else {
            displayMessage("RiderController not initialized yet.");
        }

        if (notificationController != null) {
            notificationController.sendNotification(
                rider,
                "Ride booked: " + ride.getPickupLocation() + " -> " + ride.getDropoffLocation()
            );
        }

        // Refresh table
        populateRidesTable(Database.rides);
    }

    // ---------------- Notifications ----------------
    private void showNotifications() {
        List<Notification> notifications = Database.notifications.stream()
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
