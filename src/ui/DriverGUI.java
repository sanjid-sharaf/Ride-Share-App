package ui;

import javax.swing.*;
import models.*;
import controllers.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
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
                viewNotifications()
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
// ---------------- View Notifications ----------------
    private void viewNotifications() {
        // Fetch notifications for this driver
        java.util.List<Notification> driverNotifs = mainController.notificationController.getUserNotifications(driver, true); // should return all unread + read

        if (driverNotifs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No notifications.");
            return;
        }

        // Create popup
        JFrame notifFrame = new JFrame("Notifications for " + driver.getName());
        notifFrame.setSize(500, 400);
        notifFrame.setLayout(new BorderLayout());
        notifFrame.setLocationRelativeTo(frame);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Notification n : driverNotifs) {
            String status = n.getStatus();
            String msg = "[" + status + "] " + n.getMessage() + " | " + n.getTimestamp();
            listModel.addElement(msg);
        }

        JList<String> notifList = new JList<>(listModel);
        notifList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(notifList);

        JPanel buttonPanel = new JPanel();
        JButton closeBtn = new JButton("Close");
        JButton markReadBtn = new JButton("Mark All as Read");

        buttonPanel.add(markReadBtn);
        buttonPanel.add(closeBtn);

        notifFrame.add(scrollPane, BorderLayout.CENTER);
        notifFrame.add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- Actions ----------------
        closeBtn.addActionListener(e -> notifFrame.dispose());

        markReadBtn.addActionListener(e -> {
            for (Notification n : driverNotifs) {
                mainController.notificationController.markAsRead(n.getId());
            }
            Database.saveAll(); // persist changes
            JOptionPane.showMessageDialog(notifFrame, "All notifications marked as read.");
            notifFrame.dispose();
        });

        notifFrame.setVisible(true);
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

    private void addRideDialog() {
        JFrame addRideFrame = new JFrame("Add Ride");
        addRideFrame.setSize(400, 400);
        addRideFrame.setLayout(new GridLayout(0, 2, 5, 5));
        addRideFrame.setLocationRelativeTo(frame);

        // Input fields
        JTextField pickupField = new JTextField();
        JTextField dropoffField = new JTextField();
        JTextField seatsField = new JTextField();
        JTextField fareField = new JTextField();

        // Separate date & time fields
        JTextField dayField = new JTextField();
        JTextField monthField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField hourField = new JTextField();
        JTextField minuteField = new JTextField();

        addRideFrame.add(new JLabel("Pickup Location:"));
        addRideFrame.add(pickupField);
        addRideFrame.add(new JLabel("Dropoff Location:"));
        addRideFrame.add(dropoffField);
        addRideFrame.add(new JLabel("Number of Seats:"));
        addRideFrame.add(seatsField);
        addRideFrame.add(new JLabel("Fare:"));
        addRideFrame.add(fareField);

        addRideFrame.add(new JLabel("Day:"));
        addRideFrame.add(dayField);
        addRideFrame.add(new JLabel("Month:"));
        addRideFrame.add(monthField);
        addRideFrame.add(new JLabel("Year:"));
        addRideFrame.add(yearField);
        addRideFrame.add(new JLabel("Hour (0-23):"));
        addRideFrame.add(hourField);
        addRideFrame.add(new JLabel("Minute (0-59):"));
        addRideFrame.add(minuteField);

        JButton addBtn = new JButton("Add Ride");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> {
            String pickup = pickupField.getText().trim();
            String dropoff = dropoffField.getText().trim();
            int seats;
            double fare;
            int day, month, year, hour, minute;
            java.util.Date rideDate;

            // Validate non-empty pickup/dropoff
            if (pickup.isEmpty() || dropoff.isEmpty()) {
                JOptionPane.showMessageDialog(addRideFrame, "Pickup and Dropoff cannot be empty.");
                return;
            }

            // Validate seats and fare
            try {
                seats = Integer.parseInt(seatsField.getText().trim());
                fare = Double.parseDouble(fareField.getText().trim());
                if (seats <= 0 || fare < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addRideFrame, "Invalid input for seats or fare.");
                return;
            }

            // Validate date/time fields
            try {
                day = Integer.parseInt(dayField.getText().trim());
                month = Integer.parseInt(monthField.getText().trim());
                year = Integer.parseInt(yearField.getText().trim());
                hour = Integer.parseInt(hourField.getText().trim());
                minute = Integer.parseInt(minuteField.getText().trim());

                Calendar cal = Calendar.getInstance();
                cal.setLenient(false);
                cal.set(year, month - 1, day, hour, minute, 0); // month is 0-based
                rideDate = cal.getTime(); // Throws exception if invalid
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addRideFrame, "Invalid date or time values.");
                return;
            }

            // Create ride
            Ride ride = new Ride();
            ride.setPickupLocation(pickup);
            ride.setDropoffLocation(dropoff);
            ride.setAvailableSeats(seats);
            ride.setFareEstimate(fare);
            ride.setDriver(driver);
            ride.setDateTime(rideDate);

            // Add ride via controller
            mainController.driverController.createRide(ride);

            JOptionPane.showMessageDialog(addRideFrame, "Ride added successfully!");
            addRideFrame.dispose();
            Database.saveAll();
        });

        backBtn.addActionListener(e -> addRideFrame.dispose());

        addRideFrame.add(addBtn);
        addRideFrame.add(backBtn);

        addRideFrame.setVisible(true);
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
