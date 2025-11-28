package controllers;

import models.*;
import ui.RiderGUI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RiderController {

    private Rider model;
    private RiderGUI view;

    public RiderController(Rider model, RiderGUI view) {
        this.model = model;
        this.view = view;
    }

    public Rider getRider() { return model;}

    public void updateView() {
        view.displayRiderInfo(model);
    }

    public void makePayment(Payment payment) {
        Wallet wallet = model.getWallet();
        if (wallet.getBalance() >= payment.getAmount()) {
            wallet.setBalance(wallet.getBalance() - payment.getAmount());
            payment.setTransactionStatus("Success");
            view.displayMessage("Payment successful: $" + payment.getAmount());
        } else {
            payment.setTransactionStatus("Failed");
            view.displayMessage("Payment failed: insufficient balance.");
        }
    }

    public Booking createBooking(Ride ride, int seatCount, Payment payment) {
        if (ride.getAvailableSeats() < seatCount) {
            view.displayMessage("Not enough seats available.");
            return null;
        }

        Booking booking = new Booking();
        booking.setId(Database.bookings.size() + 1);
        booking.setDate(new Date());
        booking.setSeatCount(seatCount);
        booking.setStatus("Booked");
        booking.setPayment(payment);
        booking.setPaymentStatus(payment.getTransactionStatus());

        Database.bookings.add(booking);
        ride.addBooking(booking);
        model.addRideToHistory(ride);

        view.displayMessage("Booking created successfully! Booking ID: " + booking.getId());
        return booking;
    }

    public void cancelBooking(int bookingId) {
        Booking target = Database.bookings.stream()
            .filter(b -> b.getId() == bookingId)
            .findFirst().orElse(null);

        if (target != null) {
            target.setStatus("Cancelled");
            view.displayMessage("Booking " + bookingId + " cancelled.");
        } else {
            view.displayMessage("Booking not found.");
        }
    }

    public List<Ride> filterRides(String source, String destination, Date time) {
        List<Ride> filtered = new ArrayList<>();
        for (Ride r : Database.rides) {
            if (r.getPickupLocation().equalsIgnoreCase(source)
             && r.getDropoffLocation().equalsIgnoreCase(destination)
             && r.getDateTime().compareTo(time) >= 0) {
                filtered.add(r);
            }
        }
        return filtered;
    }
}
