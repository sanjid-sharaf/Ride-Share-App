package tests;

import models.*;

import java.util.Date;

public class ModelsTest {

    public static void main(String[] args) {
        System.out.println("--- Running Model Tests ---\n");
        testUser();
        testRider();
        testDriver();
        testAdmin();
        testRide();
        testWallet();
        testPayment();
        testBooking();
        testNotification();
        System.out.println("\n--- All Tests Completed ---");
    }

    // --- USER ---
    private static void testUser() {
        System.out.println("Testing User...");
        User user = new User();
        user.setId(1);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("pass123");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Main St");

        assertTest("ID", user.getId() == 1);
        assertTest("Name", user.getName().equals("Alice"));
        assertTest("Email", user.getEmail().equals("alice@example.com"));
        assertTest("Password", user.getPassword().equals("pass123"));
        assertTest("Phone", user.getPhoneNumber().equals("1234567890"));
        assertTest("Address", user.getAddress().equals("123 Main St"));
        System.out.println();
    }

    // --- RIDER ---
    private static void testRider() {
        System.out.println("Testing Rider...");
        Rider rider = new Rider();
        rider.setName("Bob");
        rider.getPreferredPickup().add("Downtown");
        rider.setWallet(new Wallet());

        Ride ride1 = new Ride();
        rider.addRideToHistory(ride1);

        assertTest("Name", rider.getName().equals("Bob"));
        assertTest("Preferred Pickup", rider.getPreferredPickup().size() == 1);
        assertTest("Wallet not null", rider.getWallet() != null);
        assertTest("Ride History", rider.getRideHistory().size() == 1);
        System.out.println();
    }

    // --- DRIVER ---
    private static void testDriver() {
        System.out.println("Testing Driver...");
        Driver driver = new Driver();
        driver.setName("Charlie");
        driver.setLicenseNumber("ABC123");
        driver.setVehicleDetails("Toyota Camry");
        driver.addRideToHistory(new Ride());

        assertTest("Name", driver.getName().equals("Charlie"));
        assertTest("License Number", driver.getLicenseNumber().equals("ABC123"));
        assertTest("Vehicle Details", driver.getVehicleDetails().equals("Toyota Camry"));
        assertTest("Ride History", driver.getRideHistory().size() == 1);
        System.out.println();
    }

    // --- ADMIN ---
    private static void testAdmin() {
        System.out.println("Testing Admin...");
        Admin admin = new Admin();
        admin.setName("Dana");
        admin.setAdminLevel(1);
        admin.setAdminRole("SuperAdmin");

        assertTest("Name", admin.getName().equals("Dana"));
        assertTest("Admin Level", admin.getAdminLevel() == 1);
        assertTest("Admin Role", admin.getAdminRole().equals("SuperAdmin"));
        System.out.println();
    }

    // --- RIDE ---
    private static void testRide() {
        System.out.println("Testing Ride...");
        Ride ride = new Ride();
        ride.setPickupLocation("Mall");
        ride.setDropoffLocation("Airport");
        ride.setAvailableSeats(3);
        ride.setFareEstimate(25.0);

        Booking booking = new Booking();
        booking.setSeatCount(2);
        ride.addBooking(booking);

        assertTest("Pickup Location", ride.getPickupLocation().equals("Mall"));
        assertTest("Dropoff Location", ride.getDropoffLocation().equals("Airport"));
        assertTest("Available Seats after booking", ride.getAvailableSeats() == 1);
        assertTest("Bookings List Size", ride.getBookings().size() == 1);
        System.out.println();
    }

    // --- WALLET ---
    private static void testWallet() {
        System.out.println("Testing Wallet...");
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);

        assertTest("Balance", wallet.getBalance() == 100.0);
        System.out.println();
    }

    // --- PAYMENT ---
    private static void testPayment() {
        System.out.println("Testing Payment...");
        Payment payment = new Payment();
        payment.setAmount(50.0);
        payment.setTransactionStatus("Pending");

        assertTest("Amount", payment.getAmount() == 50.0);
        assertTest("Transaction Status", payment.getTransactionStatus().equals("Pending"));
        System.out.println();
    }

    // --- BOOKING ---
    private static void testBooking() {
        System.out.println("Testing Booking...");
        Booking booking = new Booking();
        booking.setSeatCount(2);
        booking.setStatus("Confirmed");
        booking.setPayment(new Payment());

        assertTest("Seat Count", booking.getSeatCount() == 2);
        assertTest("Status", booking.getStatus().equals("Confirmed"));
        assertTest("Payment not null", booking.getPayment() != null);
        System.out.println();
    }

    // --- NOTIFICATION ---
    private static void testNotification() {
        System.out.println("Testing Notification...");
        Notification notification = new Notification();
        notification.setMessage("Ride confirmed");
        notification.setStatus("Unread");
        notification.setTimestamp(new Date());

        assertTest("Message", notification.getMessage().equals("Ride confirmed"));
        assertTest("Status", notification.getStatus().equals("Unread"));
        assertTest("Timestamp not null", notification.getTimestamp() != null);
        System.out.println();
    }

    // --- Helper method to print PASS/FAIL ---
    private static void assertTest(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
        } else {
            System.out.println("FAIL: " + testName);
        }
    }
}
