package models;

import java.util.Date;

public class Booking {

    private int id;
    private Date date;
    private int seatCount;
    private String status;          // e.g., "Booked", "Cancelled"
    private String paymentStatus;   // e.g., "Success", "Failed"
    private Payment payment;        // each booking has a payment
    private Rider rider;            // the rider who booked
    private Ride ride;              // the ride being booked

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public Rider getRider() { return rider; }
    public void setRider(Rider rider) { this.rider = rider; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }
}
