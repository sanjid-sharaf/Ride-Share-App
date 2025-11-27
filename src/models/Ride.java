package models;

import java.util.Date;

public class Ride {

    private int id;
    private String pickupLocation;
    private String dropoffLocation;
    private Date dateTime;
    private double fareEstimate;
    private String status;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getPickupLocation(){ return pickupLocation; }
    public void setPickupLocation(String pickupLocation){ this.pickupLocation = pickupLocation; }

    public String getDropoffLocation(){ return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation){ this.dropoffLocation = dropoffLocation; }

    public Date getDateTime(){ return dateTime; }
    public void setDateTime(Date dateTime){ this.dateTime = dateTime; }

    public double getFareEstimate(){ return fareEstimate; }
    public void setFareEstimate(double fareEstimate){ this.fareEstimate = fareEstimate; }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
}
