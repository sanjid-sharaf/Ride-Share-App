package views;

import models.User;
import ui.RideGUI;
import javax.swing.*;

public class LoginView {

    private RideGUI gui;  // reference to the main GUI

    // GUI setter
    public void setGUI(RideGUI gui) {
        this.gui = gui;
    }

    // Display login result
    public void displayLoginResult(boolean success) {
        if (gui == null) return;

        if (success) {
            gui.displayText("Login successful!");
        } else {
            gui.displayText("Login failed. Check your credentials.");
        }
    }

    // Display arbitrary messages on the UI
    public void displayMessage(String msg) {
        if (gui != null) {
            gui.displayText(msg);
        }
    }

    // Display user details
    public void printUserDetails(User user) {
        if (gui == null) return;

        gui.displayText("=== User Profile ===");
        gui.displayText("ID: " + user.getId());
        gui.displayText("Name: " + user.getName());
        gui.displayText("Email: " + user.getEmail());
        gui.displayText("Phone: " + user.getPhoneNumber());
        gui.displayText("Address: " + user.getAddress());
        gui.displayText("===================");
    }
}
