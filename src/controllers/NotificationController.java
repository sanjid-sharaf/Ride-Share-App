package controllers;

import models.Notification;
import models.User;
import models.Database;
import views.RiderView;
import views.DriverView;
import views.AdminView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationController {

    public NotificationController() {
    }

    // --- Send a notification ---
    public void sendNotification(User receiver, String message) {
        Notification notification = new Notification();

        // SAFE ID generation
        int newId = Database.notifications.stream()
                            .mapToInt(Notification::getId)
                            .max()
                            .orElse(0) + 1;

        notification.setId(newId);
        notification.setRecipient(receiver);   // set recipient
        notification.setMessage(message);
        notification.setTimestamp(new Date());
        notification.setStatus("Unread");

        Database.notifications.add(notification);

        System.out.println("Notification sent to " + receiver.getName() + ": " + message);
    }

    // --- Mark a notification as read ---
    public void markAsRead(int notificationId) {
        for (Notification notification : Database.notifications) {
            if (notification.getId() == notificationId) {
                notification.setStatus("Read");
                System.out.println("Notification " + notificationId + " marked as read.");
                return;
            }
        }
        System.out.println("Notification not found.");
    }

    // --- Get all notifications for a user ---
    public List<Notification> getUserNotifications(User user, boolean onlyUnread) {
        return Database.notifications.stream()
                .filter(n -> n.getRecipient() != null && n.getRecipient().equals(user))
                .filter(n -> !onlyUnread || n.getStatus().equalsIgnoreCase("Unread"))
                .collect(Collectors.toList());
    }

    // --- Update view ---
    public void updateView(User receiver, Object view) {
        List<Notification> userNotifications = getUserNotifications(receiver, false);

        System.out.println("Notifications for " + receiver.getName() + ":");
        for (Notification notification : userNotifications) {
            System.out.println("ID: " + notification.getId() +
                               ", Message: " + notification.getMessage() +
                               ", Status: " + notification.getStatus() +
                               ", Time: " + notification.getTimestamp());
        }

        // Update GUI view if applicable
        if (view instanceof RiderView && receiver instanceof models.Rider) {
            ((RiderView) view).displayRiderProfile((models.Rider) receiver);
        } else if (view instanceof DriverView && receiver instanceof models.Driver) {
            ((DriverView) view).displayDriverProfile((models.Driver) receiver);
        } else if (view instanceof AdminView && receiver instanceof models.Admin) {
            ((AdminView) view).displayAdminProfile((models.Admin) receiver);
        }
    }

    // --- Get all notifications ---
    public List<Notification> getAllNotifications() {
        return Database.notifications;
    }
}
