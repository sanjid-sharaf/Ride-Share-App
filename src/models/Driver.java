package models;

import java.util.List;
import java.util.ArrayList;

public class Driver extends User {

    private String licenseNumber;
    private String vehicleDetails;
    private Wallet wallet;
    private List<Ride> rideHistory;

    // Constructor to initialize rideHistory
    public Driver() {
        this.rideHistory = new ArrayList<>();
        this.wallet = new Wallet(); // optional, if you want a ready-to-use wallet
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public Wallet getWallet() { return wallet; }

    public List<Ride> getRideHistory() { return rideHistory; }

    public void addRideToHistory(Ride ride) {
        rideHistory.add(ride);
    }
}
