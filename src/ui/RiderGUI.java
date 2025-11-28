package ui;

import javax.swing.*;
import controllers.*;
import models.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RiderGUI {

    private Rider rider;
    private MainController mainController;
    private JFrame frame;

    public RiderGUI(Rider rider, MainController mainController) {
        this.rider = rider;
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Rider Dashboard - " + rider.getName());
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton profileBtn = new JButton("View Profile");
        profileBtn.setBounds(50, 50, 150, 30);
        profileBtn.addActionListener(e -> showProfileGUI());

        JButton bookRideBtn = new JButton("Book Ride");
        bookRideBtn.setBounds(50, 100, 150, 30);
        bookRideBtn.addActionListener(e -> bookRide());

        JButton rideHistoryBtn = new JButton("Ride History");
        rideHistoryBtn.setBounds(50, 150, 150, 30);
        rideHistoryBtn.addActionListener(e -> showRideHistory());

        JButton cancelBookingBtn = new JButton("Cancel Booking");
        cancelBookingBtn.setBounds(50, 200, 150, 30);
        cancelBookingBtn.addActionListener(e -> cancelBooking());

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(50, 300, 150, 30);
        logoutBtn.addActionListener(e -> {
            frame.dispose(); // Close RiderGUI
            RideGUI rideGUI = new RideGUI(); // create a new instance of RideGUI
            rideGUI.init(); });


        frame.add(profileBtn);
        frame.add(bookRideBtn);
        frame.add(rideHistoryBtn);
        frame.add(cancelBookingBtn);

        frame.setVisible(true);
    }

    // ---------------- Show Profile GUI ----------------
    private void showProfileGUI() {
        JFrame profileFrame = new JFrame(rider.getName() + " - Profile");
        profileFrame.setSize(400, 300);
        profileFrame.setLayout(new GridLayout(0, 1, 5, 5));
        profileFrame.setLocationRelativeTo(frame);

        profileFrame.add(new JLabel("ID: " + rider.getId()));
        profileFrame.add(new JLabel("Name: " + rider.getName()));
        profileFrame.add(new JLabel("Email: " + (rider.getEmail() != null ? rider.getEmail() : "N/A")));
        profileFrame.add(new JLabel("Phone: " + (rider.getPhoneNumber() != null ? rider.getPhoneNumber() : "N/A")));
        profileFrame.add(new JLabel("Address: " + (rider.getAddress() != null ? rider.getAddress() : "N/A")));

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> profileFrame.dispose());
        profileFrame.add(closeBtn);

        profileFrame.setVisible(true);
    }

    // ---------------- Book Ride GUI ----------------
    private void bookRide() {
        if (Database.rides.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No rides available.");
            return;
        }

        JFrame bookFrame = new JFrame("Book a Ride");
        bookFrame.setSize(500, 400);
        bookFrame.setLayout(null);
        bookFrame.setLocationRelativeTo(frame);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setBounds(20, 20, 80, 25);
        JTextField fromField = new JTextField();
        fromField.setBounds(100, 20, 150, 25);

        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(20, 60, 80, 25);
        JTextField toField = new JTextField();
        toField.setBounds(100, 60, 150, 25);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(270, 60, 100, 30);

        DefaultListModel<Ride> rideListModel = new DefaultListModel<>();
        JList<Ride> rideJList = new JList<>(rideListModel);
        rideJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(rideJList);
        scrollPane.setBounds(20, 120, 450, 150);

        JButton bookSelectedBtn = new JButton("Book Selected Ride");
        bookSelectedBtn.setBounds(150, 290, 180, 30);

        bookFrame.add(fromLabel);
        bookFrame.add(fromField);
        bookFrame.add(toLabel);
        bookFrame.add(toField);
        bookFrame.add(searchBtn);
        bookFrame.add(scrollPane);
        bookFrame.add(bookSelectedBtn);

        bookFrame.setVisible(true);

        // ---------------- Search Action ----------------
        searchBtn.addActionListener(e -> {
            rideListModel.clear();
            String from = fromField.getText().trim().toLowerCase();
            String to = toField.getText().trim().toLowerCase();

            for (Ride ride : Database.rides) {
                boolean matchesFrom = from.isEmpty() || ride.getPickupLocation().toLowerCase().contains(from);
                boolean matchesTo = to.isEmpty() || ride.getDropoffLocation().toLowerCase().contains(to);

                if (matchesFrom && matchesTo) {
                    rideListModel.addElement(ride);
                }
            }

            if (rideListModel.isEmpty()) {
                JOptionPane.showMessageDialog(bookFrame, "No rides match your search.");
            }
        });

        // ---------------- Book Selected Ride ----------------
        bookSelectedBtn.addActionListener(e -> {
            Ride selectedRide = rideJList.getSelectedValue();
            if (selectedRide == null) {
                JOptionPane.showMessageDialog(bookFrame, "Select a ride to book.");
                return;
            }

            Payment payment = new Payment();
            payment.setAmount(selectedRide.getFareEstimate());
            payment.setTransactionStatus("Pending");

            mainController.riderController.makePayment(payment);
            Booking booking = mainController.riderController.createBooking(selectedRide, 1, payment);
            if (booking != null) {
                booking.setRider(rider);  // assign rider to booking
                booking.setRide(selectedRide); // assign ride to booking
                mainController.notificationController.sendNotification(rider,
                        "Ride booked from " + selectedRide.getPickupLocation() + " to " + selectedRide.getDropoffLocation());
                JOptionPane.showMessageDialog(bookFrame, "Ride booked successfully!");
                bookFrame.dispose();
            }
        });
        Database.saveAll();
    }

    // ---------------- Ride History ----------------
    private void showRideHistory() {
        List<Booking> bookings = new ArrayList<>();
        for (Booking b : Database.bookings) {
            if (b.getRider() != null && b.getRider().equals(rider)) bookings.add(b);
        }

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No ride history available.");
            return;
        }

        JFrame historyFrame = new JFrame("Ride History");
        historyFrame.setSize(500, 400);
        historyFrame.setLayout(new GridLayout(0, 1, 5, 5));
        historyFrame.setLocationRelativeTo(frame);

        for (Booking b : bookings) {
            Ride r = b.getRide();
            historyFrame.add(new JLabel(
                    "Booking ID: " + b.getId() +
                            " | From: " + r.getPickupLocation() +
                            " | To: " + r.getDropoffLocation() +
                            " | Status: " + b.getStatus()
            ));
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> historyFrame.dispose());
        historyFrame.add(closeBtn);

        historyFrame.setVisible(true);
    }

    // ---------------- Cancel Booking ----------------
    private void cancelBooking() {
        List<Booking> bookings = new ArrayList<>();
        for (Booking b : Database.bookings) {
            if (b.getRider() != null && b.getRider().equals(rider) && !b.getStatus().equalsIgnoreCase("Cancelled")) {
                bookings.add(b);
            }
        }

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No bookings available to cancel.");
            return;
        }

        String[] options = new String[bookings.size()];
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            Ride r = b.getRide();
            options[i] = "Booking ID: " + b.getId() +
                    " | From: " + r.getPickupLocation() +
                    " | To: " + r.getDropoffLocation() +
                    " | Status: " + b.getStatus();
        }

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Select the booking you want to cancel:",
                "Cancel Booking",
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
            Booking toCancel = bookings.get(selectedIndex);
            mainController.riderController.cancelBooking(toCancel.getId());
            // Optional: Remove ride completely if needed
            Database.rides.remove(toCancel.getRide());
            JOptionPane.showMessageDialog(frame, "Booking ID " + toCancel.getId() + " cancelled and ride deleted successfully.");
        }
        Database.saveAll();
    }

}
