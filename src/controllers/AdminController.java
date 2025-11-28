package controllers;

import models.*;
import ui.AdminGUI;

public class AdminController {

    private Admin model;
    private AdminGUI view;

    public AdminController(Admin model, AdminGUI view) {
        this.model = model;
        this.view = view;
    }

    public void updateView() {
        view.displayAdminInfo(model);
    }

    public void viewUser(int userId) {
        User user = Database.users.get(userId);
        if (user != null) {
            view.displayUserInfo(user);
        } else {
            view.displayMessage("User not found.");
        }
    }

    public void updateUserProfile(int userId, String name, String phone, String address) {
        User user = Database.users.get(userId);
        if (user != null) {
            user.setName(name);
            user.setPhoneNumber(phone);
            user.setAddress(address);
            view.displayMessage("User profile updated.");
        } else {
            view.displayMessage("User not found.");
        }
    }

    public void deleteUser(int userId) {
        if (Database.users.remove(userId) != null) {
            view.displayMessage("User deleted.");
        } else {
            view.displayMessage("User not found.");
        }
    }
}
