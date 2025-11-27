package controllers;

import models.Admin;
import models.User;
import models.Database;
import views.AdminView;

public class AdminController {

    private Admin model;
    private AdminView view;

    public AdminController(Admin model, AdminView view) {
        this.model = model;
        this.view = view;
    }

    // --- Admin attributes ---
    public void setAdminLevel(int level) { model.setAdminLevel(level); }
    public int getAdminLevel() { return model.getAdminLevel(); }

    public void setAdminRole(String role) { model.setAdminRole(role); }
    public String getAdminRole() { return model.getAdminRole(); }

    // --- User management ---
    public void setUserId(int userId) {
        // This could be used to select a user for admin operations
    }

    public int getUserId() {
        return 0;
    }

    // View a user
    public User viewUser(int userId) {
        User user = Database.users.get(userId);  // <-- Use centralized Database
        if (user != null) {
            System.out.println("User found:");
            System.out.println("ID: " + user.getId());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone: " + user.getPhoneNumber());
            System.out.println("Address: " + user.getAddress());
        } else {
            System.out.println("User not found.");
        }
        return user;
    }

    // Update a user's profile
    public void updateUserProfile(int userId, String name, String phone, String address) {
        User user = Database.users.get(userId);  // <-- Use Database
        if (user != null) {
            user.setName(name);
            user.setPhoneNumber(phone);
            user.setAddress(address);
            System.out.println("User profile updated.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Suspend a user
    public void suspendUser(int userId) {
        User user = Database.users.get(userId);  // <-- Use Database
        if (user != null) {
            System.out.println("User " + userId + " suspended."); // Could add a status field later
        } else {
            System.out.println("User not found.");
        }
    }

    // Delete a user
    public void deleteUser(int userId) {
        if (Database.users.remove(userId) != null) {  // <-- Use Database
            System.out.println("User " + userId + " deleted.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Update admin view
    public void updateView() {
        view.displayAdminProfile(model);
    }
}
