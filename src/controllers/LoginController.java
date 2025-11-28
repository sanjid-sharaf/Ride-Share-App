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

    // ---------- User actions ----------

    public void register() {
        if (model != null && !Database.users.containsKey(model.getId())) {
            Database.users.put(model.getId(), model);
            loggedIn = true;
            view.displayLoginResult(true);
        } else {
            view.displayLoginResult(false);
        }
    }

    public boolean verifyLogin(String name, String password) {
        for (User u : Database.users.values()) {
            if (u.getName().equalsIgnoreCase(name) && u.getPassword().equals(password)) {
                this.model = u;
                loggedIn = true;
                view.displayLoginResult(true);
                return true;
            }
        }
        view.displayLoginResult(false);
        return false;
    }

    public void logout() {
        loggedIn = false;
        view.displayMessage("User logged out successfully.");
    }

    public void editProfile(String name, String phoneNumber, String address) {
        if (!loggedIn) {
            view.displayMessage("Please login first.");
            return;
        }
        model.setName(name);
        model.setPhoneNumber(phoneNumber);
        model.setAddress(address);
        view.displayMessage("Profile updated successfully.");
    }

    public User viewProfile() {
        if (!loggedIn) {
            view.displayMessage("Please login first.");
            return null;
        }
        updateView();
        return model;
    }

    public void updateView() {
        view.printUserDetails(model);
    }

    // Access the current user
    public void setLoginSystemUser(User user) { this.model = user; }
    public User getLoginSystemUser() { return this.model; }

}
