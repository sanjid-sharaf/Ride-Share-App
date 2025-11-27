package controllers;

import models.User;
import models.Database;
import views.LoginView;

public class LoginController {

    private User model;
    private LoginView view;
    private boolean loggedIn = false;

    public LoginController(User model, LoginView view) {
        this.model = model;
        this.view = view;
    }

    // --- Getters and Setters for User attributes ---
    public void setUserId(int id) { model.setId(id); }
    public int getUserId() { return model.getId(); }

    public void setUserName(String name) { model.setName(name); }
    public String getUserName() { return model.getName(); }

    public void setUserEmail(String email) { model.setEmail(email); }
    public String getUserEmail() { return model.getEmail(); }

    public void setUserPassword(String password) { model.setPassword(password); }
    public String getUserPassword() { return model.getPassword(); }

    public void setUserPhoneNumber(String phone) { model.setPhoneNumber(phone); }
    public String getUserPhoneNumber() { return model.getPhoneNumber(); }

    public void setUserAddress(String address) { model.setAddress(address); }
    public String getUserAddress() { return model.getAddress(); }

    public void setLoginSystemUser(User user) { this.model = user; }
    public User getLoginSystemUser() { return this.model; }

    // --- User actions ---

    // Register a new user
    public void register() {
        if (model != null && !Database.users.containsKey(model.getId())) {
            Database.users.put(model.getId(), model);  // <-- Use centralized Database
            loggedIn = true;
            view.displayLoginResult(true);
        } else {
            view.displayLoginResult(false);
        }
    }

    // Verify login credentials
    public boolean verifyLogin(int userId, String email, String password) {
        User user = Database.users.get(userId);  // <-- Use centralized Database
        if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
            this.model = user;
            loggedIn = true;
            view.displayLoginResult(true);
            return true;
        } else {
            view.displayLoginResult(false);
            return false;
        }
    }

    // Logout the user
    public void logout() {
        loggedIn = false;
        System.out.println("User logged out successfully.");
    }

    // Edit profile
    public void editProfile(String name, String phoneNumber, String address) {
        if (!loggedIn) {
            System.out.println("Please login first.");
            return;
        }
        model.setName(name);
        model.setPhoneNumber(phoneNumber);
        model.setAddress(address);
        System.out.println("Profile updated successfully.");
    }

    // View profile
    public User viewProfile() {
        if (!loggedIn) {
            System.out.println("Please login first.");
            return null;
        }
        updateView();
        return model;
    }

    // Update view
    public void updateView() {
        view.printUserDetails(model);
    }
}
