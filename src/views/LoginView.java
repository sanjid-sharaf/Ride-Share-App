package views;

import models.User;

public class LoginView {

    public void displayLoginResult(boolean success) {
        if (success) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Check your credentials.");
        }
    }

    public void printUserDetails(User user) {
        System.out.println("User Profile:");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhoneNumber());
        System.out.println("Address: " + user.getAddress());
    }
}
