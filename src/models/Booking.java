package models;

import java.util.Date;

public class Booking {

    private int id;
    private Date date;
    private int seatCount;
    private String status;
    private String paymentStatus;
    private Payment payment;   // composition: each booking has a payment

    // --- Getters & Setters ---

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public Date getDate() { 
        return date; 
    }
    public void setDate(Date date) { 
        this.date = date; 
    }

    public int getSeatCount() { 
        return seatCount; 
    }
    public void setSeatCount(int seatCount) { 
        this.seatCount = seatCount; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getPaymentStatus() { 
        return paymentStatus; 
    }
    public void setPaymentStatus(String paymentStatus) { 
        this.paymentStatus = paymentStatus; 
    }

    public Payment getPayment() { 
        return payment; 
    }
    public void setPayment(Payment payment) { 
        this.payment = payment; 
    }
}
