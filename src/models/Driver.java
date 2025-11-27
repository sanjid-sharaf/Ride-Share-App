package models;

public class Driver {

    private String licenseNumber;
    private String vehicleDetails;

    public String getLicenseNumber(){ return licenseNumber; }
    public void setLicenseNumber(String licenseNumber){ this.licenseNumber = licenseNumber; }

    public String getVehicleDetails(){ return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails){ this.vehicleDetails = vehicleDetails; }
}
