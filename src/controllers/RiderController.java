package controllers;

import models.Rider;
import models.Ride;
import models.Booking;
import models.Payment;
import models.Wallet;
import models.Database;
import views.RiderView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Notification;

public class RiderController {

    private Rider model;
    private RiderView view;

    public RiderView getView() { return this.view; }

    public RiderController(Rider model, RiderView view) {
        this.model = model;
        this.view = view;
    }

    // --- Preferred Pickup Locations ---
    public void setRiderPreferredPickup(List<String> locations) { model.setPreferredPickup(locations); }
    public List<String> getRiderPreferredPickup() { return model.getPreferredPickup(); }

    // --- Wallet and Ride History ---
    public Wallet getWallet() { return model.getWallet(); }
    public List<Ride> getRideHistory() { return model.getRideHistory(); }

    // --- Update View ---
    public void updateView() { view.displayRiderProfile(model); }

    // --- Booking and Payments ---
    public Booking createBooking(Ride ride, int seatCount, Payment payment) {
        if (ride.getAvailableSeats() < seatCount) {
            System.out.println("Not enough seats available.");
            return null;
        }

        Booking booking = new Booking();

        // SAFE ID generation
        int newId = Database.bookings.stream()
                        .mapToInt(Booking::getId)
                        .max()
                        .orElse(0) + 1;

        booking.setId(newId);
        booking.setDate(new Date());
        booking.setSeatCount(seatCount);
        booking.setStatus("Booked");
        booking.setPayment(payment);
        booking.setPaymentStatus(payment.getTransactionStatus());

        // CRITICAL — set rider
        booking.setRider(model);

        // Add to DB memory
        Database.bookings.add(booking);

        // Maintain relationship
        booking.setRide(ride);
        ride.addBooking(booking);

        System.out.println("Booking created successfully! Booking ID: " + booking.getId());

        // --- Notify driver ---
        if (ride.getDriver() != null) {
            Notification notif = new Notification();
            notif.setId(Database.notifications.stream().mapToInt(Notification::getId).max().orElse(0) + 1);
            notif.setRecipient(ride.getDriver());
            notif.setMessage("New booking created for your ride (ID: " + ride.getId() + ") by " + model.getName() + ".");
            notif.setStatus("Unread");
            notif.setTimestamp(new Date());
            Database.notifications.add(notif);
        }

        return booking;
    }
    
    public void makePayment(Payment payment) {
        Wallet wallet = model.getWallet();
        if (wallet.getBalance() >= payment.getAmount()) {
            wallet.setBalance(wallet.getBalance() - payment.getAmount());
            payment.setTransactionStatus("Success");
            System.out.println("Payment successful: $" + payment.getAmount());
        } else {
            payment.setTransactionStatus("Failed");
            System.out.println("Payment failed: insufficient balance.");
        }
    }

    // --- Filter rides ---
    public List<Ride> filterRides(String source, String destination, Date time) {
        List<Ride> filtered = new ArrayList<>();
        for (Ride ride : Database.rides) { // Use centralized Database
            if (ride.getPickupLocation().equalsIgnoreCase(source) &&
                ride.getDropoffLocation().equalsIgnoreCase(destination) &&
                ride.getDateTime().compareTo(time) >= 0) {
                filtered.add(ride);
            }
        }
        return filtered;
    }

    // --- Cancel Booking ---
    public void cancelBooking(int bookingId) {
        Booking bookingToCancel = null;
        for (Booking booking : Database.bookings) { // Use centralized bookings
            if (booking.getId() == bookingId) {
                bookingToCancel = booking;
                break;
            }
        }
        if (bookingToCancel != null) {
            bookingToCancel.setStatus("Cancelled");
            System.out.println("Booking " + bookingId + " cancelled.");

            // --- Notify driver ---
            Ride ride = bookingToCancel.getRide();
            if (ride != null && ride.getDriver() != null) {
                Notification notif = new Notification();
                notif.setId(Database.notifications.stream().mapToInt(Notification::getId).max().orElse(0) + 1);
                notif.setRecipient(ride.getDriver());
                notif.setMessage("Booking (ID: " + bookingId + ") for your ride (ID: " + ride.getId() + ") has been cancelled by " + model.getName() + ".");
                notif.setStatus("Unread");
                notif.setTimestamp(new Date());
                Database.notifications.add(notif);
            }

        } else {
            System.out.println("Booking not found.");
        }
    }
}
