package tests;

import models.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ModelsTest {

    public static void main(String[] args) {
        System.out.println("--- Running Model Tests ---");
        testUser();
        testRider();
        testDriver();
        testAdmin();
        testRide();
        testWallet();
        testPayment();
        testBooking();
        testNotification();
        System.out.println("--- All Tests Completed ---");
    }

    // --- USER ---
    private static void testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("pass123");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Main St");

        if (user.getId() != 1) System.out.println("testUser failed: id");
        if (!user.getName().equals("Alice")) System.out.println("testUser failed: name");
        if (!user.getEmail().equals("alice@example.com")) System.out.println("testUser failed: email");
        if (!user.getPassword().equals("pass123")) System.out.println("testUser failed: password");
        if (!user.getPhoneNumber().equals("1234567890")) System.out.println("testUser failed: phone");
        if (!user.getAddress().equals("123 Main St")) System.out.println("testUser failed: address");
    }

    // --- RIDER ---
    private static void testRider() {
        Rider rider = new Rider();
        rider.setName("Bob");
        rider.getPreferredPickup().add("Downtown");
        rider.setWallet(new Wallet());

        Ride ride1 = new Ride();
        rider.addRideToHistory(ride1);

        if (!rider.getName().equals("Bob")) System.out.println("testRider failed: name");
        if (rider.getPreferredPickup().size() != 1) System.out.println("testRider failed: pickup");
        if (rider.getWallet() == null) System.out.println("testRider failed: wallet");
        if (rider.getRideHistory().size() != 1) System.out.println("testRider failed: rideHistory");
    }

    // --- DRIVER ---
    private static void testDriver() {
        Driver driver = new Driver();
        driver.setName("Charlie");
        driver.setLicenseNumber("ABC123");
        driver.setVehicleDetails("Toyota Camry");
        driver.addRideToHistory(new Ride());

        if (!driver.getName().equals("Charlie")) System.out.println("testDriver failed: name");
        if (!driver.getLicenseNumber().equals("ABC123")) System.out.println("testDriver failed: license");
        if (!driver.getVehicleDetails().equals("Toyota Camry")) System.out.println("testDriver failed: vehicle");
        if (driver.getRideHistory().size() != 1) System.out.println("testDriver failed: rideHistory");
    }

    // --- ADMIN ---
    private static void testAdmin() {
        Admin admin = new Admin();
        admin.setName("Dana");
        admin.setAdminLevel(1);
        admin.setAdminRole("SuperAdmin");

        if (!admin.getName().equals("Dana")) System.out.println("testAdmin failed: name");
        if (admin.getAdminLevel() != 1) System.out.println("testAdmin failed: level");
        if (!admin.getAdminRole().equals("SuperAdmin")) System.out.println("testAdmin failed: role");
    }

    // --- RIDE ---
    private static void testRide() {
        Ride ride = new Ride();
        ride.setPickupLocation("Mall");
        ride.setDropoffLocation("Airport");
        ride.setAvailableSeats(3);
        ride.setFareEstimate(25.0);

        Booking booking = new Booking();
        booking.setSeatCount(2);
        ride.addBooking(booking);

        if (!ride.getPickupLocation().equals("Mall")) System.out.println("testRide failed: pickup");
        if (!ride.getDropoffLocation().equals("Airport")) System.out.println("testRide failed: dropoff");
        if (ride.getAvailableSeats() != 1) System.out.println("testRide failed: seats after booking");
        if (ride.getBookings().size() != 1) System.out.println("testRide failed: bookings list");
    }

    // --- WALLET ---
    private static void testWallet() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);

        if (wallet.getBalance() != 100.0) System.out.println("testWallet failed: balance");
    }

    // --- PAYMENT ---
    private static void testPayment() {
        Payment payment = new Payment();
        payment.setAmount(50.0);
        payment.setTransactionStatus("Pending");

        if (payment.getAmount() != 50.0) System.out.println("testPayment failed: amount");
        if (!payment.getTransactionStatus().equals("Pending")) System.out.println("testPayment failed: status");
    }

    // --- BOOKING ---
    private static void testBooking() {
        Booking booking = new Booking();
        booking.setSeatCount(2);
        booking.setStatus("Confirmed");
        booking.setPayment(new Payment());

        if (booking.getSeatCount() != 2) System.out.println("testBooking failed: seats");
        if (!booking.getStatus().equals("Confirmed")) System.out.println("testBooking failed: status");
        if (booking.getPayment() == null) System.out.println("testBooking failed: payment");
    }

    // --- NOTIFICATION ---
    private static void testNotification() {
        Notification notification = new Notification();
        notification.setMessage("Ride confirmed");
        notification.setStatus("Unread");
        notification.setTimestamp(new Date());

        if (!notification.getMessage().equals("Ride confirmed")) System.out.println("testNotification failed: message");
        if (!notification.getStatus().equals("Unread")) System.out.println("testNotification failed: status");
        if (notification.getTimestamp() == null) System.out.println("testNotification failed: timestamp");
    }
}

