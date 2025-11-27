package models;

import java.util.Date;

public class Booking {

    private int id;
    private Date date;
    private String status;
    private String paymentStatus;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public Date getDate(){ return date; }
    public void setDate(Date date){ this.date = date; }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }

    public String getPaymentStatus(){ return paymentStatus; }
    public void setPaymentStatus(String paymentStatus){ this.paymentStatus = paymentStatus; }
}
