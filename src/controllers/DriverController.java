package controllers;

import models.Driver;
import models.Ride;
import models.Wallet;
import models.Database;
import views.DriverView;

import java.util.List;

public class DriverController {

    private Driver model;
    private DriverView view;

    public DriverController(Driver model, DriverView view) {
        this.model = model;
        this.view = view;
    }

    // --- Getters and Setters for Driver attributes ---
    public void setDriverLicenseNumber(String license) { model.setLicenseNumber(license); }
    public String getDriverLicenseNumber() { return model.getLicenseNumber(); }

    public void setDriverVehicleDetails(String vehicle) { model.setVehicleDetails(vehicle); }
    public String getDriverVehicleDetails() { return model.getVehicleDetails(); }

    // --- Wallet and Ride History ---
    public Wallet getWallet() { return model.getWallet(); }
    public List<Ride> getRideHistory() { return model.getRideHistory(); }

    // --- Ride Management ---

    // Create a new ride
    public void createRide(Ride ride) {
        Database.rides.add(ride);      // Add to centralized Database
        model.addRideToHistory(ride);  // Add to driver's personal ride history
        System.out.println("Ride created successfully: " + ride.getId());
    }

    // Cancel a ride
    public void cancelRide(int rideId) {
        Ride rideToCancel = null;
        for (Ride ride : Database.rides) {  // Use centralized Database
            if (ride.getId() == rideId && ride.getDriver() == model) {
                rideToCancel = ride;
                break;
            }
        }
        if (rideToCancel != null) {
            rideToCancel.setStatus("Cancelled");
            System.out.println("Ride " + rideId + " cancelled.");
        } else {
            System.out.println("Ride not found or does not belong to this driver.");
        }
    }

    // Update a ride
    public void updateRide(Ride updatedRide) {
        for (int i = 0; i < Database.rides.size(); i++) {
            Ride ride = Database.rides.get(i);
            if (ride.getId() == updatedRide.getId() && ride.getDriver() == model) {
                Database.rides.set(i, updatedRide);
                System.out.println("Ride " + updatedRide.getId() + " updated.");
                return;
            }
        }
        System.out.println("Ride not found or does not belong to this driver.");
    }

    // --- View Update ---
    public void updateView() {
        view.displayDriverProfile(model);
    }
}
