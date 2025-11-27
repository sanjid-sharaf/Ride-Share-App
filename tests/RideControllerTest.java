package tests;

import controllers.RideController;
import models.Ride;
import models.Booking;
import models.Database;
import views.RideView;

import java.util.Date;

public class RideControllerTest {

    public static void main(String[] args) {
        System.out.println("=== Running RideController Tests ===");

        // Clear the database first
        Database.rides.clear();

        // Setup
        Ride ride = new Ride();
        RideController controller = new RideController(ride, new RideView());

        // --- Test 1: Setters and Getters ---
        ride.setId(101);
        controller.setRideId(101);
        controller.setRidePickupLocation("Downtown");
        controller.setRideDropoffLocation("Airport");
        controller.setRideFareEstimate(25.0);
        controller.setRideStatus("Scheduled");
        controller.setRideDateTime(new Date());

        boolean test1 = controller.getRideId() == 101 &&
                        controller.getRidePickupLocation().equals("Downtown") &&
                        controller.getRideDropoffLocation().equals("Airport") &&
                        controller.getRideFareEstimate() == 25.0 &&
                        controller.getRideStatus().equals("Scheduled") &&
                        controller.getRideDateTime() != null;

        System.out.println("Test 1 Passed: Setters/Getters " + (test1 ? "✔" : "✘"));

        // --- Test 2: Add and get ride from Database ---
        RideController.addRideToDatabase(ride);
        Ride rideFromDb = RideController.getRideById(101);
        System.out.println("Test 2 Passed: Add/Get Ride from Database " +
                ((rideFromDb != null && rideFromDb.getId() == 101) ? "✔" : "✘"));

        // --- Test 3: Remove ride from Database ---
        RideController.removeRideFromDatabase(101);
        boolean removed = RideController.getRideById(101) == null;
        System.out.println("Test 3 Passed: Remove Ride from Database " + (removed ? "✔" : "✘"));

        // --- Test 4: Get Booking ---
        Booking booking = new Booking();
        ride.getBookings().add(booking); // add booking to ride
        Booking retrievedBooking = controller.getBooking(0);
        System.out.println("Test 4 Passed: Get Booking " + (retrievedBooking != null ? "✔" : "✘"));

        // --- Test 5: Update View ---
        try {
            controller.updateView();
            System.out.println("Test 5 Passed: Update View executed ✔ (check console output)");
        } catch (Exception e) {
            System.out.println("Test 5 Failed: Update View threw exception ✘");
        }

        System.out.println("=== RideController Tests Finished ===");
    }
}
