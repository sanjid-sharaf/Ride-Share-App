package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {
    public static HashMap<Integer, User> users = new HashMap<>();
    public static List<Ride> rides = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Notification> notifications = new ArrayList<>();
}
