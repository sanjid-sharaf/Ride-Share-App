package controllers;

import models.*;
import ui.DriverGUI;

import java.util.List;

public class DriverController {

    private Driver model;
    private DriverGUI view;

    public DriverController(Driver model, DriverGUI view) {
        this.model = model;
        this.view = view;
    }

    public void updateView() {
        view.displayDriverInfo(model);
    }

    public void createRide(Ride ride) {
        Database.rides.add(ride);
        model.addRideToHistory(ride);
        view.displayMessage("Ride created: " + ride.getPickupLocation() + " -> " + ride.getDropoffLocation());
    }

    public void cancelRide(int rideId) {
        Ride ride = Database.rides.stream()
            .filter(r -> r.getId() == rideId && r.getDriver() == model)
            .findFirst().orElse(null);

        if (ride != null) {
            ride.setStatus("Cancelled");
            view.displayMessage("Ride " + rideId + " cancelled.");
        } else {
            view.displayMessage("Ride not found or not yours.");
        }
    }
}
