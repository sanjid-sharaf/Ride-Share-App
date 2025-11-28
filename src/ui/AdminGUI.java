package ui;

import javax.swing.*;
import models.*;
import controllers.*;

public class AdminGUI {

    private Admin admin;
    private MainController mainController;

    private JFrame frame;
    private JTextArea outputArea;

    public AdminGUI(Admin admin, MainController mainController) {
        this.admin = admin;
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Admin Dashboard - " + admin.getName());
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---------------- Buttons ----------------
        JButton viewUsersBtn = new JButton("View User");
        viewUsersBtn.setBounds(50, 50, 150, 30);
        viewUsersBtn.addActionListener(e -> viewUser());

        JButton deleteUserBtn = new JButton("Delete User");
        deleteUserBtn.setBounds(220, 50, 150, 30);
        deleteUserBtn.addActionListener(e -> deleteUser());

        JButton viewProfileBtn = new JButton("View Profile");
        viewProfileBtn.setBounds(50, 100, 150, 30);
        viewProfileBtn.addActionListener(e -> updateAdminProfile());

        JButton notificationsBtn = new JButton("Notifications");
        notificationsBtn.setBounds(220, 100, 150, 30);
        notificationsBtn.addActionListener(e -> showNotifications());

        // ---------------- Output Area ----------------
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBounds(50, 150, 380, 180);

        // ---------------- Add Components ----------------
        frame.add(viewUsersBtn);
        frame.add(deleteUserBtn);
        frame.add(viewProfileBtn);
        frame.add(notificationsBtn);
        frame.add(scroll);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------------- Action Methods ----------------
    private void viewUser() {
        if (mainController.adminController == null) {
            displayMessage("AdminController not initialized.");
            return;
        }

        String input = JOptionPane.showInputDialog(frame, "Enter User ID:");
        if (input != null) {
            try {
                int userId = Integer.parseInt(input);
                mainController.adminController.viewUser(userId);
            } catch (NumberFormatException ex) {
                displayMessage("Invalid input.");
            }
        }
    }

    private void deleteUser() {
        if (mainController.adminController == null) {
            displayMessage("AdminController not initialized.");
            return;
        }

        String input = JOptionPane.showInputDialog(frame, "Enter User ID to delete:");
        if (input != null) {
            try {
                int userId = Integer.parseInt(input);
                mainController.adminController.deleteUser(userId);
                displayMessage("Delete operation completed for User ID: " + userId);
            } catch (NumberFormatException ex) {
                displayMessage("Invalid input.");
            }
        }
    }

    private void updateAdminProfile() {
        if (mainController.adminController == null) {
            displayMessage("AdminController not initialized.");
            return;
        }

        mainController.adminController.updateView();
    }

    // ---------------- Display Methods ----------------
    public void displayMessage(String msg) {
        if (outputArea != null) {
            outputArea.append(msg + "\n");
        }
    }

    public void displayAdminInfo(Admin admin) {
        displayMessage("Admin: " + admin.getName() + ", Role: " + admin.getAdminRole());
        displayMessage("Admin Level: " + admin.getAdminLevel());
    }

    public void displayUserInfo(User user) {
        displayMessage("User ID: " + user.getId() +
                       ", Name: " + user.getName() +
                       ", Email: " + user.getEmail() +
                       ", Phone: " + user.getPhoneNumber() +
                       ", Address: " + user.getAddress());
    }

    // ---------------- Notifications ----------------
    private void showNotifications() {
        if (mainController.notificationController == null) {
            displayMessage("NotificationController not initialized.");
            return;
        }

        var notifications = Database.notifications.stream()
            .filter(n -> n.getStatus().equals("Unread"))
            .toList();

        if (notifications.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No new notifications.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Notification n : notifications) {
            sb.append("ID: ").append(n.getId())
              .append(" - ").append(n.getMessage())
              .append("\n");
            n.setStatus("Read");
        }

        JOptionPane.showMessageDialog(frame, sb.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }
}
