package controllers;

import models.User;
import models.Database;
import views.LoginView;

public class LoginController {

    private User model;
    private LoginView view;
    private boolean loggedIn = false;

    // Reference to your main GUI window
    private Object mainGUI;

    public LoginController(User model, LoginView view) {
        this.model = model;
        this.view = view;
    }

    // If your GUI class is ridegui, pass it here
    public void setMainGUI(Object gui) {
        this.mainGUI = gui;
    }

    // -------------------------
    // 🔥 ADD THIS — GUI HANDLERS
    // -------------------------
    public void attachHandlers(LoginView view) {
        view.loginButton.addActionListener(e -> {

            String email = view.emailField.getText();
            String pass = new String(view.passwordField.getPassword());

            boolean success = verifyLoginByEmail(email, pass);

            view.displayLoginResult(success);

            if (success && mainGUI != null) {
                try {
                    // Calls a method like ridegui.showDashboard();
                    mainGUI.getClass()
                           .getMethod("showDashboard")
                           .invoke(mainGUI);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // New helper: login by email + password
    private boolean verifyLoginByEmail(String email, String password) {
        for (User u : Database.users.values()) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                this.model = u;
                loggedIn = true;
                return true;
            }
        }
        return false;
    }

    // ---------- Your existing code below ----------

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

    public void register() {
        if (model != null && !Database.users.containsKey(model.getId())) {
            Database.users.put(model.getId(), model);
            loggedIn = true;
            view.displayLoginResult(true);
        } else {
            view.displayLoginResult(false);
        }
    }

    public boolean verifyLogin(int userId, String email, String password) {
        User user = Database.users.get(userId);
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

    public void logout() {
        loggedIn = false;
        System.out.println("User logged out successfully.");
    }

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

    public User viewProfile() {
        if (!loggedIn) {
            System.out.println("Please login first.");
            return null;
        }
        updateView();
        return model;
    }

    public void updateView() {
        view.printUserDetails(model);
    }
}
