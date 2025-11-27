package views;

import models.Rider;
import models.Ride;
import java.util.List;

public class RiderView {

    public void displayRiderProfile(Rider rider) {
    if (rider == null) {
        System.out.println("Rider Profile: Not available (rider is null).");
        return;
    }

    System.out.println("Rider Profile:");
    System.out.println("Preferred Pickup Locations: " + rider.getPreferredPickup());

    if (rider.getWallet() != null) {
        System.out.println("Wallet Balance: $" + rider.getWallet().getBalance());
    } else {
        System.out.println("Wallet: No wallet assigned.");
    }

    System.out.println("Ride History:");
    List<Ride> rides = rider.getRideHistory();
    if (rides == null || rides.isEmpty()) {
        System.out.println("No rides yet.");
    } else {
        for (Ride ride : rides) {
            System.out.println(
                "Ride ID: " + ride.getId() +
                ", From: " + ride.getPickupLocation() +
                " To: " + ride.getDropoffLocation() +
                ", Status: " + ride.getStatus()
            );
        }
    }
}
}