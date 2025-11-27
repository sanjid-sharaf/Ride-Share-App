package models;

import java.util.ArrayList;
import java.util.List;

public class Rider extends User {

    private List<String> preferredPickup;
    private Wallet wallet;            // Wallet for payments
    private List<Ride> rideHistory;   // Rides the rider has taken

    public Rider() {
        preferredPickup = new ArrayList<>();
        rideHistory = new ArrayList<>();
        wallet = new Wallet();        // Initialize a wallet for the rider
    }

    // --- Preferred Pickup Locations ---
    public List<String> getPreferredPickup() { return preferredPickup; }
    public void setPreferredPickup(List<String> preferredPickup) { this.preferredPickup = preferredPickup; }

    // --- Wallet ---
    public Wallet getWallet() { return wallet; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }

    // --- Ride History ---
    public List<Ride> getRideHistory() { return rideHistory; }
    public void addRideToHistory(Ride ride) { rideHistory.add(ride); }
}
