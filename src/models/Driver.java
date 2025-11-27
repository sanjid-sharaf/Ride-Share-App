package models;

import java.util.List;

public class Driver {

    private String licenseNumber;
    private String vehicleDetails;
    private Wallet wallet;
    private List<Ride> rideHistory;

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public Wallet getWallet() { return wallet; }

    public List<Ride> getRideHistory() { return rideHistory; }

    // Entity responsibilities only:
    public void addRideToHistory(Ride ride) {
        rideHistory.add(ride);
    }
}
