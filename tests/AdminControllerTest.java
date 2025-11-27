package tests;

import controllers.AdminController;
import models.Admin;
import models.User;
import models.Database;
import views.AdminView;

public class AdminControllerTest {

    public static void main(String[] args) {
        System.out.println("=== Running AdminController Tests ===");

        // Setup
        Database.users.clear();

        Admin admin = new Admin();
        admin.setAdminLevel(1);
        admin.setAdminRole("ADMIN");
        AdminView view = new AdminView();

        AdminController controller = new AdminController(admin, view);

        // Add a sample user
        User user = new User();
        user.setId(10);
        user.setName("John");
        user.setEmail("john@email.com");
        user.setPhoneNumber("12345");
        user.setAddress("Toronto");
        Database.users.put(10, user);

        // 1️⃣ Test viewing existing user
        User found = controller.viewUser(10);
        if (found != null && found.getName().equals("John")) {
            System.out.println("Test viewUser exists: PASSED");
        } else {
            System.out.println("Test viewUser exists: FAILED");
        }

        // 2️⃣ Test viewing non-existing user
        User notFound = controller.viewUser(99);
        if (notFound == null) {
            System.out.println("Test viewUser not found: PASSED");
        } else {
            System.out.println("Test viewUser not found: FAILED");
        }

        // 3️⃣ Test updating user profile
        controller.updateUserProfile(10, "Mike", "99999", "New City");
        User updated = Database.users.get(10);
        if (updated.getName().equals("Mike") &&
            updated.getPhoneNumber().equals("99999") &&
            updated.getAddress().equals("New City")) {
            System.out.println("Test updateUserProfile: PASSED");
        } else {
            System.out.println("Test updateUserProfile: FAILED");
        }

        // 4️⃣ Test deleting user
        controller.deleteUser(10);
        if (!Database.users.containsKey(10)) {
            System.out.println("Test deleteUser: PASSED");
        } else {
            System.out.println("Test deleteUser: FAILED");
        }

        // 5️⃣ Test setting and getting admin role
        controller.setAdminRole("SUPER_ADMIN");
        if (controller.getAdminRole().equals("SUPER_ADMIN")) {
            System.out.println("Test set/get adminRole: PASSED");
        } else {
            System.out.println("Test set/get adminRole: FAILED");
        }

        System.out.println("=== AdminController Tests Finished ===");
    }
}
