package controllers;

import models.User;
import models.Driver;
import models.Rider;
import models.Admin;
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

    // Existing methods
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

    // ✅ New method for GUI usage
    public User verifyLoginByName(String name, String password, String type) {
        for (User u : Database.users.values()) {
            boolean typeMatch = (type.equals("Rider") && u instanceof Rider)
                             || (type.equals("Driver") && u instanceof Driver)
                             || (type.equals("Admin") && u instanceof Admin);

            if (u.getName().equalsIgnoreCase(name) && u.getPassword().equals(password) && typeMatch) {
                this.model = u;
                loggedIn = true;
                if (view != null) view.displayLoginResult(true);
                return u;
            }
        }
        if (view != null) view.displayLoginResult(false);
        return null;
    }

    // Existing methods
    public void logout() {
        loggedIn = false;
        if (view != null) view.displayMessage("User logged out successfully.");
    }

    public void editProfile(String name, String phoneNumber, String address) {
        if (!loggedIn) {
            if (view != null) view.displayMessage("Please login first.");
            return;
        }
        model.setName(name);
        model.setPhoneNumber(phoneNumber);
        model.setAddress(address);
        if (view != null) view.displayMessage("Profile updated successfully.");
    }

    public User viewProfile() {
        if (!loggedIn) {
            if (view != null) view.displayMessage("Please login first.");
            return null;
        }
        updateView();
        return model;
    }

    public void updateView() {
        if (view != null) view.printUserDetails(model);
    }

    // Access the current user
    public void setLoginSystemUser(User user) { this.model = user; }
    public User getLoginSystemUser() { return this.model; }
}
