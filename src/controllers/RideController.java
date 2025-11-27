package controllers;

import models.Ride;
import models.Booking;
import models.Database;
import views.RideView;

import java.util.Date;

public class RideController {

    private Ride model;
    private RideView view;

    public RideController(Ride model, RideView view) {
        this.model = model;
        this.view = view;
    }

    // --- Ride attributes ---
    public void setRideId(int id) { model.setId(id); }
    public int getRideId() { return model.getId(); }

    public void setRidePickupLocation(String pickup) { model.setPickupLocation(pickup); }
    public String getRidePickupLocation() { return model.getPickupLocation(); }

    public void setRideDropoffLocation(String dropoff) { model.setDropoffLocation(dropoff); }
    public String getRideDropoffLocation() { return model.getDropoffLocation(); }

    public void setRideDateTime(Date dateTime) { model.setDateTime(dateTime); }
    public Date getRideDateTime() { return model.getDateTime(); }

    public void setRideFareEstimate(double fare) { model.setFareEstimate(fare); }
    public double getRideFareEstimate() { return model.getFareEstimate(); }

    public void setRideStatus(String status) { model.setStatus(status); }
    public String getRideStatus() { return model.getStatus(); }

    // --- Booking management ---
    public Booking getBooking(int bookingIndex) {
        if (model.getBookings() != null && bookingIndex >= 0 && bookingIndex < model.getBookings().size()) {
            return model.getBookings().get(bookingIndex);
        }
        return null;
    }

    // --- Static helper methods for centralized database ---
    public static Ride getRideById(int rideId) {
        for (Ride ride : Database.rides) {
            if (ride.getId() == rideId) return ride;
        }
        return null;
    }

    public static void addRideToDatabase(Ride ride) {
        Database.rides.add(ride);
    }

    public static void removeRideFromDatabase(int rideId) {
        Ride ride = getRideById(rideId);
        if (ride != null) {
            Database.rides.remove(ride);
        }
    }

    // --- Update view ---
    public void updateView() {
        view.displayRideDetails(model);
    }
}
