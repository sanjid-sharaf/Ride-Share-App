package ui;

import javax.swing.*;
import controllers.*;
import models.*;

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
        profileBtn.addActionListener(e -> mainController.riderController.updateView());

        JButton bookRideBtn = new JButton("Book Ride");
        bookRideBtn.setBounds(50, 100, 150, 30);
        bookRideBtn.addActionListener(e -> {
            if (!Database.rides.isEmpty()) {
                Ride ride = Database.rides.get(0); // just first ride
                Payment payment = new Payment();
                payment.setAmount(ride.getFareEstimate());
                payment.setTransactionStatus("Pending");

                mainController.riderController.makePayment(payment);
                mainController.riderController.createBooking(ride, 1, payment);
                mainController.notificationController.sendNotification(rider,
                        "Ride booked from " + ride.getPickupLocation() + " to " + ride.getDropoffLocation());
            } else {
                JOptionPane.showMessageDialog(frame, "No rides available.");
            }
        });

        frame.add(profileBtn);
        frame.add(bookRideBtn);

        frame.setVisible(true);
    }
}
