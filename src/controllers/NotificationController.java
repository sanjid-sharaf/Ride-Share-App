package controllers;

import models.*;
import ui.*;

import java.util.Date;
import java.util.List;

public class NotificationController {

    public void sendNotification(User receiver, String message) {
        Notification notification = new Notification();
        notification.setId(Database.notifications.size() + 1);
        notification.setMessage(message);
        notification.setTimestamp(new Date());
        notification.setStatus("Unread");

        Database.notifications.add(notification);

        if (receiver instanceof Rider rider) {
            new RiderGUI(rider, null).displayMessage("Notification: " + message);
        } else if (receiver instanceof Driver driver) {
            new DriverGUI(driver, null).displayMessage("Notification: " + message);
        } else if (receiver instanceof Admin admin) {
            new AdminGUI(admin, null).displayMessage("Notification: " + message);
        }
    }

    public void markAsRead(int notificationId) {
        Notification n = Database.notifications.stream()
            .filter(notif -> notif.getId() == notificationId)
            .findFirst().orElse(null);
        if (n != null) n.setStatus("Read");
    }

    public List<Notification> getAllNotifications() {
        return Database.notifications;
    }
}
