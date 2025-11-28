    package models;

    import java.sql.*;
    import java.util.*;
    import java.text.SimpleDateFormat;

    public class Database {

        public static HashMap<Integer, User> users = new HashMap<>();
        public static List<Ride> rides = new ArrayList<>();
        public static List<Booking> bookings = new ArrayList<>();
        public static List<Payment> payments = new ArrayList<>();
        public static List<Notification> notifications = new ArrayList<>();

        private static final String DB_URL = "jdbc:sqlite:rideshare.db";

        // ------------------ Load All ------------------
        public static void loadAll() {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                loadUsers(conn);
                loadWallets(conn);
                loadRides(conn);
                loadPayments(conn);
                loadBookings(conn);
                loadNotifications(conn);
                System.out.println("Database loaded into memory.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // ------------------ Save All ------------------
        public static void saveAll() {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                saveUsers(conn);
                saveWallets(conn);
                saveRides(conn);
                savePayments(conn);
                saveBookings(conn);
                saveNotifications(conn);
                System.out.println("Database saved to SQLite.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // ------------------ USERS ------------------
        private static void loadUsers(Connection conn) throws SQLException {
            users.clear();
            String sql = "SELECT * FROM users";
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String type = rs.getString("type");
                    User u;
                    switch (type) {
                        case "Driver" -> u = new Driver();
                        case "Rider" -> u = new Rider();
                        case "Admin" -> u = new Admin();
                        default -> u = new User();
                    }
                    u.setId(id);
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhoneNumber(rs.getString("phoneNumber"));
                    u.setAddress(rs.getString("address"));
                    u.setPassword(rs.getString("password"));
                    users.put(id, u);
                }
            }
        }

        private static void saveUsers(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM users");
            }
            String sql = "INSERT INTO users (id, name, email, phoneNumber, address, password, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (User u : users.values()) {
                    ps.setInt(1, u.getId());
                    ps.setString(2, u.getName());
                    ps.setString(3, u.getEmail());
                    ps.setString(4, u.getPhoneNumber());
                    ps.setString(5, u.getAddress());
                    ps.setString(6, u.getPassword());
                    if (u instanceof Driver) ps.setString(7, "Driver");
                    else if (u instanceof Rider) ps.setString(7, "Rider");
                    else if (u instanceof Admin) ps.setString(7, "Admin");
                    else ps.setString(7, "User");
                    ps.executeUpdate();
                }
            }
        }

        // ------------------ WALLETS ------------------
        private static void loadWallets(Connection conn) throws SQLException {
            String sql = "SELECT * FROM wallets";
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    double balance = rs.getDouble("balance");
                    int userId = rs.getInt("userId");
                    Wallet w = new Wallet();
                    w.setId(id);
                    w.setBalance(balance);
                    User u = users.get(userId);
                    if (u instanceof Rider r) r.setWallet(w);
                    else if (u instanceof Driver d) d.setWallet(w);
                }
            }
        }

        private static void saveWallets(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM wallets");
            }
            String sql = "INSERT INTO wallets (id, balance, userId) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (User u : users.values()) {
                    Wallet w = null;
                    if (u instanceof Rider r) w = r.getWallet();
                    else if (u instanceof Driver d) w = d.getWallet();
                    if (w != null) {
                        ps.setInt(1, w.getId());
                        ps.setDouble(2, w.getBalance());
                        ps.setInt(3, u.getId());
                        ps.executeUpdate();
                    }
                }
            }
        }
