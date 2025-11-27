package views;

import models.Ride;
import models.Booking;
import java.util.List;

public class RideView {

    public void displayRideDetails(Ride ride) {
        System.out.println("Ride Details:");
        System.out.println("Ride ID: " + ride.getId());
        System.out.println("Pickup Location: " + ride.getPickupLocation());
        System.out.println("Dropoff Location: " + ride.getDropoffLocation());
        System.out.println("Date/Time: " + ride.getDateTime());
        System.out.println("Available Seats: " + ride.getAvailableSeats());
        System.out.println("Fare Estimate: $" + ride.getFareEstimate());
        System.out.println("Status: " + ride.getStatus());
        System.out.println("Driver: " + (ride.getDriver() != null ? ride.getDriver().getLicenseNumber() : "N/A"));

        List<Booking> bookings = ride.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            System.out.println("Bookings:");
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getId() + ", Seats: " + booking.getSeatCount() +
                                   ", Status: " + booking.getStatus() + ", Payment Status: " + booking.getPaymentStatus());
            }
        }
    }
}
