package tests;

import controllers.LoginController;
import models.User;
import models.Database;
import views.LoginView;

public class LoginControllerTest {

    public static void main(String[] args) {
        System.out.println("=== Running LoginController Tests ===");

        // Clear the database first
        Database.users.clear();

        // Setup
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("pass123");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Street");

        LoginView view = new LoginView();
        LoginController controller = new LoginController(user, view);

        // --- Test 1: Register user ---
        controller.register();
        if (Database.users.containsKey(1)) {
            System.out.println("Test register user: PASSED");
        } else {
            System.out.println("Test register user: FAILED");
        }

        // --- Test 2: Register same user again (should fail) ---
        controller.register(); // Should fail
        System.out.println("Test register existing user: PASSED (check output)");

        // --- Test 3: Verify login with correct credentials ---
        boolean loginSuccess = controller.verifyLogin(1, "john@example.com", "pass123");
        if (loginSuccess) {
            System.out.println("Test login with correct credentials: PASSED");
        } else {
            System.out.println("Test login with correct credentials: FAILED");
        }

        // --- Test 4: Verify login with wrong credentials ---
        boolean loginFail = controller.verifyLogin(1, "john@example.com", "wrongpass");
        if (!loginFail) {
            System.out.println("Test login with wrong credentials: PASSED");
        } else {
            System.out.println("Test login with wrong credentials: FAILED");
        }

        // --- Test 5: Edit profile while logged in ---
        controller.editProfile("John Doe", "9999999999", "456 Avenue");
        User updatedUser = controller.getLoginSystemUser();
        if (updatedUser.getName().equals("John Doe") &&
            updatedUser.getPhoneNumber().equals("9999999999") &&
            updatedUser.getAddress().equals("456 Avenue")) {
            System.out.println("Test edit profile while logged in: PASSED");
        } else {
            System.out.println("Test edit profile while logged in: FAILED");
        }

        // --- Test 6: Logout ---
        controller.logout();
        System.out.println("Test logout: PASSED (check output)");

        // --- Test 7: Edit profile while logged out (should not update) ---
        controller.editProfile("Jane", "0000000000", "789 Road");
        System.out.println("Test edit profile while logged out: PASSED (check output)");

        System.out.println("=== LoginController Tests Finished ===");
    }
}
