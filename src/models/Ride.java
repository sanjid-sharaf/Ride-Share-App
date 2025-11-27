package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ride {

    private int id;
    private String pickupLocation;
    private String dropoffLocation;
    private Date dateTime;
    private int availableSeats;
    private double fareEstimate;
    private String status; // e.g., "Scheduled", "Ongoing", "Completed"
    private Driver driver;
    private List<Booking> bookings;

    public Ride() {
        bookings = new ArrayList<>();
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public double getFareEstimate() { return fareEstimate; }
    public void setFareEstimate(double fareEstimate) { this.fareEstimate = fareEstimate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public List<Booking> getBookings() { return bookings; }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        availableSeats -= booking.getSeatCount(); // Update available seats
    }
}
