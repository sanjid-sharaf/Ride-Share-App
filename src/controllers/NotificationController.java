package controllers;

import models.Notification;
import models.User;
import models.Database;
import views.RiderView;
import views.DriverView;
import views.AdminView;

import java.util.Date;
import java.util.List;

public class NotificationController {

    public NotificationController() {
        // No local list needed — all notifications are in Database.notifications
    }

    // --- Send a notification ---
    public void sendNotification(User receiver, String message) {
        Notification notification = new Notification();
        notification.setId(Database.notifications.size() + 1);
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

    // --- Update view ---
    // Depending on user type, we can display notifications differently
    public void updateView(User receiver, Object view) {
        System.out.println("Notifications for " + receiver.getName() + ":");
        for (Notification notification : Database.notifications) {
            System.out.println("ID: " + notification.getId() +
                               ", Message: " + notification.getMessage() +
                               ", Status: " + notification.getStatus() +
                               ", Time: " + notification.getTimestamp());
        }

        if (view instanceof RiderView && receiver instanceof models.Rider) {
    ((RiderView) view).displayRiderProfile((models.Rider) receiver);
    }
    else if (view instanceof DriverView && receiver instanceof models.Driver) {
        ((DriverView) view).displayDriverProfile((models.Driver) receiver);
    }
    else if (view instanceof AdminView && receiver instanceof models.Admin) {
        ((AdminView) view).displayAdminProfile((models.Admin) receiver);
    }
}

    // --- Get all notifications ---
    public List<Notification> getAllNotifications() {
        return Database.notifications;
    }
}