// ------------------ RIDES ------------------
        private static void loadRides(Connection conn) throws SQLException {
            rides.clear();
            String sql = "SELECT * FROM rides";
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Ride ride = new Ride();
                    ride.setId(rs.getInt("id"));
                    ride.setPickupLocation(rs.getString("pickupLocation"));
                    ride.setDropoffLocation(rs.getString("dropoffLocation"));

                    String dt = rs.getString("dateTime");
                    if (dt != null) {
                        try {
                            java.util.Date parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt);
                            ride.setDateTime(new Timestamp(parsedDate.getTime()));
                        } catch (Exception e) {
                            // fallback to current timestamp if parsing fails
                            ride.setDateTime(new Timestamp(System.currentTimeMillis()));
                        }
                    }

                    ride.setAvailableSeats(rs.getInt("availableSeats"));
                    ride.setFareEstimate(rs.getDouble("fareEstimate"));
                    ride.setStatus(rs.getString("status"));
                    int driverId = rs.getInt("driverId");
                    User drv = users.get(driverId);
                    if (drv instanceof Driver dr) ride.setDriver(dr);
                    rides.add(ride);
                }
            }
        }

        private static void saveRides(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM rides");
            }

            String sql = "INSERT INTO rides (id, pickupLocation, dropoffLocation, dateTime, availableSeats, fareEstimate, status, driverId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for (Ride r : rides) {
                    ps.setInt(1, r.getId());
                    ps.setString(2, r.getPickupLocation());
                    ps.setString(3, r.getDropoffLocation());

                    if (r.getDateTime() != null) {
                        String formattedDate = sdf.format(r.getDateTime());
                        ps.setString(4, formattedDate);
                    } else {
                        ps.setString(4, null);
                    }

                    ps.setInt(5, r.getAvailableSeats());
                    ps.setDouble(6, r.getFareEstimate());
                    ps.setString(7, r.getStatus());
                    ps.setInt(8, r.getDriver() != null ? r.getDriver().getId() : 0);
                    ps.executeUpdate();
                }
            }
        }


        // ------------------ PAYMENTS ------------------
        private static void loadPayments(Connection conn) throws SQLException {
            payments.clear();
            String sql = "SELECT * FROM payments";
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Payment p = new Payment();
                    p.setId(rs.getInt("id"));
                    p.setAmount(rs.getDouble("amount"));
                    p.setMethod(rs.getString("method"));
                    String dt = rs.getString("date");
                    if (dt != null) p.setDate(Timestamp.valueOf(dt));
                    p.setTransactionStatus(rs.getString("transactionStatus"));
                    p.setRefundStatus(rs.getString("refundStatus"));
                    payments.add(p);
                }
            }
        }

        private static void savePayments(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM payments");
            }
            String sql = "INSERT INTO payments (id, amount, date, method, transactionStatus, refundStatus) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Payment p : payments) {
                    ps.setInt(1, p.getId());
                    ps.setDouble(2, p.getAmount());
                    ps.setString(3, p.getDate() != null ? p.getDate().toString() : null);
                    ps.setString(4, p.getMethod());
                    ps.setString(5, p.getTransactionStatus());
                    ps.setString(6, p.getRefundStatus());
                    ps.executeUpdate();
                }
            }
        }

            // ------------------ BOOKINGS ------------------
        private static void loadBookings(Connection conn) throws SQLException {
            bookings.clear();
            String sql = "SELECT * FROM bookings";
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setId(rs.getInt("id"));
                    b.setSeatCount(rs.getInt("seatCount"));
                    b.setStatus(rs.getString("status"));
                    b.setPaymentStatus(rs.getString("paymentStatus"));

                    // Set rider
                    int riderId = rs.getInt("riderId");
                    User rider = users.get(riderId);
                    if (rider instanceof Rider r) b.setRider(r);

                    // Set ride
                    int rideId = rs.getInt("rideId");
                    Ride ride = rides.stream().filter(r -> r.getId() == rideId).findFirst().orElse(null);
                    if (ride != null) {
                        b.setRide(ride);
                        ride.addBooking(b);
                    }

                    // Set payment
                    int paymentId = rs.getInt("paymentId");
                    Payment p = payments.stream().filter(pay -> pay.getId() == paymentId).findFirst().orElse(null);
                    b.setPayment(p);

                    // Set date
                    String dateStr = rs.getString("date");
                    if (dateStr != null) {
                        try {
                            b.setDate(Timestamp.valueOf(dateStr));
                        } catch (IllegalArgumentException e) {
                            b.setDate(new java.util.Date(Timestamp.valueOf(dateStr).getTime()));
                        }
                    } else {
                        b.setDate(new java.util.Date(Timestamp.valueOf(dateStr).getTime()));
                    }

                    bookings.add(b);
                }
            }
        }

        private static void saveBookings(Connection conn) throws SQLException {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM bookings");
            }

            String sql = "INSERT INTO bookings (rideId, riderId, seatCount, status, paymentStatus, paymentId, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (Booking b : bookings) {
                    Ride ride = b.getRide();
                    Rider rider = b.getRider();
                    ps.setInt(1, ride != null ? ride.getId() : 0);
                    ps.setInt(2, rider != null ? rider.getId() : 0);
                    ps.setInt(3, b.getSeatCount());
                    ps.setString(4, b.getStatus());
                    ps.setString(5, b.getPaymentStatus());
                    ps.setInt(6, b.getPayment() != null ? b.getPayment().getId() : 0);
                    ps.setString(7, b.getDate() != null ? new Timestamp(b.getDate().getTime()).toString() : null);

                    ps.executeUpdate();

                    // Retrieve auto-generated ID
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            b.setId(rs.getInt(1));
                        }
                    }
                }
            }
        }

        // ------------------ NOTIFICATIONS ------------------
    // ------------------ NOTIFICATIONS ------------------
    private static void loadNotifications(Connection conn) throws SQLException {
        notifications.clear();
        String sql = "SELECT * FROM notifications";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setMessage(rs.getString("message"));
                n.setStatus(rs.getString("status"));

                // --- Recipient ---
                int recipientId = rs.getInt("recipientId");
                User recipient = Database.users.get(recipientId);
                n.setRecipient(recipient);

                // --- Timestamp ---
                String ts = rs.getString("timestamp");
                if (ts != null) {
                    try {
                        n.setTimestamp(java.sql.Timestamp.valueOf(ts));
                    } catch (IllegalArgumentException e1) {
                        try {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date = sdf.parse(ts);
                            n.setTimestamp(new java.sql.Timestamp(date.getTime()));
                        } catch (java.text.ParseException e2) {
                            System.out.println("Failed to parse timestamp: " + ts + ", using current time.");
                            n.setTimestamp(new java.util.Date());
                        }
                    }
                } else {
                    n.setTimestamp(new java.util.Date());
                }

                notifications.add(n);
            }
        }
    }

    private static void saveNotifications(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM notifications");
        }
        String sql = "INSERT INTO notifications (id, message, timestamp, status, recipientId) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Notification n : notifications) {
                ps.setInt(1, n.getId());
                ps.setString(2, n.getMessage());
                ps.setString(3, n.getTimestamp() != null ? n.getTimestamp().toString() : null);
                ps.setString(4, n.getStatus());
                ps.setInt(5, n.getRecipient() != null ? n.getRecipient().getId() : 0); // default 0 if null
                ps.executeUpdate();
            }
        }
    }


        public static int getNextRideId() {
        int maxId = 0;
        for (Ride r : rides) {
            if (r.getId() > maxId) maxId = r.getId();
        }
        return maxId + 1;
    }

    }
