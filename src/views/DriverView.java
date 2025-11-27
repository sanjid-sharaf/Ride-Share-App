package views;

import models.Driver;
import models.Ride;
import java.util.List;

public class DriverView {

    public void displayDriverProfile(Driver driver) {
        System.out.println("Driver Profile:");
        System.out.println("License Number: " + driver.getLicenseNumber());
        System.out.println("Vehicle Details: " + driver.getVehicleDetails());
        System.out.println("Wallet Balance: $" + driver.getWallet().getBalance());

        System.out.println("Ride History:");
        List<Ride> rides = driver.getRideHistory();
        if (rides.isEmpty()) {
            System.out.println("No rides yet.");
        } else {
            for (Ride ride : rides) {
                System.out.println("Ride ID: " + ride.getId() + ", From: " + ride.getPickupLocation() +
                                   " To: " + ride.getDropoffLocation() + ", Status: " + ride.getStatus());
            }
        }
    }
}
