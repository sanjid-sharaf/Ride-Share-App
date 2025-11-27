package tests;

import controllers.RiderController;
import models.*;
import views.RiderView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RiderControllerTest {

    public static void main(String[] args) {
        System.out.println("=== Running RiderController Tests ===");

        // Clear database first
        Database.rides.clear();
        Database.bookings.clear();

        // Setup rider and controller
        Rider rider = new Rider();
        rider.setId(1);
        rider.setName("Alice");
        rider.getWallet().setBalance(100.0);

        RiderController controller = new RiderController(rider, new RiderView());

        // --- Test 1: Make payment ---
        Payment payment1 = new Payment();
        payment1.setAmount(50.0);
        controller.makePayment(payment1);
        boolean test1 = payment1.getTransactionStatus().equals("Success") &&
                        rider.getWallet().getBalance() == 50.0;
        System.out.println("Test 1 Passed: Make payment ✔ " + test1);

        // --- Test 2: Fail payment due to insufficient balance ---
        Payment payment2 = new Payment();
        payment2.setAmount(100.0);
        controller.makePayment(payment2);
        boolean test2 = payment2.getTransactionStatus().equals("Failed");
        System.out.println("Test 2 Passed: Insufficient balance ✔ " + test2);

        // --- Test 3: Create booking ---
        Ride ride = new Ride();
        ride.setId(101);
        ride.setPickupLocation("Downtown");
        ride.setDropoffLocation("Airport");
        ride.setAvailableSeats(3);
        ride.setDateTime(new Date());
        Database.rides.add(ride);

        Payment payment3 = new Payment();
        payment3.setAmount(25.0);
        Booking booking = controller.createBooking(ride, 2, payment3);
        boolean test3 = booking != null &&
                        ride.getBookings().contains(booking) &&
                        rider.getRideHistory().contains(ride);
        System.out.println("Test 3 Passed: Create booking ✔ " + test3);

        // --- Test 4: Cancel booking ---
        controller.cancelBooking(booking.getId());
        boolean test4 = booking.getStatus().equals("Cancelled");
        System.out.println("Test 4 Passed: Cancel booking ✔ " + test4);

        // --- Test 5: Filter rides ---
        List<Ride> filteredRides = controller.filterRides("Downtown", "Airport", new Date());
        boolean test5 = filteredRides.contains(ride);
        System.out.println("Test 5 Passed: Filter rides ✔ " + test5);

        System.out.println("=== RiderController Tests Finished ===");
    }
}
