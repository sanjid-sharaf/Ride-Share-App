package tests;

import controllers.DriverController;
import models.Driver;
import models.Ride;
import models.Database;
import views.DriverView;

import java.util.ArrayList;

public class DriverControllerTest {

    public static void main(String[] args) {
        System.out.println("=== Running DriverController Tests ===");

        // Setup
        Database.rides.clear();

        // Create driver
        Driver driver = new Driver();
        driver.setLicenseNumber("DL123");
        driver.setVehicleDetails("Toyota Camry");

        DriverView view = new DriverView();
        DriverController controller = new DriverController(driver, view);

        // --- Test 1: Get/Set Driver License ---
        controller.setDriverLicenseNumber("DL999");
        if (controller.getDriverLicenseNumber().equals("DL999")) {
            System.out.println("Test set/get Driver License: PASSED");
        } else {
            System.out.println("Test set/get Driver License: FAILED");
        }

        // --- Test 2: Get/Set Vehicle Details ---
        controller.setDriverVehicleDetails("Honda Civic");
        if (controller.getDriverVehicleDetails().equals("Honda Civic")) {
            System.out.println("Test set/get Vehicle Details: PASSED");
        } else {
            System.out.println("Test set/get Vehicle Details: FAILED");
        }

        // --- Test 3: Create Ride ---
        Ride ride1 = new Ride();
        ride1.setId(101);
        ride1.setDriver(driver);
        controller.createRide(ride1);
        if (Database.rides.contains(ride1) && driver.getRideHistory().contains(ride1)) {
            System.out.println("Test createRide: PASSED");
        } else {
            System.out.println("Test createRide: FAILED");
        }

        // --- Test 4: Cancel Ride ---
        controller.cancelRide(101);
        if (ride1.getStatus() != null && ride1.getStatus().equals("Cancelled")) {
            System.out.println("Test cancelRide: PASSED");
        } else {
            System.out.println("Test cancelRide: FAILED");
        }

        // --- Test 5: Update Ride ---
        Ride updatedRide = new Ride();
        updatedRide.setId(101);
        updatedRide.setDriver(driver);
        updatedRide.setStatus("Completed");
        controller.updateRide(updatedRide);

        Ride rideFromDB = Database.rides.stream().filter(r -> r.getId() == 101).findFirst().orElse(null);
        if (rideFromDB != null && "Completed".equals(rideFromDB.getStatus())) {
            System.out.println("Test updateRide: PASSED");
        } else {
            System.out.println("Test updateRide: FAILED");
        }

        System.out.println("=== DriverController Tests Finished ===");
    }
}
