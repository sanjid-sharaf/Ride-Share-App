package controllers;

import models.*;
import ui.*;

public class MainController {

    // Sub-controllers
    public LoginController loginController;
    public RiderController riderController;
    public DriverController driverController;
    public AdminController adminController;
    public RideController rideController;
    public NotificationController notificationController;

    // Current logged-in user
    private User currentUser;

    // GUIs
    private LoginGUI loginGUI;
    private RiderGUI riderGUI;
    private DriverGUI driverGUI;
    private AdminGUI adminGUI;

    public MainController() {
        // Load database first
        System.out.println("Loading data from SQLite...");
        Database.loadAll();

        // Initialize notification controller
        notificationController = new NotificationController();

        // Initialize LoginController (no GUI yet)
        loginController = new LoginController(this);

        // Show login GUI
        loginGUI = new LoginGUI(this);
        loginGUI.showFrame();

        System.out.println("Login GUI displayed.");
    }

    // Called by LoginController after successful login
    public void loginSuccess(User user) {
        this.currentUser = user;

        System.out.println("Logged in as: " + user.getName() + " (" + user.getClass().getSimpleName() + ")");

        // Launch appropriate GUI based on user type
        if (user instanceof Rider rider) {
            riderGUI = new RiderGUI(rider, this);
            riderController = new RiderController(rider, riderGUI);
            riderGUI.showFrame();

        } else if (user instanceof Driver driver) {
            driverGUI = new DriverGUI(driver, this);
            driverController = new DriverController(driver, driverGUI);
            driverGUI.showFrame();

        } else if (user instanceof Admin admin) {
            adminGUI = new AdminGUI(admin, this);
            adminController = new AdminController(admin, adminGUI);
            adminGUI.showFrame();
        }

        // Close login window
        if (loginGUI != null) loginGUI.close();
    }

    // Called by LoginController to display login messages
    public void showLoginMessage(String msg) {
        if (loginGUI != null) loginGUI.displayText(msg);
    }

    // Optional: shutdown method
    public void shutdown() {
        System.out.println("\nSaving data back to SQLite...");
        Database.saveAll();
        System.out.println("Database saved successfully.");
    }

    public void showLoginGUI() {
        if (loginGUI == null) loginGUI = new LoginGUI(this);
        loginGUI.showFrame();
    }

    public static void main(String[] args) {
        new MainController();
    }
}
