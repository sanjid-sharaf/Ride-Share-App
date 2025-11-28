package controllers;

import models.*;
import ui.RideGUI;

import java.util.Date;

public class RideController {

    private Ride model;
    private RideGUI gui;                     // GUI reference
    private MainController mainController;   // access to all subcontrollers

    public RideController(Ride model, MainController mainController) {
        this.model = model;
        this.mainController = mainController;
    }

    public void setGUI(RideGUI gui) {
        this.gui = gui;
    }

    // --- Ride attributes ---
    public int getRideId() { return model.getId(); }
    public String getRidePickupLocation() { return model.getPickupLocation(); }
    public String getRideDropoffLocation() { return model.getDropoffLocation(); }
    public Date getRideDateTime() { return model.getDateTime(); }
    public double getRideFareEstimate() { return model.getFareEstimate(); }
    public int getAvailableSeats() { return model.getAvailableSeats(); }
    public void setRideStatus(String status) { model.setStatus(status); }
    public String getRideStatus() { return model.getStatus(); }

    // --- Book ride ---
    public void bookRide(int seatCount) {
        RiderController riderController = mainController.riderController;
        NotificationController notificationController = mainController.notificationController;

        if (model.getAvailableSeats() < seatCount) {
            if (gui != null) gui.displayMessage("Not enough seats available.");
            return;
        }

        Payment payment = new Payment();
        payment.setAmount(model.getFareEstimate());
        payment.setTransactionStatus("Pending");

        riderController.makePayment(payment);
        Booking booking = riderController.createBooking(model, seatCount, payment);

        if (booking != null) {
            if (gui != null) gui.displayMessage("Booking successful! Booking ID: " + booking.getId());
            if (notificationController != null) {
                notificationController.sendNotification(
                    riderController.getRider(),
                    "Ride booked: " + model.getPickupLocation() + " -> " + model.getDropoffLocation()
                );
            }
        } else {
            if (gui != null) gui.displayMessage("Booking failed.");
        }

        if (gui != null) gui.updateRideDetails(); // refresh GUI
    }

    // --- Update GUI ---
    public void updateGUI() {
        if (gui != null) gui.updateRideDetails();
    }

    // --- Static DB helpers ---
    public static Ride getRideById(int rideId) {
        return Database.rides.stream().filter(r -> r.getId() == rideId).findFirst().orElse(null);
    }

    public static void addRideToDatabase(Ride ride) {
        Database.rides.add(ride);
    }

    public static void removeRideFromDatabase(int rideId) {
        Ride ride = getRideById(rideId);
        if (ride != null) Database.rides.remove(ride);
    }
}
