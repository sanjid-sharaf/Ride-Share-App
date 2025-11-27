package models;

import java.util.Date;

public class Payment {

    private int id;
    private double amount;              // Added as per class diagram
    private Date date;
    private String method;              // Changed from int → String
    private String transactionStatus;
    private String refundStatus;

    // --- Getters & Setters ---

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
}
