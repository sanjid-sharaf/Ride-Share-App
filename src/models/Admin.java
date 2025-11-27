package models;

public class Admin extends User{

    private int adminLevel;
    private String adminRole;

    public int getAdminLevel(){ return adminLevel; }
    public void setAdminLevel(int adminLevel){ this.adminLevel = adminLevel; }

    public String getAdminRole(){ return adminRole; }
    public void setAdminRole(String adminRole){ this.adminRole = adminRole; }
}
