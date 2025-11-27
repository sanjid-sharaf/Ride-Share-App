package views;

import models.Admin;

public class AdminView {

    public void displayAdminProfile(Admin admin) {
        System.out.println("Admin Profile:");
        System.out.println("Admin Level: " + admin.getAdminLevel());
        System.out.println("Admin Role: " + admin.getAdminRole());
    }
}
