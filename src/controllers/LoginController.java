package controllers;

import models.Database;
import models.User;

public class LoginController {

    private MainController mainController;

    public LoginController(MainController mainController) {
        this.mainController = mainController;
    }

    // Verify login credentials
    public void verifyLogin(String username, String password) {
        User foundUser = null;

        // Iterate through all users in the database
        if (Database.users != null) {
            for (User user : Database.users.values()) {
                if (user.getName().equalsIgnoreCase(username)) {
                    foundUser = user;
                    break;
                }
            }
        }

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            mainController.loginSuccess(foundUser);
        } else {
            mainController.showLoginMessage("Invalid username or password!");
        }
    }
}
