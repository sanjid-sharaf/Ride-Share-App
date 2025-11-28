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

        // --- Load data from SQLite ---
        System.out.println("Loading data from SQLite...");
        Database.loadAll();
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

        System.out.println("All sub-controllers initialized successfully.");
    }

    // --- Run a simple demo using database data ---
    public void runDemo() {
        System.out.println("\n--- Demo: Login and Create Booking ---");

        // Login the first user in the database
        if (!Database.users.isEmpty()) {
            User user = Database.users.values().iterator().next();
            loginController.verifyLogin(user.getName(), user.getPassword());
            loginController.updateView();

            System.out.println("Logged in as: " + user.getName());

            // If user is Rider, book first available ride
            if (user instanceof Rider rider && !Database.rides.isEmpty()) {
                Ride ride = Database.rides.get(0);

                Payment payment = new Payment();
                payment.setAmount(ride.getFareEstimate());
                payment.setTransactionStatus("Pending");

                riderController.makePayment(payment);
                riderController.createBooking(ride, 1, payment);

                System.out.println("Created booking for ride: " + ride.getPickupLocation() + " -> " + ride.getDropoffLocation());

                riderController.updateView();

                // Send a notification
                notificationController.sendNotification(rider, "Your ride is confirmed!");
                notificationController.updateView(rider, riderView);
            }
        } else {
            System.out.println("No users found in database.");
        }

        // Save changes back to SQLite
        shutdown();
    }

    // --- Save all data back to SQLite ---
    public void shutdown() {
        System.out.println("\nSaving data back to SQLite...");
        Database.saveAll();
        System.out.println("Database saved successfully.");
    }

    // --- Main entry for testing ---
    public static void main(String[] args) {
        MainController app = new MainController();
        app.runDemo();
    }
}
