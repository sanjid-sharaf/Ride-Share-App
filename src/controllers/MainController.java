package controllers;

import models.*;
import views.*;

import java.util.Date;

public class MainController {

    // Sub-controllers
    public LoginController loginController;
    public RiderController riderController;
    public DriverController driverController;
    public AdminController adminController;
    public RideController rideController;
    public NotificationController notificationController;

    // Models and Views
    private User currentUser;
    private Rider currentRider;
    private Driver currentDriver;
    private Admin currentAdmin;
    private Ride currentRide;

    private LoginView loginView;
    private RiderView riderView;
    private DriverView driverView;
    private AdminView adminView;
    private RideView rideView;

    public MainController() {
        initializeSubControllers();
    }

    public void initializeSubControllers() {

        // --- Initialize Views ---
        loginView = new LoginView();
        riderView = new RiderView();
        driverView = new DriverView();
        adminView = new AdminView();
        rideView = new RideView();

        // --- Initialize Models ---
        currentUser = new User();
        currentRider = new Rider();
        currentDriver = new Driver();
        currentAdmin = new Admin();
        currentRide = new Ride();

        // --- Initialize Controllers ---
        loginController = new LoginController(currentUser, loginView);
        riderController = new RiderController(currentRider, riderView);
        driverController = new DriverController(currentDriver, driverView);
        adminController = new AdminController(currentAdmin, adminView);
        rideController = new RideController(currentRide, rideView);
        notificationController = new NotificationController();

        // --- Initialize Mock Data ---
        initMockData();

        System.out.println("All sub-controllers initialized successfully.");
    }

    // --- Initialize some mock users, rides, and payments ---
    private void initMockData() {
        // Users
        User user1 = new User();
        user1.setId(1);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setPassword("pass123");
        user1.setPhoneNumber("1234567890");
        user1.setAddress("123 Main St");
        Database.users.put(user1.getId(), user1);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setPassword("pass456");
        user2.setPhoneNumber("0987654321");
        user2.setAddress("456 Park Ave");
        Database.users.put(user2.getId(), user2);

        // Rides
        Ride ride1 = new Ride();
        ride1.setId(101);
        ride1.setPickupLocation("Downtown");
        ride1.setDropoffLocation("Airport");
        ride1.setDateTime(new Date());
        ride1.setAvailableSeats(3);
        ride1.setFareEstimate(25.0);
        ride1.setStatus("Scheduled");
        Database.rides.add(ride1);

        Ride ride2 = new Ride();
        ride2.setId(102);
        ride2.setPickupLocation("Mall");
        ride2.setDropoffLocation("Station");
        ride2.setDateTime(new Date());
        ride2.setAvailableSeats(2);
        ride2.setFareEstimate(15.0);
        ride2.setStatus("Scheduled");
        Database.rides.add(ride2);

        System.out.println("Mock data initialized.");
    }

    // --- Example method to simulate app flow ---
    public void runDemo() {
        System.out.println("\n--- Demo: Login and Create Booking ---");

        // Login user
        loginController.verifyLogin(1, "alice@example.com", "pass123");

        // View user profile
        loginController.updateView();

        // Rider books a ride
        if (!Database.rides.isEmpty()) {
            Ride ride = Database.rides.get(0);

            Payment payment = new Payment();
            payment.setAmount(ride.getFareEstimate());
            payment.setTransactionStatus("Pending");

            riderController.makePayment(payment);
            riderController.createBooking(ride, 2, payment);

            // View rider profile after booking
            riderController.updateView();
        }

        // Send notification
        notificationController.sendNotification(Database.users.get(1), "Your ride is confirmed!");
        notificationController.updateView(Database.users.get(1), riderView);
    }
}
